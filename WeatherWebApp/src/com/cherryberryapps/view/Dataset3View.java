package com.cherryberryapps.view;

import com.cherryberryapps.model.DBConnection;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Dataset3View extends VerticalLayout {
	
	private DBConnection connection;
	
	public Dataset3View() {
		connection = new DBConnection();
    	String[][] values = connection.getDataset1();
		setSizeFull();
		addComponent(buildHeader());
		
		Component body = buildBody();
		addComponent(body);
        setExpandRatio(body, 1);
        
        addComponent(buildFooter());
		
	}

	private Component buildFooter() {
		
		return null;
	}

	private Component buildBody() {
		
		return null;
	}

	private Component buildHeader() {
		
		return null;
	}

}
