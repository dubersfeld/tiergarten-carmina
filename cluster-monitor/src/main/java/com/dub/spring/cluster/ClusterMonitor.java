package com.dub.spring.cluster;


import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;

import com.dub.spring.stomp.MyHandler;
import com.dub.spring.stomp.StompClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ClusterMonitor implements Runnable {
	
	@Value("${membershipRoot}")	
	private String membershipRoot;

	ObjectMapper mapper = new ObjectMapper();
	
	boolean alive = true;
	
	private ZooKeeper zooKeeper;
	
	List<String> containerIds;
	
	private ZooKeeper zk;
	
	private final Watcher connectionWatcher;
	private final Watcher clusterWatcher;
	
	Cluster cluster;
	
		
	// main constructor		
	public ClusterMonitor(MyHandler myHandler, StompClient stompClient) throws IOException {
		
		// initial watcher
		connectionWatcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				if (event.getType() == Watcher.Event.EventType.None
						&&
					event.getState() == Watcher.Event.KeeperState.SyncConnected) {
					System.out.println("\nEvent Received: "
											+ event.toString());
				
					try {
						
						containerIds = zk.getChildren(membershipRoot, clusterWatcher);
						
						cluster = readCluster();
						
						// here cluster is ready for Stomp display
					
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
						
				}
			}
		};
		
		// main watcher
		clusterWatcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				
				System.out.println("\nEvent Received: "
						+ event.toString());

				if (event.getType() == Event.EventType.NodeChildrenChanged) {
					
					try {
						containerIds = zk.getChildren(membershipRoot, clusterWatcher);
						cluster = readCluster();
						
						// display 
						if (stompClient.getStompSession() != null) {
							myHandler.sendCluster(stompClient.getStompSession());		
						}
										
					} catch (KeeperException e) {
			
						e.printStackTrace();
					} catch (InterruptedException e) {
			
						e.printStackTrace();
					} catch (JsonProcessingException e) {
	
						e.printStackTrace();
					}
					
					
				
				}
					
			}
			
		};
	
		// Note that the port is not the default port 2181
		zk = new ZooKeeper("localhost:2182", 2000, connectionWatcher);	
			
	}// constructor

	public synchronized void close() {
		try {
			zooKeeper.close();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			synchronized(this) {
				while (alive) {
					wait();
				}
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			this.close();
		}
	}
	
	private Cluster readCluster() throws KeeperException, InterruptedException, JsonProcessingException {
		
		Cluster cluster = new Cluster();
			
		for (String child : containerIds) {
			String item = membershipRoot + "/" + child;
			System.out.println("child " + item);
			byte[] zoo_data = zk.getData(item, clusterWatcher, null);
			String data = new String(zoo_data);
			cluster.getContainers().add(data);
		}
			
		return cluster;
	}

	public Cluster getCluster() {
		return cluster;
	}

}