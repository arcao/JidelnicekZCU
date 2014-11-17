package com.arcao.menza.util;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;

public class SharedPreferencesCompat {
	public static void apply(@NonNull SharedPreferences.Editor editor) {
		if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}
}
