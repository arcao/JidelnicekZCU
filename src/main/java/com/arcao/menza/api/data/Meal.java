package com.arcao.menza.api.data;

public class Meal {
	public int id;
	public String name;
	public float priceStudent;
	public float priceStaff;
	public float priceExternal;
	public String hash;
	public float quality;
	public int commentCount;
	public int imageCount;
	public Image[] images;
	public Comment[] comments;
}
