package com.versacomllc.audit.data;

import com.versacomllc.audit.model.Customer;


public class LocalCustomer extends Customer{

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalCustomer(Customer customer) {
		this.setName(customer.getName());
		this.setRid(customer.getRid());
	}
	public LocalCustomer() {
	}
	
}
