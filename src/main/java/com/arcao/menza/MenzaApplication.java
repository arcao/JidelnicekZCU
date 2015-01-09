package com.arcao.menza;

import android.app.Application;

import com.arcao.menza.volley.VolleyHelper;

public class MenzaApplication extends Application {
	private static MenzaApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		VolleyHelper.init(getApplicationContext());
	}

	public static MenzaApplication getInstance() {
		return instance;
	}
}
