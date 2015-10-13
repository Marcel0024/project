package model;

import java.util.ArrayList;

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
	private int amountOfArrays = 14;
	
	private int amountOfValues = 10;
	
	private boolean[] boolXmlElements = new boolean[amountOfArrays];
	
	private Object[] tempArray = new Object[amountOfArrays];
	
	private ArrayList<ArrayList<Object>> arrays = new ArrayList<ArrayList<Object>>(amountOfArrays); 
	
	private DBConnection db;
	
	public XMLHandler(DBConnection db) {
		this.db = db;
	}
	
	public void initArrays(){
		
		for (int i = 0; i < amountOfArrays; i++){
			arrays.add(new ArrayList<Object>(amountOfValues));
		}
	}
	
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
		
		for (int i = 0; i < boolXmlElements.length; i++) {
			if (boolXmlElements[i]) {
				insertDataIntoArray(i, new String(ch, start, length));
				boolXmlElements[i] = false;
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("MEASUREMENT")){
			if (arrays.get(0).size() == amountOfValues) {
				checkData(arrays);
				db.insertDataIntoDatabase(amountOfValues, arrays);
				for (int i = 0; i < amountOfValues; i++) {
					arrays.get(i).clear();
				}
			}
			
		} else if (qName.equalsIgnoreCase("WEATHERDATA")) {
			throw new SAXException();
		}
	}

	private void insertDataIntoArray(int i, String value) {
		
		switch(i) {
		case 0: // Station
			try {
				arrays.get(i).add(Integer.parseInt(value));
			} catch (NumberFormatException | NullPointerException e) {
				System.out.println("Station missing");
			}
			break;
		case 1: // Date
		case 2: // Time
		case 11: // Events
			arrays.get(i).add(value);
			break;
		case 3: // Temperature
		case 4: // Dew point
		case 10: // Snow depth
			try {
				arrays.get(i).add(Math.round(Double.parseDouble(value)) / 10.0);
			} catch (NumberFormatException | NullPointerException e) {
				arrays.get(i).add(null);
			}
			break;
		case 5: // Air pressure station
		case 6: // Air pressure sea
		case 7: // Visibility
		case 8: // Wind speed
			try {
				arrays.get(i).add(Math.round(Double.parseDouble(value)) / 10.0);
			} catch (NumberFormatException | NullPointerException e) {
				arrays.get(i).add(null);
			}
			break;
		case 9: // Precipitation
			try {
				arrays.get(i).add(Math.round(Double.parseDouble(value)) / 100.0);
			} catch (NumberFormatException | NullPointerException e) {
				arrays.get(i).add(null);
			}
			break;
		case 12: // Cloudiness
			try {
				arrays.get(i).add(Math.round(Double.parseDouble(value)) / 10.0);
			} catch (NumberFormatException | NullPointerException e) {
				arrays.get(i).add(null);
			}
			break;
		case 13: // Wind direction
			try {
				arrays.get(i).add(Integer.parseInt(value));
			} catch (NumberFormatException | NullPointerException e) {
				arrays.get(i).add(null);
			}
			break;
		}
	}

	private void checkData(ArrayList<ArrayList<Object>> arrays) {
		double tempDouble;
		for (int i = 0; i < amountOfValues; i++) {
			for (int c = 0; c < amountOfArrays; c++) {
				tempArray[c] = arrays.get(c).get(i);
				
				if (c == 11) {
					tempArray[c] = (tempArray[c].equals("")) ? db.getEvent((int)tempArray[0]) : tempArray[c];
					arrays.get(c).set(i, tempArray[c]);
					//continue;
				} else if (c ==13) {
					tempArray[c] = (tempArray[c].equals("")) ? db.getWindDirection((int)tempArray[0]) : tempArray[c];
					arrays.get(c).set(i, tempArray[c]);
					//continue;
				} else {
					tempArray[c] = (tempArray[c] == null) ? db.getData((int)tempArray[0], c) : tempArray[c];
				}
				
				switch(c) {
				case 3: // Temperature
					tempDouble = db.getData((int)tempArray[0], c);
					if ((Double) tempArray[c] > tempDouble * 0.80 && (Double) tempArray[c] < tempDouble * 1.20) {
						// Good to go
					} else {
						arrays.get(c).set(i, tempDouble);
					}
					break;
				case 4: // Dew point
				case 10: // Snow depth
					if ((Double) tempArray[c] >= -9999.9 && (Double) tempArray[c] <= 9999.9) {
						// Good to go
					} else {
						arrays.get(c).set(i, db.getData((int)tempArray[0], c));
					}
					break;
				case 5: // Air pressure station
				case 6: // Air pressure sea
				case 7: // Visibility
				case 8: // Wind speed
					if ((Double) tempArray[c] >= 0.0 && (Double) tempArray[c] <= 9999.9) {
						// Good to go
					} else {
						arrays.get(c).set(i, db.getData((int)tempArray[0], c));
					}
					break;
				case 9: // Precipitation
					if ((Double) tempArray[c] >= 0.0 && (Double) tempArray[c] <= 9999.99) {
						// Good to go
					} else {
						arrays.get(c).set(i, db.getData((int)tempArray[0], c));
					}
					break;
				case 12: // Cloudiness
					if ((Double) tempArray[c] >= 0.0 && (Double) tempArray[c] <= 99.9) {
						// Good to go
					} else {
						arrays.get(c).set(i, db.getData((int)tempArray[0], c));
					}
					break;
				case 13: // Wind direction
					if ((int) tempArray[c] >= 0 && (int) tempArray[c] <= 359) {
						// Good to go
					} else {
						arrays.get(c).set(i, db.getWindDirection((int)tempArray[0]));
					}
				}
			}
		}
	}
}

