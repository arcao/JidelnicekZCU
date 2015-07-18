package com.arcao.feedback.collector;

import com.arcao.menza.BuildConfig;

public class BuildConfigCollector extends Collector {
	@Override
	public String getName() {
		return "BuildConfig INFO";
	}

	@Override
	protected String collect() {

		return "APPLICATION_ID=" + BuildConfig.APPLICATION_ID +
						"\nBUILD_TIME=" + BuildConfig.BUILD_TIME +
						"\nBUILD_TYPE=" + BuildConfig.BUILD_TYPE +
						"\nDEBUG=" + BuildConfig.DEBUG +
						"\nFLAVOR=" + BuildConfig.FLAVOR +
						"\nGIT_SHA=" + BuildConfig.GIT_SHA +
						"\nVERSION_CODE=" + BuildConfig.VERSION_CODE +
						"\nVERSION_NAME=" + BuildConfig.VERSION_NAME +
						"\n";
	}
}
