package com.arcao.menza.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.arcao.menza.R;
import com.arcao.menza.widget.SelectableTextView;

import java.util.List;

public class DrawerListAdapter extends BaseAdapter {
	protected final List<Item> mItems;
	protected final Context mContext;
	protected final LayoutInflater mInflater;


	public DrawerListAdapter(Context context, List<Item> items) {
		mItems = items;
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Item getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
			convertView.setTag(convertView.findViewById(android.R.id.text1));
		}

		SelectableTextView textView = (SelectableTextView) convertView.getTag();

		Item item = getItem(position);
		textView.setText(item.name);
		textView.setCompoundDrawables(item.drawable, null, null, null);

		return convertView;
	}

	public static class Item {
		public String name;
		public Drawable drawable;

		public Item(String name, Drawable drawable) {
			this.name = name;
			this.drawable = drawable;
		}
	}
}
