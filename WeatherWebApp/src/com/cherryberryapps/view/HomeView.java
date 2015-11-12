package com.cherryberryapps.view;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HomeView extends HorizontalLayout {

	public HomeView(){
		setSizeFull();
	    Label label = new Label("Home");
	    label.addStyleName(ValoTheme.LABEL_H2);
	    addComponent(label);
	    
	    addComponent(buildHeader());
	    
	    Component body = buildBody();
		addComponent(body);
        setExpandRatio(body, 1);
	}
	
	private Component buildHeader() {
		
		VerticalLayout header = new VerticalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        header.setMargin(true);
        Responsive.makeResponsive(header);
        
        Label title = new Label("Welcome to Aruba Networks' weather application");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        Label description = new Label("To view a dataset, simply click on one of the previews below or on of the buttons on the left.");
        description.setSizeUndefined();
        description.addStyleName(ValoTheme.LABEL_H3);
        description.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        header.addComponent(title);
        
		return header;
	}	
	
	private Component buildBody() {

		HorizontalLayout body = new HorizontalLayout();
		body.setWidth("100%");
        body.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        body.setSpacing(true);
        body.setMargin(true);
        Responsive.makeResponsive(body);
        
        
        
		return body;
	}
}
