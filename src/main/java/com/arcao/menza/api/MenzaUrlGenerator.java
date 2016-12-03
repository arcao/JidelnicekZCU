package com.arcao.menza.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class MenzaUrlGenerator {
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.US);

    private static final String URL_PREFIX = "http://menza.arcao.com/api/v2.1/";
    private static final String DAY_URL = URL_PREFIX + "get/%d/%s";
    private static final String RATING_URL = URL_PREFIX + "vote/";
    private static final String PLACE_URL = URL_PREFIX + "places/%d/";

    public static String generateDayUrl(int placeId, Date date) {
        String url = String.format(Locale.US, DAY_URL, placeId + 1, DAY_FORMAT.format(date));
        Timber.d("MenzaUrlGenerator: %s", url);
        return url;
    }

    public static String generateRatingUrl() {
        return RATING_URL;
    }

    public static String generatePlaceUrl(int placeId) {
        String url = String.format(Locale.US, PLACE_URL, placeId);
        Timber.d("MenzaUrlGenerator: %s", url);
        return url;
    }
}
