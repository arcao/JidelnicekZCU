package com.arcao.menza;

import android.app.Application;
import com.arcao.menza.volley.VolleyHelper;

/**
 * Created by msloup on 6.9.13.
 */
public class MenzaApplication extends Application {
	protected static MenzaApplication instance;

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
