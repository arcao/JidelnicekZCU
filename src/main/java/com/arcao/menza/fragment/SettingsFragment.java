package com.arcao.menza.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.util.Log;

import com.arcao.feedback.FeedbackHelper;
import com.arcao.menza.BuildConfig;
import com.arcao.menza.MainActivity;
import com.arcao.menza.R;
import com.arcao.menza.WebViewActivity;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.dialog.AbstractDialogFragment;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
	private static final String TAG = "SettingsFragment";

	@Override
	public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		Preference feedBackPref = findPreference("feedback");
		feedBackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				FeedbackHelper.sendFeedback(getActivity(), R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message);
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
		if (!TextUtils.isEmpty(BuildConfig.BUILD_TIME)) {
			try {
				//noinspection deprecation
				findPreference("build_time").setSummary(
						DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, getResources().getConfiguration().locale).format(
								new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).parse(BuildConfig.BUILD_TIME)
						)
				);
			} catch (ParseException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}


		// fix for Android 2.x
		//updateListPreferenceSummary(PrefConstant.PRICE_GROUP);
		//updateListPreferenceSummary(PrefConstant.DEFAULT_PLACE);
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
				//updateListPreferenceSummary(key);
				break;
			case PrefConstant.DEFAULT_PLACE:
				//updateListPreferenceSummary(key);
				showChangesApplyAfterRestartDialog();
				break;
		}
	}

	private void showChangesApplyAfterRestartDialog() {
		ChangesApplyAfterRestartDialogFragment.newInstance().show(getActivity().getFragmentManager(), ChangesApplyAfterRestartDialogFragment.TAG);
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
				.setPositiveButton(R.string.button_ok, null)
				.create();
		}
	}
}
