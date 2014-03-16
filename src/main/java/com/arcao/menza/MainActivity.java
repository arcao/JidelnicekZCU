package com.arcao.menza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.arcao.menza.adapter.DayPagerAdapter;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.PriceGroupChangeableDialogFragment;
import com.arcao.menza.fragment.PriceGroupSelectionDialogFragment;
import com.arcao.menza.util.FeedbackHelper;

public class MainActivity extends ActionBarActivity implements PriceGroupSelectionDialogFragment.OnPriceGroupSelectedListener {
	private static final String STATE_PLACE_ID = "STATE_PLACE_ID";
	private static final int RESULT_SETTINGS = 1;

	private DayPagerAdapter mDayPagerAdapter;
	private ViewPager mViewPager;
	private SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);

		int placeId = 0;

		if (savedInstanceState != null) {
			placeId = savedInstanceState.getInt(STATE_PLACE_ID, placeId);
		}

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		mDayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager(), placeId);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDayPagerAdapter);
		mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY);

		ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int position, long itemId) {
				mDayPagerAdapter.updatePlaceId(position);
				return true;
			}
		};

		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.places, R.layout.actionbar_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
		actionBar.setSelectedNavigationItem(placeId);
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();

		if (mSharedPreferences.getString(PrefConstant.PRICE_GROUP, null) == null
				&& getSupportFragmentManager().findFragmentByTag(PriceGroupSelectionDialogFragment.TAG) == null) {
			PriceGroupSelectionDialogFragment.newInstance().show(getSupportFragmentManager(), PriceGroupSelectionDialogFragment.TAG);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(STATE_PLACE_ID, getSupportActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_today:
				mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY, true);
				return true;
			case R.id.action_info:
				startActivity(new Intent(this, PlacePreviewActivity.class).putExtra(MealPreviewActivity.PARAM_PLACE_ID, getSupportActionBar().getSelectedNavigationIndex()));
				return true;
			case R.id.action_settings:
				startActivityForResult(new Intent(this, SettingsActivity.class), RESULT_SETTINGS);
				return true;
			case R.id.action_feedback:
				FeedbackHelper.sendFeedBack(this, R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPriceGroupSelected(String priceGroup) {
		mSharedPreferences.edit().putString(PrefConstant.PRICE_GROUP, priceGroup).commit();
		mDayPagerAdapter.notifyDataSetChanged();

		PriceGroupChangeableDialogFragment.newInstance().show(getSupportFragmentManager(), PriceGroupChangeableDialogFragment.TAG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case RESULT_SETTINGS:
				// reload fragments
				mDayPagerAdapter.notifyDataSetChanged();
				break;
		}
	}
}
