package model;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
	
	// Directory for storing the measurements.
	private final String DIRECTORY = "C:\\UNWDMI\\Measurements\\";
	// Error code used for when values are missing.
	private double error = 404.00;
	// Array with strings representing the XML elements.
	private String[] stringXmlElements;
	// Length of the array of strings representing the XML elements. This is used as the length of other arrays.
	private int amountOfValues;
	// Array with booleans representing the XML elements.
	private boolean[] boolXmlElements;
	// Array for storing the values of the XML elements.
	private ArrayList<Object> values; 
	// Object used for reading CSV files.
	private CsvReader csvReader;
	// Object for writing CSV files.
	private CsvWriter csvWriter;
	
	public XMLHandler() {
		stringXmlElements = new String[]{"STN", "DATE", "TIME", "TEMP", "DEWP", "STP", "SLP", "VISIB", "WDSP", "PRCP", "SNDP", "FRSHTT", "CLDC", "WNDDIR"};
		amountOfValues = stringXmlElements.length;
		boolXmlElements = new boolean[amountOfValues];
		values = new ArrayList<Object>(amountOfValues); 
		csvReader = new CsvReader();
	}
	/*
	 * Receive notification of the start of an element. 
	 * This is used to to check if the element is the same as one of the predefined strings in the stringXmlElements array.
	 * If this is true it will set a true boolean values in the boolXmlElements array at a specific index.
	 * 
	 * @param String uri - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
	 * @param String localName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
	 * @param String qName - The qualified name (with prefix), or the empty string if qualified names are not available.
	 * @param Attributes attributes - The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
 	public synchronized void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
 		
 		for (int i = 0; i < amountOfValues; i++) {
 			if (stringXmlElements[i].equals(qName)) {
 				boolXmlElements[i] = true;
 			}
 		}
	}
	/*
	 * Receive notification of character data inside an element.
	 * This used for inserting data into an array that will be used later on to write to a file
	 * 
	 * @param char ch - The characters.
	 * @param int start - The start position in the character array.
	 * @param int length - The number of characters to use from the character array.
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public synchronized void characters(char ch[], int start, int length) throws SAXException {
		
		for (int i = 0; i < amountOfValues; i++) {
			if (boolXmlElements[i]) {
				insertDataIntoArray(i, new String(ch, start, length));
				boolXmlElements[i] = false;
			}
		}
	}
	/*
	 * Receive notification of the end of an element.
	 * Method used for the following: 
	 *  - Calls checkData() for checking the data.
	 *  - Makes a new CsvWriter and writes to a file.
	 *  - Clears the array of values after writer.
	 * 
	 * @param String uri - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
	 * @param String localName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
	 * @param String qName - The qualified name (with prefix), or the empty string if qualified names are not available.
	 * @param Attributes attributes - The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("MEASUREMENT")) {
			checkData();
			csvWriter = new CsvWriter(DIRECTORY + csvReader.getDirectory((int) values.get(0)) + "\\" , values.get(1) +".csv");
			csvWriter.write(values, amountOfValues);
			values.clear();
				
		} else if (qName.equalsIgnoreCase("WEATHERDATA")) {
			throw new SAXException();
		}
	}
	
	/*
	 * Saves the values in the values array. 
	 * If a value is missing, the predefined error value is saved instead.
	 * If the station number is missing or can't be stored, a error message will be displayed.
	 * 
	 * @param int i - The index in the array
	 * @param Strin value - The value that needs to be saved in the array
	 * 
	 */
	private void insertDataIntoArray(int i, String value) {
		
		switch(i) {
		case 0: // Station
			try {
				values.add(Integer.parseInt(value));
			} catch (NumberFormatException | NullPointerException e) {
				 JOptionPane.showMessageDialog(new JPanel(), e.getMessage(), "Station error", JOptionPane.ERROR_MESSAGE);
			}
			break;
		case 1: // Date
		case 2: // Time
			values.add(value);
			break;
		case 3: // Temperature
		case 4: // Dew point
		case 5: // Air pressure station
		case 6: // Air pressure sea
		case 7: // Visibility
		case 8: // Wind speed
		case 10: // Snow depth
		case 12: // Cloudiness
			try {
				values.add(Math.round(Double.parseDouble(value)) / 10.0);
			} catch (NumberFormatException | NullPointerException e) {
				values.add(error);
			}
			break;
		case 9: // Precipitation
			try {
				values.add(Math.round(Double.parseDouble(value)) / 100.0);
			} catch (NumberFormatException | NullPointerException e) {
				values.add(error);
			}
			break;
		case 11: // Events
		case 13: // Wind direction
			try {
				values.add(Integer.parseInt(value));
			} catch (NumberFormatException | NullPointerException e) {
				values.add(error);
			}
			break;
		}
	}
	
	/*
	 * Uses a loop to check if the values meet the requirements.  Also calls the checkMissingData method for checking for missing data.
	 */

	private void checkData() {
		for (int i = 0; i < amountOfValues; i++) {
			checkMissingData(i);
			switch(i) {
			case 3: // Temperature
				double tempDouble = csvReader.getData((int) values.get(0), i, DIRECTORY);
				if ((Double) values.get(i) > tempDouble * 0.80 && (Double) values.get(i) < tempDouble * 1.20) {
					// Good to go
				} else {
					values.set(i, tempDouble);
				}
				break;
			case 4: // Dew point
			case 10: // Snow depth
				if ((Double) values.get(i) >= -9999.9 && (Double) values.get(i) <= 9999.9) {
					// Good to go
				} else {
					values.set(i, csvReader.getData((int) values.get(0), i, DIRECTORY));
				}
				break;
			case 5: // Air pressure station
			case 6: // Air pressure sea
			case 7: // Visibility
			case 8: // Wind speed
				if ((Double) values.get(i) >= 0.0 && (Double) values.get(i) <= 9999.9) {
					// Good to go
				} else {
					values.set(i, csvReader.getData((int)values.get(0), i, DIRECTORY));
				}
				break;
			case 9: // Precipitation
				if ((Double) values.get(i) >= 0.0 && (Double) values.get(i) <= 9999.99) {
					// Good to go
				} else {
					values.set(i, csvReader.getData((int) values.get(0), i, DIRECTORY));
				}
				break;
			case 12: // Cloudiness
				if ((Double) values.get(i) >= 0.0 && (Double) values.get(i) <= 99.9) {
					// Good to go
				} else {
					values.set(i, csvReader.getData((int) values.get(0), i, DIRECTORY));
				}
				break;
			case 13: // Wind direction
				if ((int) values.get(i) >= 0 && (int) values.get(i) <= 359) {
					// Good to go
				} else {
					values.set(i, csvReader.getEventOrWindDirection((int) values.get(0), i, DIRECTORY));
				}
			}
		}
	}
	/*
	 * Check for missing values. If a value is missing use extrapolate to store in the array.
	 * 
	 * @param int i - index of the value.
	 */
	private void checkMissingData(int i) {
		switch(i) {
		case 3: // Temperature
		case 4: // Dew point
		case 5: // Air pressure station
		case 6: // Air pressure sea
		case 7: // Visibility
		case 8: // Wind speed
		case 9: // Precipitation
		case 10: // Snow depth
		case 12: // Cloudiness
			if ((double) values.get(i) == error) {
				values.set(i, (double) csvReader.getData((int) values.get(0), i, DIRECTORY));
			}
			break;
		case 11: // Events
		case 13: // Wind direction
			if ((int) values.get(i) == error) {
				values.set(i, (int) csvReader.getEventOrWindDirection((int) values.get(0), i, DIRECTORY));
			}
			break;
		}
	}
}

