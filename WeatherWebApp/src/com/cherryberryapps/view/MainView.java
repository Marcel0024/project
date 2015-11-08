package com.cherryberryapps.view;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout implements View {

	ComponentContainer content;
	MenuView menuview;
	Dataset1View dataset1;
	Dataset2View dataset2;
	
	public MainView(){
		setSizeFull();
	    addStyleName("mainview");
	    menuview = new MenuView(this);
	    addComponent(menuview);
	    content = new CssLayout();
	    content.addStyleName("view-content");
	    content.setSizeFull();
	    addComponent(content);
	    setExpandRatio(content, 1);
	}

	public void goToSet2(){
		if(dataset2 == (null)){
			dataset2 = new Dataset2View();
			content.addComponent(dataset2);
			dataset2.setVisible(true);
		}
		if(dataset1 != null){
			dataset1.setVisible(false);
			//dataset3.setVisible(false);
		}
		dataset2.setVisible(true);
	}
	
	public void goToSet1(){
		if(dataset1 == (null)){
			dataset1 = new Dataset1View();
			content.addComponent(dataset1);
			dataset1.setVisible(true);
			
		}
		if(dataset2 != null){
			dataset2.setVisible(false);
		}
		dataset1.setVisible(true);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	

}
