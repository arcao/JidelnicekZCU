package com.arcao.menza;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.arcao.menza.constant.PrefConstant;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		switch (key) {
			case PrefConstant.PRICE_GROUP:
				updateListPreferenceSummary(key);
				break;
		}
	}

	protected void updateListPreferenceSummary(String key) {
		ListPreference p = findPreference(key);
		p.setSummary(p.getEntry());
	}

	protected <P extends Preference> P findPreference(String key) {
		return (P)super.findPreference(key);
	}

	protected <P extends Preference> P findPreference(String key, Class<P> clazz) {
		return (P)super.findPreference(key);
	}
}
