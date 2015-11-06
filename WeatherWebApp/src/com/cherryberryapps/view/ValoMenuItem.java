package com.cherryberryapps.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

public class ValoMenuItem extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String STYLE_SELECTED = "selected";


    public ValoMenuItem(String caption,int menuIndex) {
        setPrimaryStyleName("valo-menu-item");
        setCaption(caption);
        addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(final ClickEvent event) {
				Notification.show("Welcome to the Animal Farm");
				switch(menuIndex){
				case 1:
					
					break;
				case 2:
					
					break;
				
				}
				
            }
        });
    }}
