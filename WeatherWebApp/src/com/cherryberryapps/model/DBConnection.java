package com.cherryberryapps.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	
	public synchronized void insertDataIntoDatabase(int amountOfValues, ArrayList<ArrayList<Object>> arrays) {
		try {
			PreparedStatement preparedStmt = connection.prepareStatement("INSERT INTO measurements(station, date, time, temperature, dew_point, air_pressure_station, air_pressure_sea, visibility, wind_speed, precipitation, snow_depth, events, cloudiness, wind_direction) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			for (int i = 0; i < amountOfValues; i++) {
				preparedStmt.setInt(1, (int) arrays.get(0).get(i)); // Station
				preparedStmt.setString(2, (String) ((String) arrays.get(1).get(i)).replace("-", "")); // Date
				preparedStmt.setString(3, (String) ((String) arrays.get(2).get(i)).replace(":", "")); // Time
				preparedStmt.setDouble(4, (double) arrays.get(3).get(i)); // Temperature
				preparedStmt.setDouble(5, (double) arrays.get(4).get(i)); // Dew point
				preparedStmt.setDouble(6, (double) arrays.get(5).get(i)); // Air pressure station
				preparedStmt.setDouble(7, (double) arrays.get(6).get(i)); // Air pressure sea
				preparedStmt.setDouble(8, (double) arrays.get(7).get(i)); // Visibility
				preparedStmt.setDouble(9, (double) arrays.get(8).get(i)); // Wind speed
				preparedStmt.setDouble(10, (double) arrays.get(9).get(i));  // Precipitation
				preparedStmt.setDouble(11, (double) arrays.get(10).get(i)); // Snow depth
				preparedStmt.setString(12, (String) arrays.get(11).get(i)); // Events
				preparedStmt.setDouble(13, (double) arrays.get(12).get(i)); // Cloudiness
				preparedStmt.setInt(14, (int) arrays.get(13).get(i)); // Wind direction
				preparedStmt.addBatch();
			}
			preparedStmt.executeBatch();
			preparedStmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized double getData(int station, int column) {
		double returnValue = 0.0;
		int i = 0;
		String columnName = null;
		
		switch (column) {
			case 3:
				columnName = "temperature";
				break;
			case 4:
				columnName = "dew_point";
				break;
			case 5:
				columnName = "air_pressure_station";
				break;
			case 6:
				columnName = "air_pressure_sea";
				break;
			case 7:
				columnName = "visibility";
				break;
			case 8:
				columnName = "wind_speed";
				break;
			case 9:
				columnName = "precipitation";
				break;
			case 10:
				columnName = "snow_depth";
				break;
			case 12:
				columnName = "cloudiness";
				break;
			case 13:
				columnName = "wind_direction";
				break;
		}
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT "+ columnName +" FROM measurements, stations WHERE stations.country = (SELECT country FROM stations WHERE stations.stn = "+ station + ") ORDER BY measurement_id DESC LIMIT 30");
			while (resultSet.next()) {
				returnValue += resultSet.getDouble(columnName);
				i++;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnValue / i;
	}
	
	public synchronized String getEvent(int station) {
		String returnValue = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT events FROM measurements, stations WHERE stations.country = (SELECT country FROM stations WHERE stations.stn = "+ station + ") ORDER BY measurement_id DESC LIMIT 1");
			if (resultSet.next()) {
				returnValue = resultSet.getString("events");
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public Object getWindDirection(int station) {
		int returnValue = 0;
		int i = 0;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT wind_direction FROM measurements, stations WHERE stations.country = (SELECT country FROM stations WHERE stations.stn = "+ station + ") ORDER BY measurement_id DESC LIMIT 30");
			while (resultSet.next()) {
				returnValue += resultSet.getInt("wind_direction");
				i++;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnValue / i;
	}

	public boolean isValidUser(String user, String password) {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT userID FROM users WHERE password ='"+password+"' AND username ='"+user+"'");
			while (resultSet.next()) {
				if (resultSet.getInt(1) == 1) {
					return true;
				} 				
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized String [][] getDataset1() {
		String[][] returnValue = new String [10][2];
		int column;
		int row = 0;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT stations.country, AVG(13.12 + 0.6215 * measurements.temperature - 11.37 * POW(measurements.wind_speed, 0.16) + 0.3965*measurements.temperature* POW(measurements.wind_speed, 0.16)) AS WindChillTemperature FROM stations, measurements WHERE stations.continent = 'Europe' AND measurements.station = stations.stn GROUP BY stations.country ORDER BY WindChillTemperature LIMIT 10");
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

	public synchronized String[][] getDataset1Data(String item) {
		String[][] returnValue = new String [8][2];
		String[] string = item.split(" ");
		String country = string[0];
		int column;
		int row = 0;
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT measurements.time AS TimeStamp, AVG(13.12 + 0.6215 * measurements.temperature - 11.37 * POW(measurements.wind_speed, 0.16) + 0.3965 * measurements.temperature * POW(measurements.wind_speed, 0.16)) AS WindChillTemperature FROM stations, measurements WHERE stations.country = '" + country + "' AND measurements.station = stations.stn AND measurements.time >= DATE_SUB(NOW(),INTERVAL 8 HOUR) GROUP BY UNIX_TIMESTAMP(TimeStamp) DIV 3600");
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
}
