package com.dub.spring.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dub.spring.services.ContainerManagerService;

@RestController
public class ZooKeeperEndpoint {
		
	@Autowired
	private ContainerManagerService zooKeeperService;


	@RequestMapping(value = "/startContainer",
			method = RequestMethod.POST)
	public String startContainer() {
				
		try {
			zooKeeperService.startContainer();
			return "STARTED";
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "ERROR";
		}
		
	}
	
	@RequestMapping(value = "/stopContainer",
			method = RequestMethod.POST)
	public String stopContainer(@RequestBody ContainerForm containerForm) {
				
		try {
			zooKeeperService.stopContainer(containerForm.getContainerId());
			return "STOPPED";
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
}
