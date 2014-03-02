package com.arcao.menza.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcao.menza.R;
import com.arcao.menza.api.MenzaUrlGenerator;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.util.RatingChecker;
import com.arcao.menza.volley.VolleyHelper;

import java.lang.ref.WeakReference;
import java.util.Date;

public class MealPreviewFragment extends Fragment {
	public static final String PARAM_PLACE_ID = "PLACE_ID";
	public static final String PARAM_MEAL = "MEAL";
	public static final String PARAM_DATE = "DATE";

	protected WeakReference<RatingChecker> ratingCheckerRef;
	protected WeakReference<RatingBar> ratingBarRef;

	public static MealPreviewFragment getInstance(Meal meal) {
		MealPreviewFragment fragment = new MealPreviewFragment();

		Bundle args = new Bundle();
		args.putParcelable(PARAM_MEAL, meal);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		ratingCheckerRef = new WeakReference<>(new RatingChecker(activity));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.meal_preview, container, false);

		int placeId = getArguments().getInt(PARAM_PLACE_ID);
		Date date = new Date(getArguments().getLong(PARAM_DATE));
		Meal meal = getArguments().getParcelable(PARAM_MEAL);

		((TextView)view.findViewById(R.id.mealName)).setText(meal.name);

		((TextView)view.findViewById(R.id.priceStudent)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStudent));
		((TextView)view.findViewById(R.id.priceStaff)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStaff));
		((TextView)view.findViewById(R.id.priceExternal)).setText(AppConstant.PRICE_FORMAT.format(meal.priceExternal));

		RatingBar ratingBar = ((RatingBar)view.findViewById(R.id.ratingBar));
		ratingBarRef = new WeakReference<>(ratingBar);

		ratingBar.setMax(100);

		prepareRatingBar(ratingBar, placeId, date, meal);

		return view;
	}

	protected void prepareRatingBar(RatingBar ratingBar, final int placeId, final Date date, final Meal meal) {
		if (meal.quality >= 0) {
			ratingBar.setProgress((int) meal.quality);
		}

		RatingChecker ratingChecker = ratingCheckerRef.get();

		if (ratingChecker != null) {
			ratingBar.setIsIndicator(ratingChecker.isRated(date, meal.hash));
		}

		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if (!fromUser)
					return;

				Bundle params = new Bundle();
				params.putString("hash", meal.hash);
				params.putInt("vote", (int) rating);

				ratingBar.setIsIndicator(true);

				VolleyHelper.addPostRequest(MenzaUrlGenerator.generateRatingUrl(), params, Object.class, createRatingReqSuccessListener(placeId, date, meal), createRatingReqErrorListener(meal));
			}
		});

	}

	private Response.Listener<Object> createRatingReqSuccessListener(final int placeId, final Date date, final Meal meal) {
		return new Response.Listener<Object>() {
			@Override
			public void onResponse(Object response) {
				if (getActivity() == null)
					return;

				RatingChecker ratingChecker = ratingCheckerRef.get();
				if (ratingChecker != null) {
					ratingChecker.addRating(date, meal.hash);
				}

				// invalidate soft cache
				VolleyHelper.invalidateCache(MenzaUrlGenerator.generateDayUrl(placeId, date), false);

				// show message
				Toast.makeText(getActivity(), R.string.vote_finished, Toast.LENGTH_LONG).show();
			}
		};
	}


	private Response.ErrorListener createRatingReqErrorListener(final Meal meal) {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (getActivity() == null)
					return;

				Log.e("VOLLEY", error.getMessage(), error);

				RatingBar ratingBar = ratingBarRef.get();
				if (ratingBar != null) {
					ratingBar.setIsIndicator(false);
					ratingBar.setProgress((int) meal.quality);
				}

				// show error message
				Toast.makeText(getActivity(), R.string.vote_failed, Toast.LENGTH_LONG).show();
			}
		};
	}

}
