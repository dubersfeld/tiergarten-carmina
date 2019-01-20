package com.dub.spring.services;

import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface StompService {
	
	public void publishCluster() throws InterruptedException, ExecutionException, JsonProcessingException, KeeperException; 
	
	
}
