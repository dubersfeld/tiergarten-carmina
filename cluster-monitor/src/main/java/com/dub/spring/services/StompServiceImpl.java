package com.dub.spring.services;

import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.spring.client.MyHandler;
import com.dub.spring.client.StompClient;
import com.fasterxml.jackson.core.JsonProcessingException;


@Service
public class StompServiceImpl implements StompService {
	
	@Autowired
	private StompClient stompClient;
	
	@Autowired
	private MyHandler myHandler;
	
	
	@Override
	public void publishCluster() throws InterruptedException, ExecutionException, JsonProcessingException, KeeperException {
		myHandler.sendCluster(stompClient.getStompSession());
	}
}
