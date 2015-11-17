package com.cherryberryapps.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class CsvWriter {

	private CSVWriter writer;
	private String[] columnNames;
	
	public CsvWriter(String directory, String fileName, String columnName1, String columnName2) {
		try {
			File file = new File(directory);
			file.mkdirs();
			writer = new CSVWriter(new FileWriter(directory + fileName, true));
			columnNames = new String[] {columnName1, columnName2};
			printColumnNames(columnNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void write(String[] values) {
		try {
			writer.writeNext(values, false);
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
