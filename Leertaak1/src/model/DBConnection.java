package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private int station;
	private String date;
	private String time;
	private double temperature;
	private double dewPoint;
	private double AirPressureStation;
	private double AirPressureSea;
	private double visibility;
	private double windSpeed;
	private double precipitation;
	private double snowDepth;
	private int event;
	private double cloudiness;
	private int windDirection;
	
	public DBConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unwdmi", "root", "");
			statement = connection.createStatement();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void insertData(String[] measurements) {
		station = Integer.parseInt(measurements[0]);
		date = measurements[1].replace("-", "");
		time = measurements[2].replace(":", "");
		temperature = Double.parseDouble(measurements[3]);
		dewPoint = Double.parseDouble(measurements[4]);
		AirPressureStation = Double.parseDouble(measurements[5]);
		AirPressureSea = Double.parseDouble(measurements[6]);
		visibility = Double.parseDouble(measurements[7]);
		windSpeed = Double.parseDouble(measurements[8]);
		precipitation = Double.parseDouble(measurements[9]);
		snowDepth = Double.parseDouble(measurements[10]);
		event = Integer.parseInt(measurements[11]);
		cloudiness = Double.parseDouble(measurements[12]);
		windDirection = Integer.parseInt(measurements[13]);
		
		try {
			statement.executeUpdate("INSERT INTO measurements(station, date, time, temperature, dew_point, air_pressure_station, air_pressure_sea, visibility, wind_speed, precipitation, snow_depth, events, cloudiness, wind_direction) "
					+ "VALUES ("+ station +", "+ date +", "+ time +", "+ temperature +", "+ dewPoint +", "+ AirPressureStation +", "+ AirPressureSea +", "+ visibility +", "+ windSpeed +", "+ precipitation +", "+ snowDepth +", "+ event +", "+ cloudiness +", "+ windDirection +")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized double getData(String station, String columnName) {
		double dTemp = 0;
		int i = 0;
		try {
			resultSet = statement.executeQuery("SELECT "+ columnName +" FROM measurements WHERE station = " + station + " ORDER BY measurement_id DESC");
			if (resultSet.isBeforeFirst()) {
				while (resultSet.next() | i < 30){
					dTemp += resultSet.getDouble(columnName);
					i++;
				}
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dTemp / i;
	}
}
