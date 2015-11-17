package com.cherryberryapps.view;

import java.io.File;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HomeView extends VerticalLayout {
	
	private MainView mainview;
	private String user = "";
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	public HomeView(MainView mainview){
		this.mainview = mainview;
		setSizeFull();
	    
	    addComponent(buildHeader());
	    
	    Component body = buildBody();
		addComponent(body);
        setExpandRatio(body, 1);
        try {
        	if (getSession().getAttribute("user") != null) {
        		user = (String) getSession().getAttribute("user").toString();
        	}
        } catch (NullPointerException e) {
        	
        }
	}
	
	private Component buildHeader() {
		
		VerticalLayout header = new VerticalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        header.setMargin(true);
        Responsive.makeResponsive(header);
        
        Label title = new Label("Welcome " + user );
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
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
        
        Panel panelDataset1 = new Panel(); 
        VerticalLayout layoutDataset1 = new VerticalLayout();
        Label labelDataset1 = new Label("10 coldest countries in Europe");
        labelDataset1.setSizeUndefined();
        labelDataset1.addStyleName(ValoTheme.LABEL_H2);
        labelDataset1.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        FileResource resourceDataset1 = new FileResource(new File(basepath + "/WEB-INF/lib/Dataset1.png"));
        Image imageDataset1 = new Image(null, resourceDataset1 );
        layoutDataset1.addComponent(labelDataset1);
        layoutDataset1.addComponent(imageDataset1);
        layoutDataset1.setSpacing(true);
        panelDataset1.setContent(layoutDataset1);
        panelDataset1.addClickListener(new ClickListener() {
			
			@Override
			public void click(ClickEvent event) {
				mainview.goToSet1();
			}
		});
        
        
        Panel panelDataset2 = new Panel(); 
        VerticalLayout layoutDataset2 = new VerticalLayout();
        Label labelDataset2 = new Label("5 countries in Europe with the highest rainfall");
        labelDataset2.setSizeUndefined();
        labelDataset2.addStyleName(ValoTheme.LABEL_H2);
        labelDataset2.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        FileResource resourceDataset2 = new FileResource(new File(basepath + "/WEB-INF/lib/Dataset2.png"));
        Image imageDataset2 = new Image(null, resourceDataset2 );
        layoutDataset2.addComponent(labelDataset2);
        layoutDataset2.addComponent(imageDataset2);
        layoutDataset2.setSpacing(true);
        panelDataset2.setContent(layoutDataset2);
        panelDataset2.addClickListener(new ClickListener() {
			
			@Override
			public void click(ClickEvent event) {
				mainview.goToSet2();
			}
		});
        
        Panel panelDataset3 = new Panel(); 
        VerticalLayout layoutDataset3 = new VerticalLayout();
        Label labelDataset3 = new Label("Humidity of European countries");
        labelDataset3.setSizeUndefined();
        labelDataset3.addStyleName(ValoTheme.LABEL_H2);
        labelDataset3.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        FileResource resourceDataset3 = new FileResource(new File(basepath + "/WEB-INF/lib/Dataset3.png"));
        Image imageDataset3 = new Image(null, resourceDataset3 );
        layoutDataset3.addComponent(labelDataset3);
        layoutDataset3.addComponent(imageDataset3);
        layoutDataset3.setSpacing(true);
        panelDataset3.setContent(layoutDataset3);
        panelDataset3.addClickListener(new ClickListener() {
			
			@Override
			public void click(ClickEvent event) {
				mainview.goToSet3();
			}
		});
        
        body.addComponent(panelDataset1);
        body.addComponent(panelDataset2);
        body.addComponent(panelDataset3);
        
        
        
		return body;
	}
}
