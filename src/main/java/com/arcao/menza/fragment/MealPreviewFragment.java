package com.arcao.menza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arcao.menza.R;
import com.arcao.menza.api.data.Meal;

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
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.meal_preview, container, false);

		Meal meal = getArguments().getParcelable(PARAM_MEAL);


		return view;
	}
}
