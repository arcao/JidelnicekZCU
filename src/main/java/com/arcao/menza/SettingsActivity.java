package com.arcao.menza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.util.FeedbackHelper;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		addPreferencesFromResource(R.xml.preferences);

		Preference feedBackPref = findPreference("feedback");
		feedBackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				FeedbackHelper.sendFeedBack(SettingsActivity.this, R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message);
				return true;
			}
		});

		Preference licensesPref = findPreference("licenses");
		licensesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(SettingsActivity.this, WebViewActivity.class);
				i.putExtra(WebViewActivity.PARAM_TITLE, R.string.pref_licenses);
				i.putExtra(WebViewActivity.PARAM_RAW_RESOURCE, R.raw.licenses);
				startActivity(i);
				return true;
			}
		});

		findPreference("version").setSummary(getVersion(this));


		// fix for Android 2.x
		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), PrefConstant.PRICE_GROUP);
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
				setResult(MainActivity.RESULT_REFRESH);
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

	public static String getVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "0.0";
		}
	}
}
