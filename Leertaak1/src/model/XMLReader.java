package model;

import java.io.IOException;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class XMLReader {
	
	public XMLReader(Socket socket){
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLHandler handler = new XMLHandler() ;
			
			saxParser.parse(socket.getInputStream(), handler);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			//e.printStackTrace();
		}
	}
}
