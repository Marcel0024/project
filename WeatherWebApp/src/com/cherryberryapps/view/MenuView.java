package com.cherryberryapps.view;

import java.io.File;

import com.vaadin.server.FileResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class MenuView extends CustomComponent{

	private static final long serialVersionUID = 1L;
	String basepath;
	MainView mainview;

	public MenuView(MainView mainview){
		this.mainview = mainview;
		setPrimaryStyleName("valo-menu");
        setSizeUndefined();
		Responsive.makeResponsive(this);
        basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		
		CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");
        
        Component companylogo = buildCompanyLogo();
        
        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildProfile());
        menuContent.addComponent(buildMenuItems());
        menuContent.addComponent(companylogo);
		
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
    
    private Component buildCompanyLogo(){
    	FileResource resource = new FileResource(new File(basepath + "/WEB-INF/lib/logo.png"));
    	Label icon = new Label();
    	icon.setIcon(resource);
		HorizontalLayout logowrap = new HorizontalLayout(icon);
		
    	return logowrap;
    }
    
    
    @SuppressWarnings("serial")
	private Component buildProfile(){
		MenuBar logowrap = new MenuBar();
    	logowrap.addStyleName("user-menu");
    	FileResource resource = new FileResource(new File(basepath + "/WEB-INF/lib/aruba-logo.jpg"));
    	MenuItem logoitem = logowrap.addItem("",resource,null);
    	MenuItem home = logoitem.addItem("Home",null,null);
    	MenuItem logout = logoitem.addItem("Logout",null,null);
    	
    	MenuBar.Command homecommand = new MenuBar.Command() {
    	    public void menuSelected(MenuItem selectedItem) {
    	    	mainview.goToHome();
    	}};
    	
    	MenuBar.Command logoutcommand = new MenuBar.Command() {
    	    public void menuSelected(MenuItem selectedItem) {
    	       UI.getCurrent().getNavigator().navigateTo("login");
    	}};
    	home.setCommand(homecommand);
    	logout.setCommand(logoutcommand);
    	
    	return logowrap;
    }
    
	private Component buildBadgeWrapper(Component menuItemButton) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        return dashboardWrapper;
    }
	
    @SuppressWarnings("serial")
	private Component buildMenuItems(){
    	CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
      
        Component menuItemComponent1 = new ValoMenuItem("Dataset 1",1);
        menuItemComponent1.addListener(new Listener() {
			
			@Override
			public void componentEvent(Event event) {
				if (event.getClass() == Button.ClickEvent.class) {
					mainview.goToSet1();
				}
			}
		});
        menuItemComponent1 = buildBadgeWrapper(menuItemComponent1);
    	menuItemsLayout.addComponent(menuItemComponent1);
    	
    	Component menuItemComponent2 = new ValoMenuItem("Dataset 2",2);
    	menuItemComponent2.addListener(new Listener() {
			
			@Override
			public void componentEvent(Event event) {
				if (event.getClass() == Button.ClickEvent.class) {
					mainview.goToSet2();
				}
			}
		});
        menuItemComponent2 = buildBadgeWrapper(menuItemComponent2);
    	menuItemsLayout.addComponent(menuItemComponent2);
    	
    	Component menuItemComponent3 = new ValoMenuItem("Dataset 3",3);
    	menuItemComponent3.addListener(new Listener() {
			
			@Override
			public void componentEvent(Event event) {
				if (event.getClass() == Button.ClickEvent.class) {
					mainview.goToSet3();
				}
			}
		});
        menuItemComponent3 = buildBadgeWrapper(menuItemComponent3);
    	menuItemsLayout.addComponent(menuItemComponent3);
        
        return menuItemsLayout;
    	
    }
	
}
