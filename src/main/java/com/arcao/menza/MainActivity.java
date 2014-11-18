package com.arcao.menza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.arcao.menza.adapter.DayPagerAdapter;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.NavigationDrawerFragment;
import com.arcao.menza.fragment.dialog.PriceGroupChangeableDialogFragment;
import com.arcao.menza.fragment.dialog.PriceGroupSelectionDialogFragment;
import com.arcao.menza.util.FeedbackHelper;

public class MainActivity extends AbstractBaseActivity implements PriceGroupSelectionDialogFragment.OnPriceGroupSelectedListener, NavigationDrawerFragment.OnDrawerCallbackListener {
	public static final String PARAM_PLACE_ID = "PLACE_ID";
  private static final String STATE_PLACE_ID = "STATE_PLACE_ID";
	public static final int RESULT_REFRESH = 101;

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DayPagerAdapter mDayPagerAdapter;
	private ViewPager mViewPager;
	private SharedPreferences mSharedPreferences;
	private int placeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		// prepare current place id
		int defaultPlaceId = Integer.parseInt(mSharedPreferences.getString(PrefConstant.DEFAULT_PLACE, "0"));
		placeId = getIntent().getIntExtra(PARAM_PLACE_ID, defaultPlaceId);
		if (savedInstanceState != null) {
			placeId = savedInstanceState.getInt(STATE_PLACE_ID, placeId);
		}

		setContentView(R.layout.activity_main);

		// prepare toolbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// prepare drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), getToolbar());
		mNavigationDrawerFragment.setPlaceId(placeId);
		setTitle(mNavigationDrawerFragment.getPlaceName(placeId));

		// prepare day adapter
		mDayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager(), placeId);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDayPagerAdapter);
		mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY);
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

		outState.putInt(STATE_PLACE_ID, placeId);
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
				startActivity(new Intent(this, PlacePreviewActivity.class).putExtra(MealPreviewActivity.PARAM_PLACE_ID, placeId));
				return true;
			case R.id.action_settings:
				startActivityForResult(new Intent(this, SettingsActivity.class), 0);
				return true;
			case R.id.action_feedback:
				FeedbackHelper.sendFeedBack(this, R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSettingsSelected() {
		startActivityForResult(new Intent(this, SettingsActivity.class), 0);
	}

	@Override
	public void onFeedbackSelected() {
		FeedbackHelper.sendFeedBack(this, R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message);
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

		if (resultCode == RESULT_REFRESH) {
				// reload fragments
				mDayPagerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPlaceSelected(int placeId) {
		this.placeId = placeId;
		setTitle(mNavigationDrawerFragment.getPlaceName(placeId));
		mDayPagerAdapter.updatePlaceId(placeId);
	}
}
