package com.arcao.menza.dto;

public class Building {
	private static Building[] buildings = new Building[] {
		new Building("Menza Bory", "Univerzitní 12"),
		new Building("Menza Kollárova", "Kollárova 19"),
		new Building("Bufet Lochotín", "Bolevecká 30"),
		new Building("Bufet FAV/FST", "Univerzitní 22"),
		new Building("Bufet PF", "Klatovská 51")
	};
	
	private final String name;
	private final String address;
	
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
	
	public static Building getBuilding(int id) {
		return buildings[id];
	}
	
	public static Building[] getBuildings() {
		return buildings;
	}
	
	public static String[] getBuildingNames() {
		String[] names = new String[buildings.length];
		for (int i=0; i < names.length; i++) {
			names[i] = buildings[i].getName();
		}
		
		return names;
	}
}
