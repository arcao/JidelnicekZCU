package com.arcao.menza;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public abstract class AbstractBaseActivity extends ActionBarActivity {
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setTitle(getTitle());
		}
	}
}
