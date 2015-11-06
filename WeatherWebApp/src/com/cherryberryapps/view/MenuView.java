package com.cherryberryapps.view;

import java.io.File;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;

public class MenuView extends CustomComponent{

	private static final long serialVersionUID = 1L;
	String basepath;

	public MenuView(){
		setPrimaryStyleName("valo-menu");
        setSizeUndefined();
		
        basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		
		CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");
        
        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildLogo());
        menuContent.addComponent(buildMenuItems());
		
        setCompositionRoot(menuContent);
		
	}
	
    private Component buildTitle() {
        Label logo = new Label("Aruba Networks <strong>Dashboard</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }
    
    private Component buildLogo(){
		MenuBar logowrap = new MenuBar();
    	logowrap.addStyleName(ValoTheme.MENU_LOGO);
    	FileResource resource = new FileResource(new File(basepath + "/WEB-INF/lib/original.png"));
    	logowrap.addItem("",resource,null);
    	return logowrap;
    }
    
	private Component buildBadgeWrapper(Component menuItemButton) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        return dashboardWrapper;
    }
	
    private Component buildMenuItems(){
    	CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
      
        Component menuItemComponent = new ValoMenuItem("Dataset 1",1);
        menuItemComponent = buildBadgeWrapper(menuItemComponent);
    	menuItemsLayout.addComponent(menuItemComponent);
    	
    	Component menuItemComponent1 = new ValoMenuItem("Dataset 2",2);
        menuItemComponent1 = buildBadgeWrapper(menuItemComponent1);
    	menuItemsLayout.addComponent(menuItemComponent1);
    	
    	Component menuItemComponent2 = new ValoMenuItem("Dataset 3",3);
        menuItemComponent2 = buildBadgeWrapper(menuItemComponent2);
    	menuItemsLayout.addComponent(menuItemComponent2);
        
        return menuItemsLayout;
    	
    }
	
}
