package com.arcao.menza.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class UpperCaseTextView extends TextView {

	public UpperCaseTextView(Context context) {
		super(context);
		setTransformationMethod(upperCaseTransformation);
	}

	public UpperCaseTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTransformationMethod(upperCaseTransformation);
	}

	public UpperCaseTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTransformationMethod(upperCaseTransformation);
	}

	private final TransformationMethod upperCaseTransformation =
		new TransformationMethod() {
			private final Locale locale = getResources().getConfiguration().locale;

			@Override
			public CharSequence getTransformation(CharSequence source, View view) {
				return source != null ? source.toString().toUpperCase(locale) : null;
			}

			@Override
			public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {}
		};
}