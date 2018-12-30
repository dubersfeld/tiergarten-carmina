package com.dub.spring.listeners;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.dub.spring.client.StompClient;

@Component
public class StompConnectEventListener implements ApplicationListener<SessionConnectEvent> {
		
	@Autowired
	private StompClient stompClient;
			
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
				
		System.out.println("onApplicationEvent begin "
				+ event.getClass());
		
		System.out.println("onApplicationEvent stompSession == null "
				+ stompClient.getStompSession() == null);
		
		
		if (stompClient.getStompSession() == null) {
			
			try {
				stompClient.setStompSession(stompClient.connect().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}					
		}
	}
}
