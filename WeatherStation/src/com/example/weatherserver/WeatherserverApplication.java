package com.example.weatherserver;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class WeatherserverApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Weatherserver Application");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
