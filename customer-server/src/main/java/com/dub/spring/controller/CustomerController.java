package com.dub.spring.controller;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dub.spring.domain.Customer;


@RestController
public class CustomerController {
	
	@Autowired
	private Environment environment;
	
	List<Customer> customers = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
		customers.add(new Customer("Donald", "Duck")); 
		customers.add(new Customer("Mickey", "Mouse")); 
		customers.add(new Customer("Roger", "Rabbit")); 
	}
	
	@RequestMapping("/allCustomers")
	public List<Customer> allCustomers() {
		return customers;
	}
	

	@RequestMapping("/allCustomersWithContainer")
	public CustomersWithContainer allCustomersWithContainerId() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		int index = name.indexOf('@');
		String container = name.substring(index + 1);
		return new CustomersWithContainer(customers, container);
	}
	

	private static class CustomersWithContainer {
		
		List<Customer> customers;
		String container;
		
		public CustomersWithContainer(List<Customer> customers, String container) {
			this.customers = customers;
			this.container = container;
		}
		
		public List<Customer> getCustomers() {
			return customers;
		}
		public void setCustomers(List<Customer> customers) {
			this.customers = customers;
		}

		public String getContainer() {
			return container;
		}

		public void setContainer(String container) {
			this.container = container;
		}		
	}
	
}