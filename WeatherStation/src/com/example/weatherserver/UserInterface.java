package com.example.weatherserver;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;

public class UserInterface extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserInterface(){
		this.setName("WeatherServer Application");
		makeMenubar();
		Label label = new Label("Hello Vaadin user");
		Label label1 = new Label("Whatupp");
		addComponent(label);
		addComponent(label1);
		
	}

	private void makeMenubar(){
		TabSheet tabsheet = new TabSheet();
		addComponent(tabsheet);

		// Create the first tab
		VerticalLayout tab1 = new VerticalLayout();
		tab1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tab1, "Dataset 1",null);

		// This tab gets its caption from the component caption
		VerticalLayout tab2 = new VerticalLayout();
		tab1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tab2, "Dataset 2",null);
		
		VerticalLayout tab3 = new VerticalLayout();
		tab1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tab3, "Dataset 3",null);
		
		VerticalLayout tab4 = new VerticalLayout();
		tab1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tab4, "Dataset 34",null);
		
	}
	
}
