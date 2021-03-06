package com.arcao.menza;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by msloup on 27.2.14.
 */
public abstract class AbstractPopupActionBarActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		getSupportActionBar().setTitle(title);
	}

	protected boolean showAsPopup(int widthResId, int heightResId) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Not supported, because of problems with touching outside of window
			return false;
		}

		int width = getResources().getDimensionPixelSize(widthResId);
		int height = getResources().getDimensionPixelSize(heightResId);

		if (width == 0 || height == 0) {
			return false;
		}

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		this.getWindow().setAttributes(params);

		// This sets the window size, while working around the IllegalStateException thrown by ActionBarView
		this.getWindow().setLayout(width, height);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
