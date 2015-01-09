package com.arcao.menza.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CardHeaderTextView extends TextView {
	private static Typeface FONT = null;

	public CardHeaderTextView(Context context) {
		super(context);
		init(this, context);
	}

	public CardHeaderTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(this, context);
	}

	public CardHeaderTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(this, context);
	}

	void init(CardHeaderTextView textView, Context context) {
		if (isInEditMode()) return;

		if (FONT == null)
			FONT = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
		textView.setTypeface(FONT);
	}
}