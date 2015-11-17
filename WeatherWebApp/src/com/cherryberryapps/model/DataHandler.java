package com.cherryberryapps.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DataHandler {

	static PreparedStatement statement1;
	static Connection con;
	
	public DataHandler(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/unwdmi?rewriteBatchedStatements=true","root","");
			
			
			   } catch (SQLException e) {
				   e.printStackTrace();
			   } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	
	@SuppressWarnings("unchecked")
	public static synchronized void insertData(Object[] measurments){
		try {
			statement1 = con.prepareStatement("INSERT INTO measurements(station, date, time, temperature, dew_point, air_pressure_station, air_pressure_sea, visibility, wind_speed, precipitation, snow_depth, events, cloudiness, wind_direction) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		for(int i = 0; i < 10;i++){
		double stn = ((ArrayList<Double>) measurments[0]).get(i);
		String date = (((ArrayList<String>) measurments[1]).get(i).replace("-",""));
		String time = (((ArrayList<String>) measurments[2]).get(i).replace(":",""));
		double temperature = ((ArrayList<Double>) measurments[3]).get(i);
		double dewpoint = ((ArrayList<Double>) measurments[4]).get(i);
		double airPressureStn = ((ArrayList<Double>) measurments[5]).get(i);
		double airPressureSea = ((ArrayList<Double>) measurments[6]).get(i);
		double visibility = ((ArrayList<Double>) measurments[7]).get(i);
		double windSpeed = ((ArrayList<Double>) measurments[8]).get(i);
		double rainFall = ((ArrayList<Double>) measurments[9]).get(i);
		double snow = ((ArrayList<Double>) measurments[10]).get(i);
		String events = ((ArrayList<String>) measurments[11]).get(i);
		double overCast = ((ArrayList<Double>) measurments[12]).get(i);
		double windDirection = ((ArrayList<Double>) measurments[13]).get(i);
		
		try {
			
			statement1.setDouble(1,stn);
			statement1.setString(2,date);
			statement1.setString(3,time);
			statement1.setDouble(4,temperature);
			statement1.setDouble(5,dewpoint);
			statement1.setDouble(6,airPressureStn);
			statement1.setDouble(7,airPressureSea);
			statement1.setDouble(8,visibility);
			statement1.setDouble(9,windSpeed);
			statement1.setDouble(10,rainFall);
			statement1.setDouble(11,snow);
			statement1.setString(12,events);
			statement1.setDouble(13,overCast);
			statement1.setDouble(14,windDirection);
			statement1.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
		try {
			statement1.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized String extrapolate(int i){
		if((i > 2 && i < 11) || i > 11){
			String table = null;
			switch(i){
			case 3:
				table = "temperature";
				break;
			case 4:
				table = "dewpoint";
				break;
			case 5:
				table = "air_pressure_stn";
				break;
			case 6:
				table = "air_pressure_sea";
				break;
			case 7:
				table = "visibility";
				break;
			case 8:
				table = "wind_speed";
				break;
			case 9:
				table = "rainfall";
				break;
			case 10:
				table = "fallen_snow";
				break;
			case 12:
				table = "overcast";
				break;
			case 13:
				table = "wind_direction";
				break;
			}
			try {
				statement1 = con.prepareStatement("SELECT "+ table +" AS average FROM measurements ORDER BY ID DESC LIMIT 30");
				ResultSet rs = statement1.executeQuery();
				int avg = 0;
				while(rs.next()){
					 avg =+ rs.getInt("average");
				}
				avg = avg/30;
				return new Integer(avg).toString();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}else{
			return "000000";
		}
		return null;
	}
	
	public static synchronized String extrapolateTemp(ArrayList<Double> stations,int i){
		if(i == 3){
			Double d = stations.get(stations.size() -1);
			int stn = (int) d.doubleValue();
			try {
				statement1 = con.prepareStatement("SELECT temperature AS average "
						+ "FROM measurements,stations "
						+ "WHERE stations.country = (SELECT country "
						+ "FROM stations "
						+ "WHERE stations.stn = "+stn+ ") "
						+ "AND measurements.station = stations.stn "
						+ "ORDER BY measurements.ID DESC LIMIT 30 ");
				ResultSet rs = statement1.executeQuery();
				double avg = 0;
				while(rs.next()){
					 avg = avg + rs.getDouble("average");
				}
				avg = avg/30;
				return new Double(avg).toString();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		return null;	
	}
}
