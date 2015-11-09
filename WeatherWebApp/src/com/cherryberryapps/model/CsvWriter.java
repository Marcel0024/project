package com.cherryberryapps.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class CsvWriter {

	private CSVWriter writer;
	private String[] entries;
	private String[] columnNames = new String[]{"Country", "Temperature"};
	
	public CsvWriter(String directory, String fileName) {
		try {
			File file = new File(directory);
			file.mkdirs();
			writer = new CSVWriter(new FileWriter(directory + fileName, true));
			printColumnNames(columnNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void write(String[][] values) {
		try {
			entries = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				if (i == values.length - 1) {
					entries[i] = values[i][0] + "," + (double) Math.round(Double.parseDouble(values[i][1])* 100) / 100 + '\n';
				} else {
					entries[i] = values[i][0] + "," + (double) Math.round(Double.parseDouble(values[i][1])* 100) / 100;
				}
			}
			writer.writeNext(entries, false);
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printColumnNames(String[] columnNames) {
	    try {
	    	writer.writeNext(columnNames, true);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
