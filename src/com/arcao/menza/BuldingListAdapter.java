package com.arcao.menza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BuldingListAdapter extends BaseAdapter {
	private static Building[] buildings = new Building[] {
		new Building("Menza Bory", "Univerzitní 12"),
		new Building("Menza Kollárova", "Kollárova 19"),
		new Building("Bufet Lochotín", "Bolevecká 30"),
		new Building("Bufet FAV/FST", "Univerzitní 22"),
		new Building("Bufet PF", "Klatovská 51")
	};
	
	private LayoutInflater mInflater;

	public BuldingListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		return buildings.length;
	}

	@Override
	public Building getItem(int position) {
		return buildings[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
		    convertView = mInflater.inflate(R.layout.menza_item, parent, false);

		    holder = new ViewHolder();
		    holder.name = (TextView) convertView.findViewById(R.id.name);
		    holder.address = (TextView) convertView.findViewById(R.id.address);
		    convertView.setTag(holder);
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(this.getItem(position).getName());
		holder.address.setText(this.getItem(position).getAddress());
		return convertView; 
	}

	private class ViewHolder {
	    TextView name;
	    TextView address;
	} 
	
	public static Building getBuilding(int id) {
		return buildings[id];
	}
}
