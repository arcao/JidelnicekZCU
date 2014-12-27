package com.arcao.menza.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcao.menza.R;
import com.arcao.menza.widget.SelectableTextView;

import java.util.List;

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.ViewHolder> {
	public interface OnItemClickListener {
		void onItemClick(RecyclerView.Adapter<?> parent, View view, int position);
	}

	private final List<Item> items;
	private OnItemClickListener onItemClickListener;
	private int lastSelectedPosition = -1;
	private boolean selectable = false;

	public DrawerRecyclerAdapter(List<Item> items) {
		this.items = items;

		setHasStableIds(true);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	public DrawerRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_list_item, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(DrawerRecyclerAdapter.ViewHolder viewHolder, int position) {
		Item item = items.get(position);

		viewHolder.bind(item);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}


	public static class Item {
		public String name;
		public Drawable drawable;

		public Item(String name, Drawable drawable) {
			this.name = name;
			this.drawable = drawable;
		}
	}

	protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private final SelectableTextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
			textView = (SelectableTextView) itemView;
			itemView.setOnClickListener(this);
		}

		public void bind(Item item) {
			textView.setChecked(selectable && isSelected(getPosition()));

			textView.setText(item.name);
			textView.setCompoundDrawablesWithIntrinsicBounds(item.drawable, null, null, null);
		}

		@Override
		public void onClick(View v) {
			setSelected(getPosition());
			if (onItemClickListener != null) {
				onItemClickListener.onItemClick(DrawerRecyclerAdapter.this, v, getPosition());
			}
		}
	}

	private boolean isSelected(int pos) {
		return lastSelectedPosition == pos;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;

		if (lastSelectedPosition >= 0)
			notifyDataSetChanged();
	}

	public void setSelected(int position) {
		int oldSelectedPosition = lastSelectedPosition;
		lastSelectedPosition = position;

		if (oldSelectedPosition >= 0)
			notifyItemChanged(oldSelectedPosition);

		notifyDataSetChanged();
	}

	public Item getItem(int position) {
		return items.get(position);
	}
}
