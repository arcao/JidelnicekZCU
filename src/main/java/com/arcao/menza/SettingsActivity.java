package com.arcao.menza;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.MenuItem;

import com.arcao.menza.fragment.SettingsFragment;

public class SettingsActivity extends AbstractBaseActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) {
			SettingsFragment fragment = new SettingsFragment();

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fragment, fragment);
			ft.commit();
		}
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

	@Override
	public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen) {
		SettingsFragment fragment = new SettingsFragment();
		Bundle args = new Bundle();
		args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
		fragment.setArguments(args);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.replace(R.id.fragment, fragment, preferenceScreen.getKey());
		ft.addToBackStack(preferenceScreen.getKey());
		ft.commit();

		return true;
	}
}
