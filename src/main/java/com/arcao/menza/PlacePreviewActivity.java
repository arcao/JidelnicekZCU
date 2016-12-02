package com.arcao.menza;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcao.menza.api.MenzaUrlGenerator;
import com.arcao.menza.api.data.Place;
import com.arcao.menza.fragment.ErrorFragment;
import com.arcao.menza.fragment.LoadingFragment;
import com.arcao.menza.fragment.PlacePreviewFragment;
import com.arcao.menza.volley.VolleyHelper;

public class PlacePreviewActivity extends AbstractBaseActivity {
	private static final String PARAM_PLACE_ID = "PLACE_ID";
	private static final String STATE_TITLE = "TITLE";
	private static final String STATE_SUBTITLE = "SUBTITLE";

	private TextView titleTextView;
	private TextView subTitleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_place_preview);

		getWindow().setBackgroundDrawable(null);

		titleTextView = (TextView) findViewById(R.id.title);
		subTitleTextView = (TextView) findViewById(R.id.subTitle);

		int placeId = getIntent().getIntExtra(PARAM_PLACE_ID, 0);
		setTitle(getResources().getStringArray(R.array.places)[placeId]);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
		}

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().replace(R.id.fragment, LoadingFragment.newInstance()).commitAllowingStateLoss();
			VolleyHelper.addGetRequest(MenzaUrlGenerator.generatePlaceUrl(placeId), Place.class, createPlaceReqSuccessListener(), createPlaceReqErrorListener());
		} else {
			// reset the title and subtitle
			setTitle(savedInstanceState.getCharSequence(STATE_TITLE));
			setSubTitle(savedInstanceState.getCharSequence(STATE_SUBTITLE));
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putCharSequence(STATE_TITLE, titleTextView.getText());
		outState.putCharSequence(STATE_SUBTITLE, subTitleTextView.getText());
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);

		titleTextView.setText(title);
	}

	private void setSubTitle(CharSequence subTitle) {
		subTitleTextView.setText(subTitle);
		subTitleTextView.setVisibility(View.VISIBLE);
	}

	private Response.Listener<Place> createPlaceReqSuccessListener() {
		return new Response.Listener<Place>() {
			@Override
			public void onResponse(Place response) {
				setTitle(response.name);
				setSubTitle(response.address);

				getFragmentManager().beginTransaction().replace(R.id.fragment,
						PlacePreviewFragment.getInstance(response)).commitAllowingStateLoss();
			}
		};
	}

	private Response.ErrorListener createPlaceReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("VOLLEY", error.getMessage(), error);


				getFragmentManager().beginTransaction().replace(R.id.fragment,
						ErrorFragment.newInstance(R.string.connection_error_data)).commitAllowingStateLoss();
			}
		};
	}
}
