package com.arcao.menza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arcao.menza.R;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.AppConstant;

/**
 * Created by msloup on 26.2.14.
 */
public class MealPreviewFragment extends Fragment {
	public static final String PARAM_MEAL = "MEAL";

	public static MealPreviewFragment getInstance(Meal meal) {
		MealPreviewFragment fragment = new MealPreviewFragment();

		Bundle args = new Bundle();
		args.putParcelable(PARAM_MEAL, meal);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.meal_preview, container, false);

		Meal meal = getArguments().getParcelable(PARAM_MEAL);

		((TextView)view.findViewById(R.id.mealName)).setText(meal.name);

		((TextView)view.findViewById(R.id.priceStudent)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStudent));
		((TextView)view.findViewById(R.id.priceStaff)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStaff));
		((TextView)view.findViewById(R.id.priceExternal)).setText(AppConstant.PRICE_FORMAT.format(meal.priceExternal));

		RatingBar ratingBar = ((RatingBar)view.findViewById(R.id.ratingBar));
		ratingBar.setMax(100);

		if (meal.quality > 0) {
			ratingBar.setProgress((int) (20F + 80F * (meal.quality / 100F)));
		}

		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if (!fromUser)
					return;


			}
		});

		return view;
	}
}
