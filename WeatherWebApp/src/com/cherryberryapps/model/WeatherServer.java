package com.cherryberryapps.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;




public class WeatherServer {
private static final int PORT = 7789;
ServerSocket server; 
DataHandler dataHandler;

	public WeatherServer(){
		try{
			server = new ServerSocket(PORT);
			System.out.println("server is running 1");
			dataHandler = new DataHandler();
			start();
			
			}catch(IOException ioe) {
				System.out.println("Could not connect to port 7789");
				System.err.println(ioe);
			}
		
	}
	
	public void start(){
		while(true){
			//listening to port
				try{
					Socket socket;
					socket = server.accept();
					new Thread(new Runnable(){
						@Override
						public void run() {
							readXML(socket);
							
						}}).start();
				} catch (IOException e){
						System.out.println(e);
			}
		}
	}
	
	private void readXML(Socket socket){
		try{
			 UserHandler userHandler = new UserHandler();
			 SAXParserFactory factory = SAXParserFactory.newInstance();
			 SAXParser saxParser = factory.newSAXParser();
	         saxParser.parse(new BufferedInputStream(socket.getInputStream()), userHandler);
	         
		}catch(SAXException se) {
			//se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
			
		
	}
	
}

