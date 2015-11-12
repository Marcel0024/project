package com.cherryberryapps.view;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout implements View {

	UI window;
	ComponentContainer content;
	MenuView menuview;
	Dataset1View dataset1;
	Dataset2View dataset2;
	Dataset3View dataset3;
	HomeView homeview;
	
	public MainView(UI window){
		this.window = window;
		setSizeFull();
	    addStyleName("mainview");
	    menuview = new MenuView(this);
	    homeview = new HomeView();
	    addComponent(menuview);
	    content = new CssLayout();
	    content.addStyleName("view-content");
	    content.setSizeFull();
	    content.addComponent(homeview);
	    addComponent(content);
	    setExpandRatio(content, 1);
	}

	public void goToSet1(){
		if(dataset1 == (null)){
			dataset1 = new Dataset1View(window);
			content.addComponent(dataset1);
			homeview.setVisible(false);
			dataset1.setVisible(true);
		}
		if(dataset2 != null){
			dataset2.setVisible(false);
		}
		if(dataset3 != null){
			dataset3.setVisible(false);
		}
		homeview.setVisible(false);
		dataset1.setVisible(true);
	}
	
	public void goToSet2(){
		if(dataset2 == (null)){
			dataset2 = new Dataset2View();
			content.addComponent(dataset2);
			homeview.setVisible(false);
			dataset2.setVisible(true);
		}
		if(dataset1 != null){
			dataset1.setVisible(false);
		}
		if(dataset3 != null){
			dataset3.setVisible(false);
		}
		homeview.setVisible(false);
		dataset2.setVisible(true);
	}
	
	public void goToSet3(){
		if(dataset3 == (null)){
			dataset3 = new Dataset3View(window);
			content.addComponent(dataset3);
			homeview.setVisible(false);
			dataset3.setVisible(true);
		}
		if(dataset1 != null){
			dataset1.setVisible(false);
		}
		if(dataset2 != null){
			dataset2.setVisible(false);
		}
		homeview.setVisible(false);
		dataset3.setVisible(true);
	}
	
	public void goToHome(){
		if(dataset1 != null){
			dataset1.setVisible(false);
		}
		if(dataset2 != null){
			dataset2.setVisible(false);
		}
		if(dataset3 != null){
			dataset3.setVisible(false);
		}
		homeview.setVisible(true);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	

}
