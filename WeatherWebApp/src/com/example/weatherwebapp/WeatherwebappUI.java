package com.example.weatherwebapp;

import javax.servlet.annotation.WebServlet;

import com.cherryberryapps.model.WeatherServer;
import com.cherryberryapps.view.LoginView;
import com.cherryberryapps.view.MainView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("weatherwebapp")
@Push
public class WeatherwebappUI extends UI {
	
	Navigator navigator;
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = WeatherwebappUI.class, widgetset = "com.example.weatherwebapp.widgetset.WeatherwebappWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		new WeatherServer();
		setTheme("valo");
		getPage().setTitle("Aruba Networks");
		addStyleName(ValoTheme.UI_WITH_MENU);
		MainView mainview = new MainView(this);
		navigator = new Navigator(this,this);
		navigator.addView("login", new LoginView());
		navigator.addView("main", mainview);
		navigator.navigateTo("login");		
	}	
}