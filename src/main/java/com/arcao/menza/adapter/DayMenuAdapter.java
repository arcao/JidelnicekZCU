package com.arcao.menza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arcao.menza.R;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.api.data.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DayMenuAdapter extends BaseAdapter {
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_SECTION = 1;
    protected static final int COUNT_OF_TYPES = TYPE_SECTION + 1;

    protected final List<Object> items;
    protected final LayoutInflater mInflater;


    public DayMenuAdapter(Context mContext, Section[] sections) {
		items = new ArrayList<Object>();
        mInflater = LayoutInflater.from(mContext);

        fillItems(sections);
	}

	protected void fillItems(Section[] sections) {
		for (Section section : sections) {
			items.add(new SectionItem(section.name));
			for (Meal meal: section.meals) {
				items.add(meal);
			}
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    @Override
    public int getItemViewType(int position) {
        return (getItem(position) instanceof SectionItem) ? TYPE_SECTION : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return COUNT_OF_TYPES;
    }

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
    public boolean isEnabled(int position) {
        return !(getItem(position) instanceof SectionItem);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Object item = getItem(position);
        if (item instanceof SectionItem) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_menu_section, parent, false);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(((SectionItem) item).name);
            return convertView;
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_menu_item, parent, false);

            holder = new ViewHolder();
			holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
			holder.rating.setMax(100);
            holder.rating.setIsIndicator(true);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

		Meal meal = (Meal) item;

        holder.number.setText(String.valueOf(meal.id));
		holder.name.setText(meal.name);
		holder.price.setText(String.valueOf((int)meal.priceStudent)); // TODO price according to configuration

		if (meal.quality < 0) {
			holder.rating.setVisibility(View.INVISIBLE);
		} else {
			holder.rating.setVisibility(View.VISIBLE);
			holder.rating.setProgress((int) (20F + 80F * (meal.quality / 100F)));
		}

        return convertView;

    }

	protected static class SectionItem {
		public final String name;

		public SectionItem(String name) {
			this.name = name.toUpperCase(Locale.US);
		}
	}

    protected static class ViewHolder {
        TextView name;
		TextView number;
		TextView price;
		RatingBar rating;
    }

}
