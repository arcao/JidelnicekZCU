package com.arcao.menza.constant;

import java.text.DecimalFormat;

public interface AppConstant {
	int DAY_ID_MAX = 14;
	int DAY_ID_TODAY = 7;
	int DAY_ID_YESTERDAY = DAY_ID_TODAY - 1;
	int DAY_ID_TOMORROW = DAY_ID_TODAY + 1;

	String CONSUMER_KEY = "JidelnicekZCU";
	String CONSUMER_SECRET = "Psv590isi3M1FR97SkQ7ews4iWP1Td60";

	DecimalFormat PRICE_FORMAT = new DecimalFormat("0.##");

	int RATING__NUM_STARS = 5;
	int RATING__MAX = 100;
	int RATING__STEP_SIZE = RATING__MAX / RATING__NUM_STARS;
	float RATING__QUANTIFIER = RATING__NUM_STARS / (float) (RATING__NUM_STARS - 1);
}
