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
	
	String[] strXmlElementsValue = new String[14];
	
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
				strXmlElementsValue[i] = new String(ch, start, length);
				boolXmlElements[i] = false;
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("MEASUREMENT")){
			checkMeasurements(strXmlElementsValue);
			
		} else if (qName.equalsIgnoreCase("WEATHERDATA")){
			throw new SAXException();
		}
	}
	
	private void checkMeasurements(String[] strXmlElementsValue) {

		double dTemp;
		double dExtrapolation;
		DBConnection db = new DBConnection();
		//Temperature
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[3])) / 10.0;
			if (dTemp >= -9999.9 && dTemp <= 9999.9){
				dExtrapolation = db.getData(strXmlElementsValue[0], "temperature");
				if (dExtrapolation != 0){
					if (dTemp * 1.20 >= dExtrapolation | dTemp * 0.80 <= dExtrapolation){
						strXmlElementsValue[3] = String.valueOf(dExtrapolation);
					}
				} else {
					strXmlElementsValue[3] = String.valueOf((-9999.9 +  9999.9) / 2);
				}
			} else {
				strXmlElementsValue[3] = String.valueOf(db.getData(strXmlElementsValue[0], "temperature"));
			}
		} catch (NumberFormatException e){
			strXmlElementsValue[3] = String.valueOf(db.getData(strXmlElementsValue[0], "temperature"));
		}
		
		//Dew point
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[4])) / 10.0;
			if (dTemp >= -9999.9 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElementsValue[4] = String.valueOf(db.getData(strXmlElementsValue[0], "dew_point"));
			}
		} catch (NumberFormatException e){
			strXmlElementsValue[4] = String.valueOf(db.getData(strXmlElementsValue[0], "dew_point"));
		}
		
		//Air pressure station
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[5])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "air_pressure_station");
				if (dExtrapolation == 0){
					strXmlElementsValue[5] = String.valueOf((9999.9 + 0.0) /2);
				} else {
					strXmlElementsValue[5] = String.valueOf(db.getData(strXmlElementsValue[0], "air_pressure_station"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "air_pressure_station");
			if (dExtrapolation == 0){
				strXmlElementsValue[5] = String.valueOf((9999.9 + 0.0) /2);
			} else {
				strXmlElementsValue[5] = String.valueOf(db.getData(strXmlElementsValue[0], "air_pressure_station"));
			}
		}
		
		//Air pressure sea
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[6])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "air_pressure_sea");
				if (dExtrapolation == 0){
					strXmlElementsValue[6] = String.valueOf((9999.9 + 0.0) /2);
				} else {
					strXmlElementsValue[6] = String.valueOf(db.getData(strXmlElementsValue[0], "air_pressure_sea"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "air_pressure_sea");
			if (dExtrapolation == 0){
				strXmlElementsValue[6] = String.valueOf((9999.9 + 0.0) /2);
			} else {
				strXmlElementsValue[6] = String.valueOf(db.getData(strXmlElementsValue[0], "air_pressure_sea"));
			}
		}
		
		//Visibility
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[7])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "visibility");
				if (dExtrapolation == 0){
					strXmlElementsValue[7] = String.valueOf((9999.9 + 0.0) /2);
				} else {
					strXmlElementsValue[7] = String.valueOf(db.getData(strXmlElementsValue[0], "visibility"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "visibility");
			if (dExtrapolation == 0){
				strXmlElementsValue[7] = String.valueOf((9999.9 + 0.0) /2);
			} else {
				strXmlElementsValue[7] = String.valueOf(db.getData(strXmlElementsValue[0], "visibility"));
			}
		}
		
		//Wind speed
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[8])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 9999.9){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "wind_speed");
				if (dExtrapolation == 0){
					strXmlElementsValue[8] = String.valueOf((9999.9 + 0.0) /2);
				} else {
					strXmlElementsValue[8] = String.valueOf(db.getData(strXmlElementsValue[0], "wind_speed"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "wind_speed");
			if (dExtrapolation == 0){
				strXmlElementsValue[8] = String.valueOf((9999.9 + 0.0) /2);
			} else {
				strXmlElementsValue[8] = String.valueOf(db.getData(strXmlElementsValue[0], "wind_speed"));
			}
		}

		//Precipitation
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[9])) / 100.0;
			if (dTemp >= 0.00 && dTemp <= 9999.99){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "precipitation");
				if (dExtrapolation == 0){
					strXmlElementsValue[9] = String.valueOf((9999.9 + 0.0) /2);
				} else {
					strXmlElementsValue[9] = String.valueOf(db.getData(strXmlElementsValue[0], "precipitation"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "precipitation");
			if (dExtrapolation == 0){
				strXmlElementsValue[9] = String.valueOf((9999.9 + 0.0) /2);
			} else {
				strXmlElementsValue[9] = String.valueOf(db.getData(strXmlElementsValue[0], "precipitation"));
			}
		}

		//Snow depth
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[10])) / 10.0;
			if (dTemp >= -9999.9 && dTemp <= 9999.9){
				//Good to go
			} else {
				strXmlElementsValue[10] = String.valueOf(db.getData(strXmlElementsValue[0], "snow_depth"));
			}
		} catch (NumberFormatException e){
			strXmlElementsValue[10] = String.valueOf(db.getData(strXmlElementsValue[0], "snow_depth"));
		}

		//Cloudiness
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[12])) / 10.0;
			if (dTemp >= 0.0 && dTemp <= 99.9){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "cloudiness");
				if (dExtrapolation == 0){
					strXmlElementsValue[12] = String.valueOf((99.9 + 0.0) /2);
				} else {
					strXmlElementsValue[12] = String.valueOf(db.getData(strXmlElementsValue[0], "cloudiness"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "cloudiness");
			if (dExtrapolation == 0){
				strXmlElementsValue[12] = String.valueOf((99.9 + 0.0) /2);
			} else {
				strXmlElementsValue[12] = String.valueOf(db.getData(strXmlElementsValue[0], "cloudiness"));
			}
		}
		
		//Wind direction
		try {
			dTemp = Math.round(Double.parseDouble(strXmlElementsValue[13]));
			if (dTemp >= 0 && dTemp <= 359){
				//Good to go
			} else {
				dExtrapolation = db.getData(strXmlElementsValue[0], "wind_direction");
				if (dExtrapolation == 0){
					strXmlElementsValue[12] = String.valueOf(359 /2);
				} else {
					strXmlElementsValue[12] = String.valueOf(db.getData(strXmlElementsValue[0], "wind_direction"));
				}
			}
		} catch (NumberFormatException e){
			dExtrapolation = db.getData(strXmlElementsValue[0], "wind_direction");
			if (dExtrapolation == 0){
				strXmlElementsValue[12] = String.valueOf(359 /2);
			} else {
				strXmlElementsValue[12] = String.valueOf(db.getData(strXmlElementsValue[0], "wind_direction"));
			}
		}
		
		db.insertData(strXmlElementsValue);
		db.closeConnection();
	}
	
}

