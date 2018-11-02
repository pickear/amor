package com.amor.arrow;

import com.amor.arrow.listener.ApplicationStartOverListener;
import com.amor.core.listener.event.ApplicationStartOverEvent;
import com.amor.core.listener.event.EventPublisher;

public class ArrowApplicationLaucher {

	public static void main(String[] args) {
		registerListener();
		EventPublisher.postEvent(new ApplicationStartOverEvent());
	}


	private static void registerListener(){
		EventPublisher.register(new ApplicationStartOverListener());
	}
}
