package com.example.weatherserver;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class WeatherserverApplication extends Application {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		setMainWindow(new UserInterface());
	}

}
