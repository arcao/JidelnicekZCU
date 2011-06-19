package com.arcao.menza.dto;

public class Building {
	private static Building[] buildings = new Building[] {
		new Building("Menza Bory", "Univerzitní 12", "Obědy:\n10.30 - 15.00\n\nVečeře:\n16.30 - 18.30\n(V době prázdnin a zkouškovém období nepodáváme)"),
		new Building("Menza Kollárova", "Kollárova 19", "Obědy:\n10.30 - 14.30"),
		new Building("Bufet Lochotín", "Bolevecká 30", "Po-St: 10.30 - 19.30\nČt: 09.00 - 18.00\nPá: 09.00 - 15.00\n\nProvozní doba ve zkouškovém období:\nPo-Čt: 10.30 - 16.30\nPá: 09.00 - 14.00\n\nVýdej hotových jídel:\nPo-Pá: od 11.30"),
		new Building("Bufet FAV/FST", "Univerzitní 22", "Po-Čt: 09.00 - 16.30\nPá: 09.00 - 15.00\n\nProvozní doba ve zkouškovém období:\nPo-Čt: 09.00 - 15.30\nPá: 09.00 - 14.00\n\nVýdej hotových jídel:\nPo-Pá: od 10.30"),
		new Building("Bufet PF", "Klatovská 51", "Po-Čt: 09.00 - 16.30\nPá: 09.00 - 15.00\n\nProvozní doba ve zkouškovém období:\nPo-Čt: 09.00 - 15.30\nPá: 09.00 - 14.00\n\nVýdej hotových jídel:\nPo-Pá: od 10.00")
	};
	
	private final String name;
	private final String address;
	private final String openTime;
	
	public Building(String name, String address, String openTime) {
		this.name = name;
		this.address = address;
		this.openTime = openTime;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getOpenTime() {
		return openTime;
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
