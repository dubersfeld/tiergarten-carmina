package com.dub.spring.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.dub.spring.cluster.Cluster;


@Controller
public class StompController {

	 
    /**
     * This method is required to push a Cluster object to the browser
     * */ 
    @MessageMapping("/notify")
    @SendTo("/topic/cluster")
    public Cluster notify(Cluster cluster) throws Exception {
     
        return cluster;   
    }
}
