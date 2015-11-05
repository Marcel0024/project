package com.cherryberryapps.webapplication;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class WeatherWebApplication extends Application {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		setMainWindow(new UserInterface());
	}

}
