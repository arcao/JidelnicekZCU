package com.arcao.menza;

import java.io.Serializable;

public class Food implements Serializable {
	private static final long serialVersionUID = 8290201632931336294L;
	
	private String name;
	private String type;
	private int price;
	private float rating;
	
	public Food(String name, String type, int price, float rating) {
		this.name = name;
		this.type = type;
		this.price = price;
		this.rating = rating;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getPrice() {
		return price;
	}
	
	public float getRating() {
		return rating;
	}
}
