package com.arcao.menza.constant;

import java.text.DecimalFormat;

public interface AppConstant {
	public static int DAY_ID_MAX = 14;
    public static int DAY_ID_TODAY = 7;
    public static int DAY_ID_YESTERDAY = DAY_ID_TODAY - 1;
    public static int DAY_ID_TOMORROW = DAY_ID_TODAY + 1;

    public static String CONSUMER_KEY = "JidelnicekZCU";
    public static String CONSUMER_SECRET = "Psv590isi3M1FR97SkQ7ews4iWP1Td60";

	public static DecimalFormat PRICE_FORMAT = new DecimalFormat("0.##");
}
