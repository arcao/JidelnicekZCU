package com.arcao.menza;

import android.app.Application;

import com.arcao.menza.volley.VolleyHelper;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		VolleyHelper.init(getApplicationContext());
	}
}
