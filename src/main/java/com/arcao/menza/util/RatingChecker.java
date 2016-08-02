package com.arcao.menza.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.arcao.menza.constant.PrefConstant;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class RatingChecker {
	private static final int CAPACITY = 100;

	private static final Queue<String> ratingKeys = new LinkedList<>();
	private static boolean loaded = false;
	private final SharedPreferences mSharedPreferences;


	public RatingChecker(Context context) {
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		loadRatingKeys();
	}

	private void loadRatingKeys() {
		synchronized (ratingKeys) {
			if (loaded)
				return;

			String keysString = mSharedPreferences.getString(PrefConstant.VOTES, null);
			if (keysString == null)
				return;

			String[] keys = keysString.split(PrefConstant.VOTES_SEPARATOR);


			ratingKeys.clear();
			Collections.addAll(ratingKeys, keys);

			loaded = true;
		}
	}

	public boolean isRated(Date date, String hash) {
		synchronized (ratingKeys) {
			return ratingKeys.contains(String.format(Locale.US, PrefConstant.VOTES_VALUE_FORMAT, date, hash));
		}
	}

	public void addRating(Date date, String hash) {
		String key = String.format(Locale.US, PrefConstant.VOTES_VALUE_FORMAT, date, hash);

		synchronized (ratingKeys) {
			if (ratingKeys.contains(key))
				return;

			while (ratingKeys.size() >= CAPACITY) {
				ratingKeys.poll();
			}

			ratingKeys.add(key);
		}

		String keys = join(ratingKeys, PrefConstant.VOTES_SEPARATOR);

		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(PrefConstant.VOTES, keys);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	public synchronized void removeRating(Date date, String hash) {
		String key = String.format(Locale.US, PrefConstant.VOTES_VALUE_FORMAT, date, hash);

		synchronized (ratingKeys) {
			if (ratingKeys.contains(key))
				return;

			if (!ratingKeys.remove(key))
				return;
		}

		String keys = join(ratingKeys, PrefConstant.VOTES_SEPARATOR);

		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(PrefConstant.VOTES, keys);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	private static String join(Collection<String> collection, String separator) {
		StringBuilder sb = new StringBuilder();

		for (String item : collection) {
			if (item == null || item.length() == 0)
				continue;

			if (sb.length() != 0)
				sb.append(separator);

			sb.append(item);
		}

		return sb.toString();
	}
}
