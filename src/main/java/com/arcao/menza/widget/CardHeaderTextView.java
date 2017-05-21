package com.arcao.menza.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CardHeaderTextView extends AppCompatTextView {
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

    private void init(CardHeaderTextView textView, Context context) {
        if (isInEditMode())
            return;

        if (FONT == null)
            FONT = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        textView.setTypeface(FONT);
    }
}