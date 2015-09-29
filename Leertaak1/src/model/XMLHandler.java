package model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
	/*Station
	 * Date
	 * Time
	 * Temperature
	 * Dew point
	 * Air pressure station
	 * Air pressure sea
	 * Visibility 
	 * Wind speed
	 * Precipitation
	 * Snow depth
	 * Events
	 * Cloudiness
	 * Wind direction
	 */
	boolean[] boolXmlElements = new boolean[14];
	
	String[] strXmlElements = new String[14];
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		switch (qName){
			case "STN":
				boolXmlElements[0] = true;
				break;
			case "DATE":
				boolXmlElements[1] = true;
				break;
			case "TIME":
				boolXmlElements[2] = true;
				break;
			case "TEMP":
				boolXmlElements[3] =true;
				break;
			case "DEWP":
				boolXmlElements[4] = true;
				break;
			case "STP":
				boolXmlElements[5] = true;
				break;
			case "SLP":
				boolXmlElements[6] = true;
				break;
			case "VISIB":
				boolXmlElements[7] =true;
				break;
			case "WDSP":
				boolXmlElements[8] = true;
				break;
			case "PRCP":
				boolXmlElements[9] = true;
				break;
			case "SNDP":
				boolXmlElements[10] = true;
				break;
			case "FRSHTT":
				boolXmlElements[11] = true;
				break;
			case "CLDC":
				boolXmlElements[12] = true;
				break;
			case "WNDDIR":
				boolXmlElements[13] = true;
				break;
		}
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		
		for (int i = 0; i < boolXmlElements.length; i++){
			if (boolXmlElements[i]) {
				strXmlElements[i] = new String(ch, start, length);
				boolXmlElements[i] = false;
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("MEASUREMENT")){
			checkMeasurements(strXmlElements);
			
		} else if (qName.equalsIgnoreCase("WEATHERDATA")){
			throw new SAXException();
		}
	}
	
	private void checkMeasurements(String[] strXmlElements) {

		double dTemp;
		DBConnection db = new DBConnection();
		//Temperature
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[3])) / 10.0;
			if (dTemp >= -9999.9 && dTemp <= 9999.9){
				double extrapolation = db.getData(strXmlElements[0], "temperature");
				if (dTemp * 1.20 >= extrapolation | dTemp * 0.80 <= extrapolation){
					strXmlElements[3] = String.valueOf(extrapolation);
				}
			} else {
				strXmlElements[3] = String.valueOf(db.getData(strXmlElements[0], "temperature"));
			}
		} catch (NumberFormatException e){
			strXmlElements[3] = String.valueOf(db.getData(strXmlElements[0], "temperature"));
		}
		
		//Dew point
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[4])) / 10.0;
			if (dTemp >= -9999.9 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElements[4] = String.valueOf(db.getData(strXmlElements[0], "dew_point"));
			}
		} catch (NumberFormatException e){
			strXmlElements[4] = String.valueOf(db.getData(strXmlElements[0], "dew_point"));
		}
		
		//Air pressure station
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[5])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElements[5] = String.valueOf(db.getData(strXmlElements[0], "air_pressure_station"));
			}
		} catch (NumberFormatException e){
			strXmlElements[5] = String.valueOf(db.getData(strXmlElements[0], "air_pressure_station"));
		}
		
		//Air pressure sea
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[6])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElements[6] = String.valueOf(db.getData(strXmlElements[0], "air_pressure_sea"));
			}
		} catch (NumberFormatException e){
			strXmlElements[6] = String.valueOf(db.getData(strXmlElements[0], "air_pressure_sea"));
		}
		
		
		//Visibility
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[7])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElements[7] = String.valueOf(db.getData(strXmlElements[0], "visibility"));
			}
		} catch (NumberFormatException e){
			strXmlElements[7] = String.valueOf(db.getData(strXmlElements[0], "visibility"));
		}
		
		
		//Wind speed
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[8])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElements[8] = String.valueOf(db.getData(strXmlElements[0], "wind_speed"));
			}
		} catch (NumberFormatException e){
			strXmlElements[8] = String.valueOf(db.getData(strXmlElements[0], "wind_speed"));
		}

		//Precipitation
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[9])) / 100.0;
			if (dTemp >= 0.00 && dTemp <= 9999.99){
				//Good to go
			} else {
				strXmlElements[9] = String.valueOf(db.getData(strXmlElements[0], "precipitation"));
			}
		} catch (NumberFormatException e){
			strXmlElements[9] = String.valueOf(db.getData(strXmlElements[0], "precipitation"));
		}

		//Snow depth
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[10])) / 10.0;
			if (dTemp >= -9999.9 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElements[10] = String.valueOf(db.getData(strXmlElements[0], "snow_depth"));
			}
		} catch (NumberFormatException e){
			strXmlElements[10] = String.valueOf(db.getData(strXmlElements[0], "snow_depth"));
		}

		//Cloudiness
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[12])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 99.9){
				//Good to go
			} else {
				strXmlElements[12] = String.valueOf(db.getData(strXmlElements[0], "cloudiness"));
			}
		} catch (NumberFormatException e){
			strXmlElements[12] = String.valueOf(db.getData(strXmlElements[0], "cloudiness"));
		}
		
		//Wind direction
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElements[13]));
			if (dTemp >= 0 && dTemp <= 359){
				//Good to go
			} else {
				strXmlElements[13] = String.valueOf(db.getData(strXmlElements[0], "wind_direction"));
			}
		} catch (NumberFormatException e){
			strXmlElements[13] = String.valueOf(db.getData(strXmlElements[0], "wind_direction"));
		}
		
		db.insertData(strXmlElements);
		db.closeConnection();
	}
	
}

