package com.arcao.menza;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodListAdapter extends BaseAdapter {
	private List<Food> foodList;
	private LayoutInflater mInflater;
	private int priceSource = 0;
	
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SECTION = 1;
	private static final int COUNT_OF_TYPES = TYPE_SECTION + 1;
	
	public FoodListAdapter(Context context, JSONObject jsTypes) {
		mInflater = LayoutInflater.from(context);
		
		if (context instanceof FoodActivity)
			priceSource = ((FoodActivity) context).getPriceSource();
		
		foodList = new ArrayList<Food>();
		
		try {
			fillFoodList(jsTypes);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getItemViewType(int position) {
		return (getItem(position) instanceof FoodSection) ? TYPE_SECTION : TYPE_ITEM; 
	}
	
	@Override
	public int getViewTypeCount() {
		return COUNT_OF_TYPES;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return !(getItem(position) instanceof FoodSection);
	}

	private void fillFoodList(JSONObject jsTypes) throws JSONException {
		@SuppressWarnings("unchecked")
		Iterator<String> types = jsTypes.keys();
		while(types.hasNext()) {
			String type = types.next();
			
			foodList.add(new FoodSection(type));
			
			JSONObject jsSensors = jsTypes.getJSONObject(type); 
			@SuppressWarnings("unchecked")
			Iterator<String> sensors = jsSensors.keys();
			while(sensors.hasNext()) {
				String sensor = sensors.next();
				
				JSONObject jsFood = jsSensors.getJSONObject(sensor);
				String food = jsFood.getString("nazev");
				int price = 0;
				switch (priceSource) {
				case 1:
					price = jsFood.optInt("price_zam");
					break;
				case 2:
					price = jsFood.optInt("price_ext");
					break;
				default:
					price = jsFood.optInt("price_stu");
					break;
				}
				int rating = jsFood.optInt("oblibenost");
				
				foodList.add(new Food(food, type, price, (rating < 0) ? -1F : 1F + (rating * 0.04F)));
			}
		}
	}
	
	@Override
	public int getCount() {
		return foodList.size();
	}

	@Override
	public Food getItem(int position) {
		return foodList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Food item = getItem(position);
		if (item instanceof FoodSection) {
			if (convertView == null) {
			    convertView = mInflater.inflate(R.layout.food_section, parent, false);

			    holder = new ViewHolder();
		        holder.name = (TextView) convertView.findViewById(R.id.section_name);
		        
			    convertView.setTag(holder);
			} else {
			    holder = (ViewHolder) convertView.getTag();
			}
			
			holder.name.setText(item.getName());
			return convertView;
		}
		
		if (convertView == null) {
		    convertView = mInflater.inflate(R.layout.food_item, parent, false);

		    holder = new ViewHolder();
		    holder.name = (TextView) convertView.findViewById(R.id.food_name);
		    holder.price = (TextView) convertView.findViewById(R.id.price);
	        holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
	        
		    convertView.setTag(holder);
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(item.getName());
		holder.price.setText(String.valueOf(item.getPrice()));
		
		if (item.getRating() >= 0) {
			holder.ratingBar.setVisibility(View.VISIBLE);
			holder.ratingBar.setRating(item.getRating());
		} else {
			holder.ratingBar.setVisibility(View.INVISIBLE);
		}
		return convertView; 
	}

	private class ViewHolder {
	    TextView name;
	    TextView price;
	    RatingBar ratingBar;
	} 
}
