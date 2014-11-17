package com.arcao.menza;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public abstract class AbstractBaseActivity extends ActionBarActivity {
	private Toolbar mToolbar;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			mToolbar.setTitle(getTitle());
		}
	}

	public Toolbar getToolbar() {
		return mToolbar;
	}
}
