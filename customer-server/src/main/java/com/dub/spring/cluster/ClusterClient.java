package com.dub.spring.cluster;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ClusterClient implements Watcher, Runnable {

	private static String membershipRoot = "/Members";
	private ZooKeeper zk;
	
	public ClusterClient(String hostPort, String container) {
		
		try {	
			zk = new ZooKeeper(hostPort, 2000, this);
		} catch(IOException e) {
			e.printStackTrace();
		} 
		
		if (zk != null) {
			try {
				// create zkNode /Members if does not exists
				if (zk.exists(membershipRoot, false) == null) {
					zk.create(membershipRoot, "".getBytes(), 
									Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				zk.create(membershipRoot + '/' + container, container.getBytes(),
						Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
				System.out.println("Sator exception " + e);
			}
		}
	}
	
	public synchronized void close() {
		try {
			zk.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			synchronized(this) {
				while (true) {
					wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();	
		} finally {
			this.close();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.printf("\nEventReceived: %s", event.toString());
	}

}
