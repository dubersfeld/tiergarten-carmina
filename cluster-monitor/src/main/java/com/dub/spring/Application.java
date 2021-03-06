package com.dub.spring;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import com.dub.spring.cluster.ClusterMonitor;
import com.dub.spring.stomp.MyHandler;
import com.dub.spring.stomp.StompClient;

/**
 * Note here that some beans depend on other beans
 * The bean construction order is mandatory 
 * Otherwise the application fails due to a NullPointerException
 * */

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Value("${membershipRoot}")	
	private String membershipRoot;
	
	
	public static void main(String[] args)  {
		SpringApplication.run(Application.class, args);	
	}
	
	@Bean("myHandler")
	public MyHandler myHandler() {
		return new MyHandler();
	}
	
	
	@Bean("stompClient")
	@DependsOn("myHandler")
	public StompClient stompClient() {
		StompClient stompClient = new StompClient(myHandler());	
		return stompClient;
	}
	
	
	@Bean
	@DependsOn({"myHandler","stompClient"})	
	public ClusterMonitor clusterMonitor() throws IOException {
		
		ClusterMonitor clusterMonitor = new ClusterMonitor(myHandler(), stompClient());
		return clusterMonitor;
	}


	@Override
	public void run(String... args) throws Exception {
		
		clusterMonitor().run();
		
	}
		
}