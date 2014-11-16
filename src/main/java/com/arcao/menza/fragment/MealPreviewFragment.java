package com.arcao.menza.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.arcao.menza.R;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.fragment.dialog.ErrorDialogFragment;
import com.arcao.menza.fragment.dialog.RatingDialogFragment;
import com.arcao.menza.util.RatingChecker;

import java.lang.ref.WeakReference;
import java.util.Date;

public class MealPreviewFragment extends Fragment {
	protected static final String PARAM_PLACE_ID = "PLACE_ID";
	protected static final String PARAM_MEAL = "MEAL";
	protected static final String PARAM_DATE = "DATE";

	protected RatingChecker ratingChecker;
	protected WeakReference<RatingBar> ratingBarRef;

	protected int placeId;
	protected Date date;
	protected Meal meal;

	public static MealPreviewFragment getInstance(int placeId, Date date, Meal meal) {
		MealPreviewFragment fragment = new MealPreviewFragment();

		Bundle args = new Bundle();
		args.putInt(PARAM_PLACE_ID, placeId);
		args.putLong(PARAM_DATE, date.getTime());
		args.putParcelable(PARAM_MEAL, meal);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		ratingChecker = new RatingChecker(activity.getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_meal_preview, container, false);

		placeId = getArguments().getInt(PARAM_PLACE_ID);
		date = new Date(getArguments().getLong(PARAM_DATE));
		meal = getArguments().getParcelable(PARAM_MEAL);

		((TextView)view.findViewById(R.id.mealName)).setText(meal.name);

		((TextView)view.findViewById(R.id.priceStudent)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStudent));
		((TextView)view.findViewById(R.id.priceStaff)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStaff));
		((TextView)view.findViewById(R.id.priceExternal)).setText(AppConstant.PRICE_FORMAT.format(meal.priceExternal));

		RatingBar ratingBar = ((RatingBar)view.findViewById(R.id.ratingBar));
		prepareRatingBar(ratingBar, meal);

		return view;
	}

	protected void prepareRatingBar(RatingBar ratingBar, final Meal meal) {
		ratingBar.setMax(AppConstant.RATING__MAX);
		ratingBar.setNumStars(AppConstant.RATING__NUM_STARS);
		ratingBar.setClickable(true);
		ratingBar.setFocusable(true);
		ratingBar.setFocusableInTouchMode(true);

		if (meal.quality >= 0) {
			ratingBar.setProgress((int) (AppConstant.RATING__STEP_SIZE + meal.quality / AppConstant.RATING__QUANTIFIER));
		}

		ratingBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				performVote();
			}
		});

		ratingBar.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					performVote();
				}
				return true;
			}
		});
	}

	public void performVote() {
		if (ratingChecker.isRated(date, meal.hash)) {
			ErrorDialogFragment.newInstance(R.string.vote_error_title, R.string.vote_already_before).show(getActivity().getSupportFragmentManager(), ErrorDialogFragment.TAG);
			return;
		}

		if (!DateUtils.isToday(date.getTime())) {
			ErrorDialogFragment.newInstance(R.string.vote_error_title, R.string.vote_today_only).show(getActivity().getSupportFragmentManager(), ErrorDialogFragment.TAG);
			return;
		}

		RatingDialogFragment.newInstance().show(getActivity().getSupportFragmentManager(), RatingDialogFragment.TAG);
	}
}
