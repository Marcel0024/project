package com.cherryberryapps.view;

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
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.AbstractComponent;
@SuppressWarnings({ "serial", "unused" })
public class Dataset1View extends HorizontalLayout implements View {
	
	private DBConnection connection;
	private Table table;
	private Chart chart;
	private Button button;
		
	public Dataset1View() {
		connection = new DBConnection();
    	String[][] values = connection.getDataset1();
		setSizeFull();
		//addComponent(buildToolbar());
		addComponent(buildTable(values));
		//addComponent(buildChart(values));
		addComponent(buildButton(values));
		 
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        
        Label title = new Label("Coldest 10 countries in Europe");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        
		return header;
		
	}

	private Table buildTable(String[][] values) {

		table = new Table("Coldest 10 countries in Europe");
		table.addContainerProperty("Country", String.class, null);
		table.addContainerProperty("Temperature",  Double.class, null);
		
		for (String[] value: values) {
			table.addItem(new Object[]{value[0], (double) Math.round(Double.parseDouble(value[1])* 100) / 100}, null);
		}
		
		table.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				String[][] data = connection.getDataset1Data(event.getItem().toString()); 
                
				Table table = new Table();
				table.addContainerProperty("Time", String.class, null);
				table.addContainerProperty("Temperature",  String.class, null);
				
				for (String[] value: data) {
					table.addItem(new Object[]{value[0], value[1]}, null);
				}
				
				VerticalLayout popupContent = new VerticalLayout();
				popupContent.addComponent(table);
				
				PopupView popup = new PopupView(null, popupContent);
				popup.setPopupVisible(true);
				popup.setSizeFull();
				popup.setHideOnMouseOut(false);
				addComponent(popup);
				
				
				
			}
		});

		 //table.setPageLength(table.size());
		return table;
	}
	
	private Component buildChart(String[][] values) {
		chart = new Chart(ChartType.COLUMN);
		chart.setWidth("400px");
		chart.setHeight("300px");
		DataSeries data = new DataSeries();
		
		//Configuration
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Coldest 10 countries in Europe");
		conf.setSubTitle("Wind chill corrected");
		conf.getLegend().setEnabled(false);
		
		//XAxis
		XAxis xaxis = new XAxis();
		xaxis.setTitle("Contry");
		conf.addxAxis(xaxis);
		
		//YAxis
		YAxis yaxis = new YAxis();
		yaxis.setTitle("Temperature");
		conf.addyAxis(yaxis);
		
		for (String[] value: values) {
			data.add(new DataSeriesItem(value[0], (double) Math.round(Double.parseDouble(value[1])* 100) / 100));
		}
		
		conf.addSeries(data);
		return chart;
	}

	private Component buildButton(String[][] values) {
		button = new Button("Download");
		button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				DateFormat df = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
				Date date = new Date();
				CsvWriter writer = new CsvWriter(System.getProperty("user.home") + "/Desktop", df.format(date));
				writer.write(values);
				for (String[] value: values) {
					System.out.println(value[0] + " " + value[1]);
				}
			}
		});
		return button;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
