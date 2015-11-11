package com.cherryberryapps.view;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.cherryberryapps.model.CsvWriter;
import com.cherryberryapps.model.DBConnection;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Dataset3View extends VerticalLayout {
	
	private UI window;
	private DBConnection connection;	
	
	public Dataset3View(UI window) {
		this.window = window;
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
        googleMap.addMapClickListener(new MapClickListener() {
			
			@Override
			public void mapClicked(LatLon position) {
				googleMap.clearMarkers();
				ArrayList<String> countries = connection.getCountriesInEurope(position.getLat(), position.getLon());
				Iterator<String> it = countries.iterator();
				while (it.hasNext()) {
					addStations(googleMap, it.next());
				} 
			}

		});
        googleMap.addMarkerClickListener(new MarkerClickListener() {
			
			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				Window subWindow = new Window();
				subWindow.center();
				subWindow.setWidth("50%");
				subWindow.setHeight("50%");
				
				VerticalLayout subContent = new VerticalLayout();
		        subContent.setMargin(true);
		        subContent.addComponent(buildChart(clickedMarker.getCaption()));
		        
		        subWindow.setContent(subContent);		        
		        window.addWindow(subWindow);
			}
		});
        
        return googleMap;
	}

	protected Component buildChart(String caption) {
		
		ArrayList<ArrayList<Object>> dataSeriesItems = connection.getDataForHumidity(caption);
		calulateHumidity(dataSeriesItems);
		Chart chart = new Chart(ChartType.LINE);
		chart.setSizeFull();
		
		DataSeries dataSeries = new DataSeries();
		
		//Configuration
		Configuration conf = chart.getConfiguration();
		conf.setTitle(caption);
		conf.getLegend().setEnabled(false);
		conf.getChart().setBackgroundColor(new SolidColor(255,255,255,0));
		
		//XAxis
		XAxis xaxis = new XAxis();
		xaxis.setTitle("Humidity");
		//xaxis.setCategories(values[0][0]);
		conf.addxAxis(xaxis);
		
		//YAxis
		YAxis yaxis = new YAxis();
		yaxis.setTitle("Time");
		conf.addyAxis(yaxis);
		
		for (int i = 0; i < dataSeriesItems.size(); i++) {
			dataSeries.add(new DataSeriesItem((String)dataSeriesItems.get(i).get(2), (double) dataSeriesItems.get(i).get(3)));
		}
		
		conf.addSeries(dataSeries);
		
		return chart;
	}

	private void calulateHumidity(ArrayList<ArrayList<Object>> dataSeriesItems) {
		double temperature;
		double dewPoint;
		for (int i = 0; i < dataSeriesItems.size(); i++) {
			//100*(EXP((17.625*TD)/(243.04+TD))/EXP((17.625*T)/(243.04+T))) 
			temperature = (double) dataSeriesItems.get(i).get(0);
			dewPoint = (double) dataSeriesItems.get(i).get(1);
			dataSeriesItems.get(i).add(100*(Math.exp((17.625*dewPoint)/(243.04+dewPoint))/Math.exp((17.625*temperature)/(243.04+temperature))));
		}
		
	}

	private void addStations(GoogleMap googleMap, String country) {
		
		ArrayList<ArrayList<String>> stations = connection.getStationsInCountry(country);
		
		for (int i = 0; i < stations.size(); i++) {
			
			GoogleMapMarker marker = new GoogleMapMarker(stations.get(i).get(1), new LatLon((double) Double.parseDouble(stations.get(i).get(4)), (double) Double.parseDouble(stations.get(i).get(5))), false);
			marker.setAnimationEnabled(false);
			new GoogleMapInfoWindow(stations.get(i).get(0), marker);
			googleMap.addMarker(marker);
		}
		
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
