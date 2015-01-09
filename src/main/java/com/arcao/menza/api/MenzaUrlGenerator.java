package com.arcao.menza.api;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenzaUrlGenerator {
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private static final String URL_PREFIX = "http://menza.arcao.com/api/v2.1/";
	private static final String DAY_URL = URL_PREFIX + "get/%d/%s";
	private static final String RATING_URL = URL_PREFIX + "vote/";
	private static final String PLACE_URL = URL_PREFIX + "places/%d/";

	public static String generateDayUrl(int placeId, Date date) {
		String url = String.format(DAY_URL, placeId + 1, DAY_FORMAT.format(date));
		Log.v("JZ|MenzaUrlGenerator", url);
		return url;
	}

	public static String generateRatingUrl() {
		return RATING_URL;
	}

	public static String generatePlaceUrl(int placeId) {
		String url = String.format(PLACE_URL, placeId);
		Log.v("JZ|MenzaUrlGenerator", url);
		return url;
	}
}
