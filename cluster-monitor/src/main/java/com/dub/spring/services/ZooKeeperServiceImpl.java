package com.dub.spring.services;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dub.spring.cluster.Cluster;
import com.dub.spring.utils.StreamGobbler;


public class ZooKeeperServiceImpl implements ZooKeeperService {
	
	@Value("${membershipRoot}")	
	private String membershipRoot;
	
	@Value("${clientUrl}")	
	private String clientUrl;
	
	@Value("${password}")	
	private String password;
	
	@Value("${launchCommand}")	
	private String launchCommand;
	
	@Value("${stopCommandBase}")	
	private String stopCommandBase;
	
	@Autowired 
	private ZooKeeper zooKeeper;
	
	
	@Override
	public void startContainer() {
	
		ProcessBuilder builder = new ProcessBuilder();
    	builder.command("bash", "-c", "cd " + clientUrl + " ; pwd ; ls ; " + launchCommand); 

		Process process;
		
		try {
			process = builder.start();
			OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
			
			StreamGobbler streamGobbler = 
	    		  new StreamGobbler(process.getInputStream(), System.out::println);    		
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			 output.write(password + "\n");
             output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	@Override
	public Cluster getCluster() throws KeeperException, InterruptedException {
	
		// no change here
		List<String> children = zooKeeper.getChildren(membershipRoot, false);
			
		Set<String> containers = new HashSet<String>();
		
		for (String child : children) {
			String item = membershipRoot + "/" + child;
			System.out.println(item);
			byte[] zoo_data = zooKeeper.getData(item, null, null);
			String data = new String(zoo_data);			
			containers.add(data);
		}
				
		return new Cluster(containers);
	}

	@Override
	public void stopContainer(String containerId) throws IOException, InterruptedException {
		
		String stopCommand = stopCommandBase + containerId;
			
		ProcessBuilder builder = new ProcessBuilder();
    	builder.command("bash", "-c", "cd " + clientUrl + " ; pwd ; ls ; " + stopCommand); 

		Process process;
		
		try {
			process = builder.start();
			OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
			
			StreamGobbler streamGobbler = 
	    		  new StreamGobbler(process.getInputStream(), System.out::println);    		
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			 output.write(password + "\n");
             output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
