package com.arcao.menza.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.util.StateSet;
import android.util.TypedValue;

import com.arcao.menza.R;

public class ThemedDrawable {
	@SuppressLint("InlinedApi")
	private static final int[][] STATES_LIST = {
					new int[] { -android.R.attr.state_enabled },
					new int[] { android.R.attr.state_focused },
					new int[] { android.R.attr.state_activated },
					new int[] { android.R.attr.state_pressed },
					new int[] { android.R.attr.state_checked },
					new int[] { android.R.attr.state_selected }
	};

	public static Drawable getDrawable(Context context, @DrawableRes int resId) {
		return getDrawable(context, resId, PorterDuff.Mode.SRC_IN);
	}

	public static Drawable getDrawable(Context context, @DrawableRes int resId, PorterDuff.Mode mode) {
		return createThemedDrawable(context, ContextCompat.getDrawable(context, resId), mode);
	}

	public static Drawable createThemedDrawable(Context context, Drawable drawable, PorterDuff.Mode mode) {
		final int colorControlNormal = getThemeAttrColor(context, R.attr.colorControlNormal);
		final int colorControlActivated = getThemeAttrColor(context, R.attr.colorControlActivated);
		final int colorControlDisabled = getDisabledThemeAttrColor(context, R.attr.colorControlNormal);

		ColorFilter colorFilterNormal = new PorterDuffColorFilter(colorControlNormal, mode);
		ColorFilter colorFilterActivated = new PorterDuffColorFilter(colorControlActivated, mode);
		ColorFilter colorFilterDisabled = new PorterDuffColorFilter(colorControlDisabled, mode);

		Drawable drawableNormal = drawable.getConstantState().newDrawable().mutate();
		Drawable drawableActivated = drawable.getConstantState().newDrawable().mutate();
		Drawable drawableDisabled = drawable.getConstantState().newDrawable().mutate();

		FilterableStateListDrawable listDrawable = new FilterableStateListDrawable();
		listDrawable.addState(new int[] { -android.R.attr.state_enabled }, drawableDisabled, colorFilterDisabled);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			listDrawable.addState(new int[]{android.R.attr.state_activated}, drawableActivated, colorFilterActivated);
		}
		listDrawable.addState(new int[] { android.R.attr.state_pressed }, drawableActivated, colorFilterActivated);
		listDrawable.addState(new int[] { android.R.attr.state_checked }, drawableActivated, colorFilterActivated);
		listDrawable.addState(new int[] { android.R.attr.state_selected }, drawableActivated, colorFilterActivated);
		listDrawable.addState(StateSet.WILD_CARD, drawableNormal, colorFilterNormal);

		return listDrawable;
	}

	public static Drawable createColorStateListDrawable(Drawable drawable, ColorStateList colorStateList, PorterDuff.Mode mode) {
		FilterableStateListDrawable listDrawable = new FilterableStateListDrawable();
		for (int[] states : STATES_LIST) {
			int color = colorStateList.getColorForState(states, colorStateList.getDefaultColor());
			if (color != colorStateList.getDefaultColor()) {
				PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, mode);
				Drawable newDrawable = drawable.getConstantState().newDrawable().mutate();
				listDrawable.addState(states, newDrawable, colorFilter);
			}
		}

		int color = colorStateList.getDefaultColor();
		PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, mode);
		Drawable newDrawable = drawable.getConstantState().newDrawable().mutate();
		listDrawable.addState(StateSet.WILD_CARD, newDrawable, colorFilter);

		return listDrawable;
	}

	private static int getThemeAttrColor(Context context, int attr) {
		TypedValue typedValue = new TypedValue();

		if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
			if (typedValue.type >= TypedValue.TYPE_FIRST_INT
							&& typedValue.type <= TypedValue.TYPE_LAST_INT) {
				return typedValue.data;
			} else if (typedValue.type == TypedValue.TYPE_STRING) {
				return context.getResources().getColor(typedValue.resourceId);
			}
		}
		return 0;
	}

	private static int getThemeAttrColor(Context context, int attr, float alpha) {
		final int color = getThemeAttrColor(context, attr);
		final int originalAlpha = Color.alpha(color);

		// Return the color, multiplying the original alpha by the disabled value
		return (color & 0x00ffffff) | (Math.round(originalAlpha * alpha) << 24);
	}

	private static int getDisabledThemeAttrColor(Context context, int attr) {
		TypedValue typedValue = new TypedValue();

		// Now retrieve the disabledAlpha value from the theme
		context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, typedValue, true);
		final float disabledAlpha = typedValue.getFloat();

		return getThemeAttrColor(context, attr, disabledAlpha);
	}

	/**
	 * This is an extension to {@link android.graphics.drawable.StateListDrawable} that workaround a bug not allowing
	 * to set a {@link android.graphics.ColorFilter} to the drawable in one of the states., it add a method
	 * {@link #addState(int[], android.graphics.drawable.Drawable, android.graphics.ColorFilter)} for that purpose.
	 * <p/>
	 * I've opened a bug about this: https://code.google.com/p/android/issues/detail?id=60183
	 */
	private static class FilterableStateListDrawable extends StateListDrawable {

		private int currIdx = -1;
		private int childrenCount = 0;
		private final SparseArray<ColorFilter> filterMap;

		public FilterableStateListDrawable() {
			super();
			filterMap = new SparseArray<>();
		}

		@Override
		public void addState(int[] stateSet, Drawable drawable) {
			super.addState(stateSet, drawable);
			childrenCount++;
		}

		/**
		 * Same as {@link #addState(int[], android.graphics.drawable.Drawable)}, but allow to set a colorFilter associated to this Drawable.
		 *
		 * @param stateSet    - An array of resource Ids to associate with the image.
		 *                    Switch to this image by calling setState().
		 * @param drawable    -The image to show.
		 * @param colorFilter - The {@link android.graphics.ColorFilter} to apply to this state
		 */
		public void addState(int[] stateSet, Drawable drawable, ColorFilter colorFilter) {
			int currChild = childrenCount;
			addState(stateSet, drawable);
			filterMap.put(currChild, colorFilter);
		}

		@Override
		public boolean selectDrawable(int idx) {
			if (currIdx != idx) {
				setColorFilter(getColorFilterForIdx(idx));
			}
			boolean result = super.selectDrawable(idx);
			if (getCurrent() != null) {
				currIdx = result ? idx : currIdx;
				if (!result) {
					setColorFilter(getColorFilterForIdx(currIdx));
				}
			} else if (getCurrent() == null) {
				currIdx = -1;
				setColorFilter(null);
			}
			return result;
		}

		private ColorFilter getColorFilterForIdx(int idx) {
			return filterMap != null ? filterMap.get(idx) : null;
		}
	}
}
