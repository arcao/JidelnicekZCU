package com.arcao.menza.widget.decorator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arcao.menza.adapter.DayMenuRecyclerAdapter;

/**
 * Disabling DividerItemDecoration around {@link com.arcao.menza.adapter.DayMenuRecyclerAdapter#TYPE_SECTION} item types
 */
public class DayMenuDividerItemDecoration extends DividerItemDecoration {
	public DayMenuDividerItemDecoration(Context context, int orientation) {
		super(context, orientation);
	}

	@Override
	protected void drawVerticalDivider(Canvas c, RecyclerView parent, int left, int right, int pos) {
		int adapterPos = parent.getChildPosition(parent.getChildAt(pos));

		// skip drawing above section position
		//if (!isSectionPosition(parent, adapterPos))
		//	return;

		// skip drawing bellow section position
		if (!isSectionPosition(parent, adapterPos + 1))
			return;

		super.drawVerticalDivider(c, parent, left, right, pos);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		int adapterPos = parent.getChildPosition(view);

		// skip setting item offset if current item is section - we don't draw divider
		//if (!isSectionPosition(parent, adapterPos)) {
		//	outRect.set(0, 0, 0, 0);
		//	return;
		//}

		// skip setting item offset if next item is section - we don't draw divider
		if (!isSectionPosition(parent, adapterPos + 1)) {
			outRect.set(0, 0, 0, 0);
			return;
		}

		super.getItemOffsets(outRect, view, parent, state);
	}

	private boolean isSectionPosition(RecyclerView recyclerView, int pos) {
		return pos < recyclerView.getAdapter().getItemCount() &&
						recyclerView.getAdapter().getItemViewType(pos) == DayMenuRecyclerAdapter.TYPE_SECTION;
	}
}
