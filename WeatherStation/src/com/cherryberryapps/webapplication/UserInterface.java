package com.cherryberryapps.webapplication;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;

public class UserInterface extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserInterface(){
		this.setName("Aruba Networks: Weather Web Application");
		makeMenubar();
		Label label = new Label("Hello Vaadin user");
		addComponent(label);
		//setVisible(false);
		
	}

	private void makeMenubar(){
		TabSheet tabsheet = new TabSheet();
		addComponent(tabsheet);

		// Create the first tab
		VerticalLayout tabDataset1 = new VerticalLayout();
		tabDataset1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tabDataset1, "10 coldest country",null);

		// This tab gets its caption from the component caption
		VerticalLayout tabDataset2 = new VerticalLayout();
		tabDataset1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tabDataset2, "Dataset 2",null);
		
		VerticalLayout tabDataset3 = new VerticalLayout();
		tabDataset1.addComponent(new Embedded(null,null));
		tabsheet.addTab(tabDataset3, "Dataset 3",null);
		
		
		
	}
	
}
