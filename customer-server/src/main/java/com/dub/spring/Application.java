package com.dub.spring;

import java.lang.management.ManagementFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.dub.spring.cluster.ClusterClient;


@SpringBootApplication
@EnableDiscoveryClient
public class Application implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		String hostPort = "zookeeper-server:2181";
		String name = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println("Arepo name " + name);
		
		int index = name.indexOf('@');
		String container = name.substring(index + 1);
	
		ClusterClient clusterClient = new ClusterClient(hostPort, container);
		clusterClient.run();
	}
}
