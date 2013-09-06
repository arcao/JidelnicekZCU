package com.arcao.menza.api;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by msloup on 6.9.13.
 */
public class MenzaUrlGenerator {
	protected static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
	protected static final String DAY_URL = "http://menza.arcao.com/api/get_v2/%d/%s";

	public static String generateDayUrl(int menzaId, Date date) {
		return String.format(DAY_URL, menzaId, DAY_FORMAT.format(date));
	}
}
