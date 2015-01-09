package com.arcao.menza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcao.menza.api.MenzaUrlGenerator;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.PrefConstant;
import com.arcao.menza.fragment.MealPreviewFragment;
import com.arcao.menza.fragment.dialog.RatingDialogFragment;
import com.arcao.menza.util.RatingChecker;
import com.arcao.menza.volley.VolleyHelper;

import java.util.Date;

public class MealPreviewActivity extends AbstractPopupActionBarActivity implements RatingDialogFragment.OnRatingChangeListener {
	public static final String PARAM_PLACE_ID = "PLACE_ID";
	public static final String PARAM_DATE = "DATE";
	public static final String PARAM_MEAL = "MEAL";

	private TextView titleTextView;

	private int placeId = 1;
	private Date date;
	private Meal meal;
	private String priceGroup;
	private MealPreviewFragment fragment;
	private RatingChecker ratingChecker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_meal_preview);
		titleTextView = (TextView) findViewById(R.id.title);

		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		priceGroup = mSharedPreferences.getString(PrefConstant.PRICE_GROUP, PrefConstant.PRICE_GROUP__STUDENT);

		placeId = getIntent().getIntExtra(PARAM_PLACE_ID, placeId);
		date = new Date(getIntent().getLongExtra(PARAM_DATE, 0L));
		meal = getIntent().getParcelableExtra(PARAM_MEAL);

		ratingChecker = new RatingChecker(getApplicationContext());

		setTitle(meal.name);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		showAsPopup(R.dimen.popup_width, R.dimen.popup_height);

		if (savedInstanceState == null) {
			fragment = MealPreviewFragment.getInstance(placeId, date, meal);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);

		titleTextView.setText(title);
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

	@Override
	public void onRatingChanged(int rating) {
		Bundle params = new Bundle();
		params.putString("hash", meal.hash);
		params.putInt("vote", rating);

		ratingChecker.addRating(date, meal.hash);

		// show message
		Toast.makeText(getApplicationContext(), R.string.vote_progress, Toast.LENGTH_LONG).show();

		VolleyHelper.addPostRequest(MenzaUrlGenerator.generateRatingUrl(), params, Object.class, createRatingReqSuccessListener(placeId, date, meal), createRatingReqErrorListener(meal));
	}

	private Response.Listener<Object> createRatingReqSuccessListener(final int placeId, final Date date, final Meal meal) {
		return new Response.Listener<Object>() {
			@Override
			public void onResponse(Object response) {
				// invalidate soft cache
				VolleyHelper.invalidateCache(MenzaUrlGenerator.generateDayUrl(placeId, date), false);

				setResult(MainActivity.RESULT_REFRESH);

				// show message
				Toast.makeText(getApplicationContext(), R.string.vote_finished, Toast.LENGTH_LONG).show();
			}
		};
	}


	private Response.ErrorListener createRatingReqErrorListener(final Meal meal) {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("VOLLEY", error.getMessage(), error);

				ratingChecker.removeRating(date, meal.hash);

				// show error message
				Toast.makeText(getApplicationContext(), R.string.vote_failed, Toast.LENGTH_LONG).show();
			}
		};
	}
}

