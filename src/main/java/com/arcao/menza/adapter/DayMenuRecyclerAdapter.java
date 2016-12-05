package com.arcao.menza.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.arcao.menza.R;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.constant.PrefConstant;
import java.util.ArrayList;
import java.util.List;

public class DayMenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    public static final int TYPE_SECTION = 1;

    public interface OnItemClickListener {
        void onItemClick(Meal item);
    }

    private final List<Object> items = new ArrayList<>();
    private final String priceGroup;
    private OnItemClickListener onItemClickListener;

    public DayMenuRecyclerAdapter(Context mContext) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        priceGroup = mSharedPreferences.getString(PrefConstant.PRICE_GROUP, PrefConstant.PRICE_GROUP__STUDENT);
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public void fillItems(List<com.arcao.menza.api.data.Section> sections) {
        clearItems();

        for (com.arcao.menza.api.data.Section section : sections) {
            items.add(new Section(section.name()));
            items.addAll(section.meals());
        }

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private String getMealPrice(Meal meal) {
        switch (priceGroup) {
            case PrefConstant.PRICE_GROUP__STAFF:
                return AppConstant.PRICE_FORMAT.format(meal.priceStaff());

            case PrefConstant.PRICE_GROUP__EXTERNAL:
                return AppConstant.PRICE_FORMAT.format(meal.priceExternal());

            case PrefConstant.PRICE_GROUP__STUDENT:
            default:
                return AppConstant.PRICE_FORMAT.format(meal.priceStudent());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SECTION:
                return new SectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_section, parent, false));
            default:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind((Meal) items.get(position));
        } else if (holder instanceof SectionViewHolder) {
            ((SectionViewHolder) holder).bind((Section) items.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Section ? TYPE_SECTION : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class Section {
        final String name;

        Section(String name) {
            this.name = name;
        }
    }

    private class SectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        SectionViewHolder(View view) {
            super(view);

            view.setClickable(false);
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);

            titleTextView = (TextView) view.findViewById(R.id.name);
        }

        void bind(Section item) {
            titleTextView.setText(item.name);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleTextView;
        private final TextView priceTextView;
        private final RatingBar ratingBar;
        private final int titleTextViewOriginalColor;
        private final int titleTextViewPremiumColor;

        ItemViewHolder(View view) {
            super(view);

            view.setClickable(true);
            view.setFocusable(true);
            view.setOnClickListener(this);

            titleTextView = (TextView) view.findViewById(R.id.name);
            priceTextView = (TextView) view.findViewById(R.id.price);
            ratingBar = (RatingBar) view.findViewById(R.id.rating);

            ratingBar.setMax(AppConstant.RATING__MAX);
            ratingBar.setNumStars(AppConstant.RATING__NUM_STARS);

            titleTextViewOriginalColor = titleTextView.getCurrentTextColor();
            //noinspection deprecation
            titleTextViewPremiumColor = view.getContext().getResources().getColor(R.color.premium_meal);
        }

        void bind(Meal item) {
            titleTextView.setText(item.name());
            priceTextView.setText(getMealPrice(item));
            titleTextView.setTextColor(item.premium() ? titleTextViewPremiumColor : titleTextViewOriginalColor);

            if (item.quality() > 0) {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setProgress((int) (AppConstant.RATING__STEP_SIZE + item.quality() / AppConstant.RATING__QUANTIFIER));
            } else {
                ratingBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick((Meal) items.get(getLayoutPosition()));
            }
        }
    }
}