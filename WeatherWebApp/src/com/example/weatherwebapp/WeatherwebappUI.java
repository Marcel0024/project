package com.example.weatherwebapp;

import javax.servlet.annotation.WebServlet;

import com.cherryberryapps.view.LoginView;
import com.cherryberryapps.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("weatherwebapp")
public class WeatherwebappUI extends UI {

	Navigator navigator;
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = WeatherwebappUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		setTheme("valo");
		getPage().setTitle("Aruba Networks");
		navigator = new Navigator(this,this);
		navigator.addView("login", new LoginView());
		navigator.addView("main", new MainView());
		navigator.navigateTo("main");		
	}	
}