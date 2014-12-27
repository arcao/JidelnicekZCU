package com.arcao.menza.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.format.DateUtils;

import com.arcao.menza.MenzaApplication;
import com.arcao.menza.R;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.fragment.DayMenuFragment;
import com.arcao.menza.fragment.UpdateableFragment;

import java.util.Calendar;
import java.util.Date;

public class DayPagerAdapter extends FragmentStatePagerAdapter {
	protected int placeId;

	public DayPagerAdapter(FragmentManager fm, int initialPlaceId) {
		super(fm);
		this.placeId = initialPlaceId;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new DayMenuFragment();

		Bundle args = new Bundle();
		args.putInt(DayMenuFragment.ARG_DAY_ID, position);
		args.putInt(DayMenuFragment.ARG_PLACE_ID, placeId);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		if (object instanceof UpdateableFragment) {
			UpdateableFragment fragment = (UpdateableFragment) object;
			fragment.getArguments().putInt(DayMenuFragment.ARG_PLACE_ID, placeId);
			fragment.update();
		}

		return super.getItemPosition(object);
	}

	@Override
	public int getCount() {
		return AppConstant.DAY_ID_MAX;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, position - AppConstant.DAY_ID_TODAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date date = cal.getTime();

		switch (position) {
			case AppConstant.DAY_ID_YESTERDAY:
			case AppConstant.DAY_ID_TODAY:
			case AppConstant.DAY_ID_TOMORROW:
				return MenzaApplication.getInstance().getResources().getStringArray(R.array.relative_day_title)[position - AppConstant.DAY_ID_YESTERDAY];
			default:
				return DateUtils.formatDateTime(MenzaApplication.getInstance(), date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
		}
	}

	public void updatePlaceId(int placeId) {
		this.placeId = placeId;
		notifyDataSetChanged();
	}
}
