package com.amor.bow;

import com.amor.bow.listener.ApplicationStartOverListener;
import com.amor.bow.listener.PropertiesModifiedListener;
import com.amor.common.listener.event.ApplicationStartOverEvent;
import com.amor.common.listener.event.EventPublisher;

public class BowApplicationLaucher {

	public static void main(String[] args) {
		registerListener();
		EventPublisher.postEvent(new ApplicationStartOverEvent());
	}


	private static void registerListener(){
		EventPublisher.register(new ApplicationStartOverListener());
		EventPublisher.register(new PropertiesModifiedListener());
	}
}
