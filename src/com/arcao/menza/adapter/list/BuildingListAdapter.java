package com.arcao.menza.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcao.menza.R;
import com.arcao.menza.dto.Building;

public class BuildingListAdapter extends BaseAdapter {
	
	
	private final LayoutInflater mInflater;

	public BuildingListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		return Building.getBuildings().length;
	}

	public Building getItem(int position) {
		return Building.getBuilding(position);
	}

	public long getItemId(int position) {
		return position;
	}

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
}
