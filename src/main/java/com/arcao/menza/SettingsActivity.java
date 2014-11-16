package com.arcao.menza;

import android.os.Bundle;
import android.view.MenuItem;

import com.arcao.menza.fragment.SettingsFragment;

public class SettingsActivity extends AbstractBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Display the fragment as the main content.
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsFragment()).commit();
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
