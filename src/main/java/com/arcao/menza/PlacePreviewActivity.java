package com.arcao.menza;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcao.menza.api.MenzaUrlGenerator;
import com.arcao.menza.api.data.Place;
import com.arcao.menza.fragment.ErrorFragment;
import com.arcao.menza.fragment.PlacePreviewFragment;
import com.arcao.menza.volley.VolleyHelper;

public class PlacePreviewActivity extends AbstractPopupActionBarActivity {
	public static final String PARAM_PLACE_ID = "PLACE_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int placeId = getIntent().getIntExtra(PARAM_PLACE_ID, 0);

		setTitle(getResources().getStringArray(R.array.places)[placeId]);

		setContentView(R.layout.activity_fragment);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		showAsPopup(R.dimen.popup_width, R.dimen.popup_height);

		if (savedInstanceState == null) {
			VolleyHelper.addGetRequest(MenzaUrlGenerator.generatePlaceUrl(placeId), Place.class, createPlaceReqSuccessListener(), createPlaceReqErrorListener());
		}
	}

	private Response.Listener<Place> createPlaceReqSuccessListener() {
		return new Response.Listener<Place>() {
			@Override
			public void onResponse(Place response) {
				setTitle(response.name);

				Fragment fragment = PlacePreviewFragment.getInstance(response);
				getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
			}
		};
	}

	private Response.ErrorListener createPlaceReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("VOLLEY", error.getMessage(), error);


				if (getSupportFragmentManager().findFragmentById(R.id.fragment) == null) {
					getSupportFragmentManager().beginTransaction().add(R.id.fragment,
									ErrorFragment.newInstance(R.string.connection_error_data)).commit();
				}
			}
		};
	}
}
