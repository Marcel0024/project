package com.cherryberryapps.view;

import com.cherryberryapps.model.DBConnection;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

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

	private Component buildFooter() {
		VerticalLayout header = new VerticalLayout();
		
		return header;
	}

	private Component buildBody() {
		HorizontalLayout body = new HorizontalLayout();
		
		return body;
	}

	private Component buildHeader() {
		HorizontalLayout footer = new HorizontalLayout();
		
		return footer;
	}

}
