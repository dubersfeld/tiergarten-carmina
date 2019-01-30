package com.dub.spring.cluster;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

	private Set<String> containers = new HashSet<>();

	public Cluster() {
	}
	
	public Cluster(Set<String> containers) {
		this.containers = containers;
	}
	
	public Set<String> getContainers() {
		return containers;
	}

	public void setContainers(Set<String> containers) {
		this.containers = containers;
	}
	
}
