package com.arcao.menza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arcao.menza.adapter.DayPagerAdapter;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.dialog.PriceGroupChangeableDialogFragment;
import com.arcao.menza.fragment.dialog.PriceGroupSelectionDialogFragment;
import com.arcao.menza.util.FeedbackHelper;

public class MainActivity extends AbstractBaseActivity implements PriceGroupSelectionDialogFragment.OnPriceGroupSelectedListener {
	public static final String PARAM_PLACE_ID = "PLACE_ID";
    private static final String STATE_PLACE_ID = "STATE_PLACE_ID";
	public static final int RESULT_REFRESH = 101;

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
		prepareDrawer();

		// prepare day adapter
		mDayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager(), placeId);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDayPagerAdapter);
		mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY);
	}

	private void prepareDrawer() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

		final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
						this,  mDrawerLayout, mToolbar,
						R.string.navigation_drawer_open, R.string.navigation_drawer_close
		);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		// prepare places
		final ArrayAdapter<CharSequence> mAdapterPlaces = ArrayAdapter.createFromResource(this, R.array.places, R.layout.drawer_place_item);

		final ListView mListPlaces = (ListView) findViewById(R.id.drawer);
		mListPlaces.setAdapter(mAdapterPlaces);
		mListPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				placeId = position;
				mListPlaces.setItemChecked(placeId, true);
				setTitle(mAdapterPlaces.getItem(placeId));
				mDayPagerAdapter.updatePlaceId(placeId);
				mDrawerLayout.closeDrawers();
			}
		});

		mListPlaces.setItemChecked(placeId, true);
		setTitle(mAdapterPlaces.getItem(placeId));
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
}
