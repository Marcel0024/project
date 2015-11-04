package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

public class CsvWriter {

	private CSVWriter writer;
	private String[] entries;
	private String[] columnNames = new String[]{"Station", "Date", "Time", "Temperature", "Dew point", "Air Pressure Station", 
			"Air Pressure Sea", "Visibility", "Wind Speed", "Precipitation", "Snow Depth", "Events", "Cloudiness", "Wind Direction"};
	
	public CsvWriter(String directory, String fileName) {
		try {
			File file = new File(directory);
			file.mkdirs();
			writer = new CSVWriter(new FileWriter(directory + fileName, true));
			if (isFileEmpty(directory + fileName)) {
				printColumnNames();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void write(ArrayList<Object> arrays, int amountOfArrays) {
		try {
			entries = new String[amountOfArrays];
			for (int i = 0; i < amountOfArrays; i++) {
				if (i == amountOfArrays - 1) {
					entries[i] = arrays.get(i).toString() + '\n';
				} else {
					entries[i] = arrays.get(i).toString();
				}
			}
			writer.writeNext(entries, false);
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printColumnNames() {
	    try {
	    	writer.writeNext(columnNames, true);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isFileEmpty(String directory) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(directory));
			return (br.readLine() == null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
