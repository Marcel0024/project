package com.cherryberryapps.view;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cherryberryapps.model.CsvWriter;
import com.cherryberryapps.model.DBConnection;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Dataset3View extends VerticalLayout {
	
	private DBConnection connection;
	
	public Dataset3View() {
		connection = new DBConnection();
		
		setSizeFull();
		addComponent(buildHeader());
		
		Component body = buildBody();
		addComponent(body);
        setExpandRatio(body, 1);
        
        addComponent(buildFooter());
		
	}

	private Component buildHeader() {
		VerticalLayout header = new VerticalLayout();
		header.addStyleName("viewheader");
        header.setSpacing(true);
        header.setMargin(true);
        Responsive.makeResponsive(header);
        
        Label title = new Label("Europe map");
        
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        Label description = new Label("A map of Europe with every station displaying the humidity.");
        description.setSizeUndefined();
        description.addStyleName(ValoTheme.LABEL_H3);
        description.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        header.addComponent(title);
        header.addComponent(description);
        
		return header;
	}

	private Component buildBody() {
		HorizontalLayout body = new HorizontalLayout();
		body.setWidth("100%");
		body.setHeight("100%");
        body.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        body.setSpacing(true);
        body.setMargin(true);
        Responsive.makeResponsive(body);
        
		body.addComponent(buildGoogleMaps());
		//body.setExpandRatio(googleMap, 1.0f);
		
		return body;
	}

	private Component buildGoogleMaps() {
		GoogleMap googleMap = new GoogleMap(null, null, null);
		googleMap.setCenter(new LatLon(54.9000, 25.3167));
        googleMap.setMinZoom(3);
        googleMap.setZoom(4);
        googleMap.setSizeFull();
        addStations(googleMap);
        googleMap.addMarker("NOT DRAGGABLE: Iso-Heikkilä", new LatLon(
                60.450403, 22.230399), true, null);
        
        
        return googleMap;
	}

	private void addStations(GoogleMap googleMap) {
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		stations = connection.getStationsInEurope();
		
	}

	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
	    footer.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
	    footer.setSpacing(true);
	    footer.setMargin(true);
	    Responsive.makeResponsive(footer);
		
	    footer.addComponent(buildDownloadButton());
	    
		return footer;
	}

	private Component buildDownloadButton() {
		DateFormat dateFormatLong = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		DateFormat dateFormatShort = new SimpleDateFormat("dd_MM_yyy");
		Date date = new Date();
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "\\WEB-INF\\docs\\Dataset3" + dateFormatShort.format(date) +"\\";
		String file = dateFormatLong.format(date) + ".csv";
		
		CsvWriter writer = new CsvWriter(basepath , file);
		
		Button downloadButton = new Button("Download measurements");
		Resource res = new FileResource(new File(basepath + file));
		FileDownloader fd = new FileDownloader(res);
		fd.extend(downloadButton);
		
		return downloadButton;
	}

}
