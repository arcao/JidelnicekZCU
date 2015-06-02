package com.arcao.menza.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;

import com.arcao.menza.BuildConfig;
import com.arcao.menza.MainActivity;
import com.arcao.menza.R;
import com.arcao.menza.WebViewActivity;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.dialog.AbstractDialogFragment;
import com.arcao.menza.util.FeedbackHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	private static final String TAG = "SettingsFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		Preference feedBackPref = findPreference("feedback");
		feedBackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				FeedbackHelper.sendFeedBack(getActivity(), R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message, false);
				return true;
			}
		});

		Preference licensesPref = findPreference("licenses");
		licensesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getActivity(), WebViewActivity.class);
				i.putExtra(WebViewActivity.PARAM_TITLE, R.string.pref_licenses);
				i.putExtra(WebViewActivity.PARAM_RAW_RESOURCE, R.raw.licenses);
				startActivity(i);
				return true;
			}
		});

		findPreference("version").setSummary(getVersion(getActivity()) + " (" + BuildConfig.GIT_SHA + ")");
		try {
			findPreference("build_time").setSummary(
							DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, getResources().getConfiguration().locale).format(
											new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).parse(BuildConfig.BUILD_TIME)
							)
			);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}


		// fix for Android 2.x
		updateListPreferenceSummary(PrefConstant.PRICE_GROUP);
		updateListPreferenceSummary(PrefConstant.DEFAULT_PLACE);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
						.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
						.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		switch (key) {
			case PrefConstant.PRICE_GROUP:
				getActivity().setResult(MainActivity.RESULT_REFRESH);
				updateListPreferenceSummary(key);
				break;
			case PrefConstant.DEFAULT_PLACE:
				updateListPreferenceSummary(key);
				showChangesApplyAfterRestartDialog();
				break;
		}
	}

	private void showChangesApplyAfterRestartDialog() {
		ChangesApplyAfterRestartDialogFragment.newInstance().show(getActivity().getSupportFragmentManager(), ChangesApplyAfterRestartDialogFragment.TAG);
	}

	private void updateListPreferenceSummary(String key) {
		ListPreference p = findPreference(key);
		p.setSummary(p.getEntry());
	}

	@SuppressWarnings("unchecked")
	private <P extends Preference> P findPreference(String key) {
		return (P)super.findPreference(key);
	}

	private static String getVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "0.0";
		}
	}

	public static class ChangesApplyAfterRestartDialogFragment extends AbstractDialogFragment {
		public static final String TAG = "ChangesApplyAfterRestartDialogFragment";

		public static ChangesApplyAfterRestartDialogFragment newInstance() {
			return new ChangesApplyAfterRestartDialogFragment();
		}

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.pref_default_place_title)
				.setMessage(R.string.dialog_changes_apply_after_restart)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				})
				.create();
		}
	}
}
