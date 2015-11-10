package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class CsvReader {
	
	private CSVReader reader; 
	private final String STATIONS_DIRECTORY = "C:\\UNWDMI\\Stations\\Stations.csv";
	
	String getDirectory(int stationNumber) {
		String returnString = null;
		BufferedReader bufferedReader = null;
		String line;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(STATIONS_DIRECTORY));
			while ((line = bufferedReader.readLine()) != null) {
			    // use comma as separator
				String[] station = line.split(",");
				 if (station[0].subSequence(1, station[0].length() - 1).equals(String.valueOf(stationNumber))) {
					 returnString = (String) station[2].subSequence(1, station[2].length() - 1) + "//" + station[1].subSequence(1, station[1].length() - 1);
				 }
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}
	
	public double getData(int stationNumber, int data, String filePath) {
		String directory = getDirectory(stationNumber);
		File dir = new File(filePath + directory);
		File[] files = dir.listFiles();
		double returnValue = 0.0;
		int counter = 0;
		
		if (files != null) {
			for (File file : files) {
				 if (file.isFile()) {
	                try {
	                	reader = new CSVReader(new FileReader(file));
	                    String [] nextLine;
	                    counter = 0; reader.readNext();
	                    while ((nextLine = reader.readNext()) != null) {
	                    	returnValue += Double.parseDouble(nextLine[data]);
	                        counter++;
	                    }
	                } catch (IOException e) {
	                	e.printStackTrace();
	                }
	                finally {
	                    if (reader != null) {
	                        try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
	                    }
	                }
				 }
			}
		 } else {
			 System.out.println("No files in given directory");
		 }
		 return returnValue / counter;
	}

	public Object getEventOrWindDirection(int stationNumber, int data, String filePath) {
		String country = getDirectory(stationNumber);
		File dir = new File(filePath + country);
		File[] files = dir.listFiles();
		BufferedReader bufferedReader = null;
		String line;
		String returnString = null;
		File lastModifiedFile = files[0];
	    for (int i = 1; i < files.length; i++) {
	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
	           lastModifiedFile = files[i];
	       }
	    }
	    
	    try {
	    	bufferedReader = new BufferedReader(new FileReader(lastModifiedFile));
	    	bufferedReader.readLine();
	    	 while ((line = bufferedReader.readLine()) != null) {
	    		 String[] values = line.split(",");
	    		 returnString = values[data];
	    	 }
	    } catch ( IOException e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		return returnString;
	}
}
