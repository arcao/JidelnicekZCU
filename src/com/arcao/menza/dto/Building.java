package com.arcao.menza.dto;

public class Building {
	private String name;
	private String address;
	
	public Building(String name, String address) {
		this.name = name;
		this.address = address;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
}
