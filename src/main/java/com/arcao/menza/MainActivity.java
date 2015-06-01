package com.arcao.menza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

	private NavigationView mNavigationView;
	private DrawerLayout mDrawerLayout;
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
		final ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// prepare drawer
		mNavigationView = (NavigationView) findViewById(R.id.nav_view);
		if (mNavigationView != null) {
			mNavigationView.setNavigationItemSelectedListener(
							new NavigationView.OnNavigationItemSelectedListener() {
								@Override
								public boolean onNavigationItemSelected(MenuItem menuItem) {
									if (menuItem.getGroupId() == R.id.place) {
										onPlaceSelected(menuItem);
									} else {
										switch (menuItem.getItemId()) {
											case R.id.action_settings:
												onSettingsSelected();
												break;
											case R.id.action_feedback:
												onFeedbackSelected();
												break;
										}
									}

									mDrawerLayout.closeDrawers();
									return true;
								}
							});
		}

		// prepare day adapter
		mDayPagerAdapter = new DayPagerAdapter(this, getSupportFragmentManager(), placeId);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDayPagerAdapter);
		mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY);

		PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		pagerTitleStrip.setNonPrimaryAlpha(0.3F); // in percent

		MenuItem menuItem = getMenuItemInGroup(mNavigationView.getMenu(), R.id.place, placeId);
		if (menuItem != null) {
			onPlaceSelected(menuItem);
		}
	}

	@Nullable
	private MenuItem getMenuItemInGroup(@NonNull Menu menu, int groupId, int index) {
		int indexInGroup = 0;

		for (int i = 0; i < menu.size(); i++) {
			MenuItem menuItem = menu.getItem(i);

			if (menuItem.getGroupId() == groupId) {
				if (indexInGroup == index) {
					return menuItem;
				}
				indexInGroup++;
			}
		}

		return null;
	}

	private int getMenuIndexInGroup(@NonNull Menu menu, @NonNull MenuItem menuItem) {
		int index = 0;
		for (int i = 0; i < menu.size(); i++) {
			MenuItem currMenuItem = menu.getItem(i);
			if (currMenuItem.getGroupId() == menuItem.getGroupId()) {
				if (menuItem.equals(currMenuItem))
					return index;
				index++;
			}
		}

		return -1;
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
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
			case R.id.action_today:
				mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY, true);
				return true;
			case R.id.action_info:
				startActivity(new Intent(this, PlacePreviewActivity.class).putExtra(MealPreviewActivity.PARAM_PLACE_ID, placeId));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onSettingsSelected() {
		startActivityForResult(new Intent(this, SettingsActivity.class), 0);
	}

	public void onFeedbackSelected() {
		FeedbackHelper.sendFeedBack(this, R.string.feedback_email, R.string.feedback_subject, R.string.feedback_message, false);
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

	public void onPlaceSelected(MenuItem menuItem) {
		placeId = getMenuIndexInGroup(mNavigationView.getMenu(), menuItem);
		menuItem.setChecked(true);
		setTitle(menuItem.getTitle());
		((TextView)mNavigationView.findViewById(R.id.title)).setText(menuItem.getTitle());
		mDayPagerAdapter.updatePlaceId(placeId);
	}
}
