package com.dub.spring.services;


import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;

import com.dub.spring.cluster.Cluster;

public interface ContainerManagerService {
		
	//public Cluster getCluster()
	//		throws KeeperException, InterruptedException;
	
	public void startContainer() 
			throws IOException, InterruptedException;
	
	public void stopContainer(String processId) 
			throws IOException, InterruptedException;
			
}
