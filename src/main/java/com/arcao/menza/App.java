package com.arcao.menza;

import android.app.Application;

import com.arcao.menza.volley.VolleyHelper;

import timber.log.Timber;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		VolleyHelper.init(getApplicationContext());

		Timber.plant(new Timber.DebugTree());
	}
}
