package com.dub.spring.watchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dub.spring.client.MyHandler;
import com.dub.spring.client.StompClient;
import com.dub.spring.cluster.Cluster;
import com.dub.spring.services.StompService;
import com.dub.spring.services.ZooKeeperService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ChildrenWatcher implements Watcher {
	
	@Value("${membershipRoot}")	
	private String membershipRoot;
	
	boolean alive = true;
	
	@Autowired
	private ZooKeeper zooKeeper;
	
	@Autowired
	private ZooKeeperService zooKeeperService;
	
	@Autowired
	private StompService stompService;
	
	
	@Override
	public void process(WatchedEvent event) {
			
		if (event.getType() == Event.EventType.NodeChildrenChanged) {
			try {
				// Get current list of child znode
				// reset the watch
						
				List<String> children = zooKeeper.getChildren(membershipRoot, this);
				wall("!!!Cluster Membership Change!!!");
				wall("Members: " + children);
				System.out.println();
					
				Set<String> containers = new HashSet<>();
				
				for (String child : children) {
					String item = membershipRoot + "/" + child;
					System.out.println("child " + item);
					byte[] zoo_data = zooKeeper.getData(item, this, null);
					String data = new String(zoo_data);
					containers.add(data);
					
				}
								
				Cluster cluster = zooKeeperService.getCluster();
				
				cluster.display();
				
				try {
					// publish here
					stompService.publishCluster();
				} catch (JsonProcessingException e) {				
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (KeeperException e) {
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				alive = false;
				throw new RuntimeException(e);
			} 
		}
	}// process


	public String getMembershipRoot() {
		return membershipRoot;
	}


	public void setMembershipRoot(String membershipRoot) {
		this.membershipRoot = membershipRoot;
	}


	//helper method
	public void wall(String message) {
		System.out.printf("\nMESSAGE: %s", message);
	}

}
