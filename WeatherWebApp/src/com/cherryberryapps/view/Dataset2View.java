package com.cherryberryapps.view;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Dataset2View extends VerticalLayout{

	private DBConnection connection;
	private UI window;
	public Dataset2View(UI window){
		this.window = window;
		connection = new DBConnection();
		String[] values = connection.getDataset2();
		 addStyleName(ValoTheme.PANEL_BORDERLESS);
	     setSizeFull();
	      
	     setSizeFull();
	     setMargin(true);
	     addStyleName("dashboard-view");
	     Responsive.makeResponsive(this);
		 addComponent(buildHeader());
		 
		 Component body = buildBody(values);
		 addComponent(body);
	     setExpandRatio(body, 1);
	     
	     addComponent(buildFooter(values));
	     
	}
	
	private Component buildHeader() {
		VerticalLayout header = new VerticalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        header.setMargin(true);
        Responsive.makeResponsive(header);
        
        Label title = new Label("Top 5 countries with the highest rainfall");
        
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        Label description = new Label("A list of top 5 countries with the highest rainfall. This is a weekly average");
        description.setSizeUndefined();
        description.addStyleName(ValoTheme.LABEL_H3);
        description.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        header.addComponent(title);
        header.addComponent(description);
        
		return header;
		
	}
	
	private Component buildBody(String[] values) {
		
		HorizontalLayout body = new HorizontalLayout();
		body.setWidth("100%");
        body.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        body.setSpacing(true);
        body.setMargin(true);
        Responsive.makeResponsive(body);
        
        Component table = buildTable(values);
        body.addComponent(table);
        body.setExpandRatio(table, 1);
        
        Component chart = buildChart(values);
        body.addComponent(chart);
        body.setExpandRatio(chart, 1);
		
		return body;
	}
	
	private Component buildTable(String[] values) {
		Table table = new Table();
		table.addContainerProperty("Country", String.class, null);
		table.addContainerProperty("Rainfall in centimeters",  Double.class, null);
		table.setPageLength(table.size());
		table.setStyleName(ValoTheme.TABLE_BORDERLESS);
		table.setStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.setStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		String[] countrys = new String[10];
		
		int x = 0;
		for (int i = 0; i < 10; i++) {
			x = (i+1);
			table.addItem(new Object[]{values[x], (double) Math.round(Double.parseDouble(values[i])* 100) / 100}, null);
			countrys[i] = values[x];
			i++;
		}
		countrys[0] = values[1];
		countrys[1] = values[3];
		countrys[2] = values[5];
		countrys[3] = values[7];
		countrys[4] = values[9];
		
		table.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				Window subWindow = new Window();
				subWindow.center();
				subWindow.setSizeUndefined();
				subWindow.setResizable(false);
				subWindow.setResponsive(true);
				
				VerticalLayout subContent = new VerticalLayout();
		        subContent.setMargin(true);
		        
		        subContent.addComponent(buildSubTable(countrys[((int) event.getItemId()) -1]));
		        //event.getItem().toString())
		        subWindow.setContent(subContent);
		        
		        window.addWindow(subWindow);
			}
		});
		
		return table;
	}
	
	private Component buildSubTable(String country) {
		String[] data = connection.getDataset2data(country); 
		
		Table table = new Table();
		table.addContainerProperty("Time", String.class, null);
		table.addContainerProperty("Rainfall in Cm",  Double.class, null);
		table.setPageLength(table.size());
		table.setStyleName(ValoTheme.TABLE_BORDERLESS);
		table.setStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.setStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		
		for (int i = 0; i < 7;i++) {
			table.addItem(new Object[]{i+1 + " Hours ago", (double) Double.parseDouble(data[i])}, null);
		}
		
		return table;
	}
	
	private Component buildChart(String[] values) {
		Chart chart = new Chart(ChartType.COLUMN);
		//chart.setSizeFull();
		chart.setWidth("100%");
		chart.setHeight("200%");
		
		DataSeries data = new DataSeries();
		
		//Configuration
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Top 5 countries with highest rainfall");
		conf.getLegend().setEnabled(false);
		conf.getChart().setBackgroundColor(new SolidColor(255,255,255,0));
		
		//XAxis
		XAxis xaxis = new XAxis();
		xaxis.setTitle("Country");
		xaxis.setCategories(values[1]);
		conf.addxAxis(xaxis);
		
		//YAxis
		YAxis yaxis = new YAxis();
		yaxis.setTitle("Rainfall in centimeters");
		conf.addyAxis(yaxis);
		int x = 0;
		for (int i = 0; i < 10;i++) {
			x = (i+1);
			data.add(new DataSeriesItem(values[x], (double) Math.round(Double.parseDouble(values[i])* 100) / 100));
			i++;
		}
		
		conf.addSeries(data);
		return chart;
	}
	
	private Component buildFooter(String[] values) {
		HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        footer.setSpacing(true);
        footer.setMargin(true);
        Responsive.makeResponsive(footer);
        
        footer.addComponent(buildDownloadButton(values));
        
		return footer;
	}
	
	private Component buildDownloadButton(String[] values) {
		DateFormat dateFormatLong = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		DateFormat dateFormatShort = new SimpleDateFormat("dd_MM_yyy");
		Date date = new Date();
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "\\WEB-INF\\docs\\Dataset2" + dateFormatShort.format(date) +"\\";
		String file = dateFormatLong.format(date) + ".csv";
		
		CsvWriter writer = new CsvWriter(basepath , file, "Country", "Rainfall in centimeters");
		String[] entries = new String[2];
		
		int c = 0;
		for (int i = 0; i < 5; i++) {
			entries[1] = values[c];
			c++;
			entries[0] = values[c];
			c++;
			writer.write(entries);
		}		
		
		Button downloadButton = new Button("Download measurements");
		Resource res = new FileResource(new File(basepath + file));
		FileDownloader fd = new FileDownloader(res);
		fd.extend(downloadButton);
		
		return downloadButton;
	}
}
