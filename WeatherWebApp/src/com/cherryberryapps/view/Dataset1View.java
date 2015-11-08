package com.cherryberryapps.view;

import com.cherryberryapps.model.DBConnection;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Dataset1View extends HorizontalLayout implements View {
	
	private DBConnection connection;
	private Table table;
	private Chart chart;
		
	public Dataset1View() {
		connection = new DBConnection();
    	String[][] values = connection.getDataset1();
		setSizeFull();
		//addComponent(buildToolbar());
		addComponent(buildTable(values));
		addComponent(buildChart(values));
		 
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
		table.addContainerProperty("Temperature",  String.class, null);
		
		for (String[] value: values) {
			table.addItem(new Object[]{value[0],        value[1]}, null);
		}

		 //table.setPageLength(table.size());
		return table;
	}
	
	private Component buildChart(String[][] values) {
		chart = new Chart(ChartType.BAR);
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

	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
