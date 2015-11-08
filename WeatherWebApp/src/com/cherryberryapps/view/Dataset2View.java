package com.cherryberryapps.view;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Dataset2View extends Panel{

	public Dataset2View(){
		
		 addStyleName(ValoTheme.PANEL_BORDERLESS);
	     setSizeFull();
		 setWidth("100%");
	      
	     VerticalLayout root = new VerticalLayout();
	     root.setSizeFull();
	     root.setMargin(true);
	     root.addStyleName("dashboard-view");
	     setContent(root);
	     Responsive.makeResponsive(root);
	     
	     Label label = new Label("DashBoard");
		 label.setStyleName(ValoTheme.LABEL_H2);
		 
		 root.addComponent(label);
	}
	
}
