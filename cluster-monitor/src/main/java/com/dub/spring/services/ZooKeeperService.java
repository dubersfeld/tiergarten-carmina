package com.dub.spring.services;


import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import com.dub.spring.cluster.Cluster;

public interface ZooKeeperService {
		
	public Cluster getCluster()
			throws KeeperException, InterruptedException;
	
	public void startContainer() 
			throws IOException, InterruptedException;
	
	public void stopContainer(String processId) 
			throws IOException, InterruptedException;
			
}
