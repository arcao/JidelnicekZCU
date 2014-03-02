package com.arcao.menza.api;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenzaUrlGenerator {
	protected static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");

	protected static final String URL_PREFIX = "http://menza.arcao.com/api/v2/";
	protected static final String DAY_URL = URL_PREFIX + "get/%d/%s";
	protected static final String RATING_URL = URL_PREFIX + "vote/";

	public static String generateDayUrl(int placeId, Date date) {
		return String.format(DAY_URL, placeId, DAY_FORMAT.format(date));
	}

	public static String generateRatingUrl() {
		return RATING_URL;
	}
}
