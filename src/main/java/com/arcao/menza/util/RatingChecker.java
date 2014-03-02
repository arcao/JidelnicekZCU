package com.arcao.menza.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.arcao.menza.constant.PrefConstant;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class RatingChecker {
	protected static final int CAPACITY = 100;

	protected final Queue<String> ratingKeys;
	protected final SharedPreferences mSharedPreferences;


	public RatingChecker(Context context) {
		ratingKeys = new LinkedList<>();

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		loadRatingKeys();
	}

	protected void loadRatingKeys() {
		String keysString = mSharedPreferences.getString(PrefConstant.VOTES, null);
		if (keysString == null)
			return;

		String[] keys = keysString.split(PrefConstant.VOTES_SEPARATOR);

		for (String key : keys) {
			ratingKeys.add(key);
		}
	}

	public synchronized boolean isRated(Date date, String hash) {
		return ratingKeys.contains(String.format(PrefConstant.VOTES_VALUE_FORMAT, date, hash));
	}

	public synchronized void addRating(Date date, String hash) {
		String key = String.format(PrefConstant.VOTES_VALUE_FORMAT, date, hash);

		if (ratingKeys.contains(key))
			return;

		while (ratingKeys.size() >= CAPACITY) {
			ratingKeys.poll();
		}

		ratingKeys.add(key);

		String keys = join(ratingKeys, PrefConstant.VOTES_SEPARATOR);

		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(PrefConstant.VOTES, keys);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	protected static String join(Collection<String> collection, String separator) {
		StringBuffer sb = new StringBuffer();

		for (String item : collection) {
			if (item == null || item.length() == 0)
				continue;;

			if (sb.length() != 0)
				sb.append(separator);

			sb.append(item);
		}

		return sb.toString();
	}
}
