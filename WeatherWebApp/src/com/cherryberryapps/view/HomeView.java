package com.cherryberryapps.view;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class HomeView extends HorizontalLayout {

	public HomeView(){
		setSizeFull();
	    addStyleName(ValoTheme.LAYOUT_WELL);
	    Label label = new Label("Home");
	    label.addStyleName(ValoTheme.LABEL_H2);
	    addComponent(label);
	}
	
}
