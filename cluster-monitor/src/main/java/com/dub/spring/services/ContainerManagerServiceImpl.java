package com.dub.spring.services;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dub.spring.utils.StreamGobbler;

@Service
public class ContainerManagerServiceImpl implements ContainerManagerService {
		
	@Value("${clientUrl}")	
	private String clientUrl;
	
	@Value("${password}")	
	private String password;
	
	@Value("${launchCommand}")	
	private String launchCommand;
	
	@Value("${stopCommandBase}")	
	private String stopCommandBase;
	

	
	
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
