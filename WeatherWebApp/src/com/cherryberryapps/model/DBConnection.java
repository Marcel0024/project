package com.cherryberryapps.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBConnection {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	public DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unwdmi", "root", "");
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isValidUser(String user, String password) {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT userID FROM users WHERE password ='"+password+"' AND username ='"+user+"'");
			if (resultSet.next()) {
				return true;		
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized String [][] getWindChillTemperatureInEurope() {
		String[][] returnValue = new String [10][2];
		int column;
		int row = 0;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT stations.country, AVG(13.12 + 0.6215 * measurements.temperature - 11.37 * POW(measurements.wind_speed, 0.16) + 0.3965*measurements.temperature* POW(measurements.wind_speed, 0.16)) AS WindChillTemperature FROM stations, measurements WHERE stations.continent = 'Europe' AND measurements.station = stations.stn AND measurements.date >= DATE_SUB(CURDATE(),INTERVAL 3 MONTH) GROUP BY stations.country ORDER BY WindChillTemperature LIMIT 10");
			while (resultSet.next()) {
				column = 0;
				returnValue[row][column] = resultSet.getString(1);
				column++;
				returnValue[row][column] = resultSet.getString(2);
				row++;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}

	public synchronized String[][] getWindChillTemperatureInCountry(String country) {
		String[][] returnValue = new String [8][2];
		int column;
		int row = 0;
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT measurements.time AS TimeStamp, AVG(13.12 + 0.6215 * measurements.temperature - 11.37 * POW(measurements.wind_speed, 0.16) + 0.3965 * measurements.temperature * POW(measurements.wind_speed, 0.16)) AS WindChillTemperature FROM stations, measurements WHERE stations.country = '"+ country + "' AND measurements.station = stations.stn AND measurements.date = current_date() AND measurements.time >= DATE_SUB(NOW(),INTERVAL 8 HOUR) GROUP BY UNIX_TIMESTAMP(TimeStamp) DIV 3600");
			while (resultSet.next()) {
				column = 0;
				returnValue[row][column] = resultSet.getString(1);
				column++;
				returnValue[row][column] = resultSet.getString(2);
				row++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return returnValue;
	}

	public ArrayList<String> getCountriesInEurope(double lat, double lon) {
		ArrayList<String> countries = new ArrayList<String>();
		double range = 0.50;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM stations WHERE (latitude BETWEEN "+ (lat - range) +" AND "+ (lat + range) +") AND (longitude BETWEEN "+ (lon - range) +" AND "+ (lon + range) +") AND continent= 'Europe'");
			while (resultSet.next()) {
					countries.add(resultSet.getString(3));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return countries;
	}
	
	public ArrayList<ArrayList<String>> getStationsInCountry(String country) {
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM stations WHERE country = '"+ country +"' AND continent= 'Europe'");
			int index = 0;
			while (resultSet.next()) {
					stations.add(index, new ArrayList<String>());
					stations.get(index).add(resultSet.getString(1));
					stations.get(index).add(resultSet.getString(2));
					stations.get(index).add(resultSet.getString(3));
					stations.get(index).add(resultSet.getString(4));
					stations.get(index).add(resultSet.getString(5));
					stations.get(index).add(resultSet.getString(6));
					stations.get(index).add(resultSet.getString(7));
					index++;
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stations;
	}

	public ArrayList<ArrayList<Object>> getDataForHumidity(String caption) {
		ArrayList<ArrayList<Object>> dataSeriesItems = new ArrayList<ArrayList<Object>>();
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT temperature, dew_point, time FROM measurements JOIN stations ON measurements.station = stations.stn WHERE stations.name = '"+ caption +"'");
			int index = 0;
			while (resultSet.next()) {
				dataSeriesItems.add(index, new ArrayList<Object>());
				dataSeriesItems.get(index).add(resultSet.getDouble(1));
				dataSeriesItems.get(index).add(resultSet.getDouble(2));
				dataSeriesItems.get(index).add(resultSet.getString(3).substring(0, 8));
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dataSeriesItems;
	}
	
	public ArrayList<ArrayList<Object>> getDataForRealTimeHumidity(String caption) {
		ArrayList<ArrayList<Object>> dataSeriesItems = new ArrayList<ArrayList<Object>>();
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT temperature, dew_point, time FROM measurements JOIN stations ON measurements.station = stations.stn WHERE concat(measurements.date,' ', measurements.time) >= DATE_SUB(NOW(), INTERVAL 161 MINUTE)  AND stations.name = '"+ caption +"'");
			int index = 0;
			while (resultSet.next()) {
				dataSeriesItems.add(index, new ArrayList<Object>());
				dataSeriesItems.get(index).add(resultSet.getDouble(1));
				dataSeriesItems.get(index).add(resultSet.getDouble(2));
				dataSeriesItems.get(index).add(resultSet.getString(3).substring(0, 8));
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dataSeriesItems;
	}
	
	public String[] getDataset2(){
		String[] dataSeries = new String[10];
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT ROUND(AVG(precipitation),2) AS average, stations.country AS country "
					+ "FROM measurements,stations "
					+ "WHERE measurements.station = stations.stn "
					+ "AND measurements.date between DATE_SUB(NOW(),INTERVAL 1 WEEK) AND NOW() "
					+ "GROUP BY stations.country "
					+ "ORDER BY average DESC LIMIT 5");
			int index = 0;
			while(resultSet.next()){
				dataSeries[index] = resultSet.getString("average");
				index++;
				dataSeries[index] = resultSet.getString("country");
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataSeries;
	}
	
	public String[] getDataset2data(String country){
		String[] dataSeries = new String[7];
		try {
			statement = connection.createStatement();
			int counter = 1;
			for(int index = 0; index < 7; index++){
				resultSet = statement.executeQuery("SELECT ROUND(AVG(measurements.precipitation),2) AS average "
					+ "FROM measurements, stations "
					+ "WHERE measurements.station = stations.stn "
					//+ "AND  measurements.time > (current_time() - INTERVAL "+ counter +" HOUR) "
					+ "AND measurements.date = current_date()  "
					+ "AND measurements.time >= DATE_SUB(NOW(),INTERVAL "+ counter +" HOUR) "
					+ "AND stations.country = '"+country+"'");
				boolean validData = resultSet.next();
				if(validData){
					if(resultSet.getString("average") != null){
						dataSeries[index] = resultSet.getString("average");
					}else {
						dataSeries[index] = "0";
					}	
				}
				if(validData == false){
					dataSeries[index] = "0";
				}
				counter++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataSeries;
	}

	
}
