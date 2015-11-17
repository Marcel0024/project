package com.cherryberryapps.model;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;




public class UserHandler extends DefaultHandler {

	   boolean bStationName = false;
	   boolean bDate = false;
	   boolean bTime = false;
	   boolean bTemp = false;
	   boolean bDewPoint = false;
	   boolean bAirPresStation = false;
	   boolean bAirPresSea = false;
	   boolean bVisibility = false;
	   boolean bWindSpeed = false;
	   boolean bRainFall = false;
	   boolean bSnow = false;
	   boolean bEvents = false;
	   boolean bOverCast = false;
	   boolean bWindDir = false;
	 
	   boolean insertPass= false;
	   int counter = 0;
	   int counterSQL = 0;
	   
	   
	   char ch[];
	   int start;
	   int length;
	   Object[] measurments = new Object[14];
	   
	   UserHandler(){
		   
		   for(int i=0; i<=13;i++){
			   if(i <= 0){
				   measurments[i] = new ArrayList<Double>();
			   }else if((i > 0) && (i <= 2)){
				   measurments[i] = new ArrayList<String>();
			   }else if((i > 2) && (i < 11)){
				   measurments[i] = new ArrayList<Double>();
			   }else if(i == 11){
				   measurments[i] = new ArrayList<String>();
			   }else{
				   measurments[i] = new ArrayList<Double>();
			   }   
		   }
	   }
	   

	   @Override
	   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	      switch(qName){
	      case "MEASUREMENT" :
	    	  break;
	      case "STN" :
	    	  bStationName = true;
	    	  break;
	      case "DATE" :
	    	  bDate = true;
	    	  break;
	      case "TIME" :
	    	  bTime = true;
	    	  break;
	      case "TEMP" :
	    	  bTemp = true;
	    	  break;
	      case "DEWP" :
	    	  bDewPoint = true;
	    	  break;
	      case "STP" :
	    	  bAirPresStation = true;
	    	  break;
	      case "SLP" :
	    	  bAirPresSea = true;
	    	  break;
	      case "VISIB" :
	    	  bVisibility = true;
	    	  break;
	      case "WDSP" :
	    	  bWindSpeed = true;
	    	  break;
	      case "PRCP" :
	    	  bRainFall = true;
	    	  break;
	      case "SNDP" :
	    	  bSnow = true;
	    	  break;
	      case "FRSHTT" :
	    	  bEvents = true;
	    	  break;
	      case "CLDC" :
	    	  bOverCast = true;
	    	  break;
	      case "WNDDIR" :
	    	  bWindDir = true;
	    	  break;
	      }
	      
	   }

	   @Override
	   public void endElement(String uri, String localName, String qName) throws SAXException {
	      if (qName.equalsIgnoreCase("measurement")) {
	         //System.out.println("End Element :" + qName);
	         if(insertPass == false){
	        	 if(counter == 9){
	        		 insertPass = true;
	        	 }
	        	 counter++;
	         }
	         if(insertPass == true){
	        	 insertData();
	         }
	        
	      } else if(qName.equalsIgnoreCase("weatherdata")) {
	    	  throw new SAXException();
	      }
	   }

	   @Override
	   public void characters(char ch[], int start, int length) throws SAXException {
		   this.ch = ch;
		   this.start = start;
		   this. length = length;
		   if (bStationName) {
			 data(0);
	         bStationName = false;
	      } else if (bDate) {
	         data(1);
	         bDate = false;
	      } else if (bTime) {
	    	 data(2);
	         bTime = false;
	      } else if (bTemp) {
	     	 data(3);
	         bTemp = false;
	      } else if (bDewPoint) {
	         data(4);
		     bDewPoint = false;    	  
	      } else if (bAirPresStation) {
	         data(5);
		     bAirPresStation = false;
	      } else if (bAirPresSea) {
	         data(6);
	         bAirPresSea = false;
	      } else if (bVisibility) {
	    	 data(7);
		     bVisibility = false;
	      } else if (bWindSpeed) {
	    	 data(8);
		     bWindSpeed = false;
	      } else if (bRainFall) {
	    	 data(9);
		     bRainFall = false;
	      } else if (bSnow) {
	    	 data(10);
		     bSnow = false;
	      } else if (bEvents) {
	    	 data(11);
		     bEvents = false;
	      } else if (bOverCast) {
	    	 data(12);
		     bOverCast = false;
	      } else if (bWindDir) {
	    	 data(13);
		     bWindDir = false;
	      }
		  
		  
	      
	   }
	   
	  
	@SuppressWarnings("unchecked")
	public void data(int i){
		   String data;
		   data = new String(ch, start, length);
		   if(data.equals("\n\t\t") || data.equals("\n\t")){
			   data = DataHandler.extrapolate(i);
		   }else if(false){
			   String avgTemp = DataHandler.extrapolateTemp((ArrayList<Double>) measurments[0],i);
			   double avg = Double.parseDouble(avgTemp);
			   double temp = Double.parseDouble(data);
			   double dHighVal = avg * 1.20;
			   double dLowVal = avg * 0.80;
			   if(temp >= dHighVal || temp <= dLowVal){
				   data = new Double(avg).toString();
			   }  
		   }
		   if(i <= 0){
			   ((ArrayList<Double>) measurments[i]).add(Double.parseDouble(data));
		   }else if((i > 0) && (i <= 2)){
			   ((ArrayList<String>) measurments[i]).add(data);
		   }else if((i > 2) && (i < 11)){
			   ((ArrayList<Double>) measurments[i]).add(Double.parseDouble(data));
		   }else if(i == 11){
			   ((ArrayList<String>) measurments[i]).add(data);
		   }else{
			   ((ArrayList<Double>) measurments[i]).add(Double.parseDouble(data));
		   }
		   
	   }
	
	public void insertData(){
		DataHandler.insertData(measurments);
		
	}
	   
}
