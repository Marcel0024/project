package com.cherryberryapps.view;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout implements View {

	public MainView(){
		MenuView menuview = new MenuView();
		Dataset2View dataset = new Dataset2View();

		setSizeFull();
	    addStyleName("mainview");
	    addComponent(menuview);
	    ComponentContainer content = new CssLayout();
	    content.addStyleName("view-content");
	    content.setSizeFull();
	    content.addComponent(dataset);
	    addComponent(content);
	    setExpandRatio(content, 1);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	

}
