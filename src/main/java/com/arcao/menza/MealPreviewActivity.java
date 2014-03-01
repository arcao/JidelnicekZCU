package com.arcao.menza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.MealPreviewFragment;

public class MealPreviewActivity extends AbstractPopupActionBarActivity {
	public static final String PARAM_MEAL = MealPreviewFragment.PARAM_MEAL;
	public static final String PARAM_PLACE_ID = "PLACE_ID";

	protected Meal meal;
	protected String priceGroup;
	protected int placeId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		priceGroup = mSharedPreferences.getString(PrefConstant.PRICE_GROUP, PrefConstant.PRICE_GROUP__STUDENT);

		meal = getIntent().getParcelableExtra(PARAM_MEAL);
		placeId = getIntent().getIntExtra(PARAM_PLACE_ID, placeId);

		setContentView(R.layout.activity_fragment);

		showAsPopup(R.dimen.popup_width, R.dimen.popup_height);

		if (savedInstanceState == null) {
			Fragment fragment = MealPreviewFragment.getInstance(meal);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.meal_preview, menu);

		// Set up ShareActionProvider's share intent
		MenuItem shareItem = menu.findItem(R.id.action_share);
		ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
		mShareActionProvider.setShareIntent(getShareIntent());

		return super.onCreateOptionsMenu(menu);
	}

	protected Intent getShareIntent() {
		String place = getResources().getStringArray(R.array.places)[placeId];

		String shareText = getString(R.string.share_text, place, meal.name, (int) getMealPrice(meal));

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, shareText);
		intent.setType("text/plain");

		return intent;
	}

	protected float getMealPrice(Meal meal) {
		switch (priceGroup) {
			case PrefConstant.PRICE_GROUP__STAFF:
				return meal.priceStaff;

			case PrefConstant.PRICE_GROUP__EXTERNAL:
				return meal.priceExternal;

			case PrefConstant.PRICE_GROUP__STUDENT:
			default:
				return meal.priceStudent;

		}
	}

}
