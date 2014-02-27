package com.arcao.menza;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.fragment.MealPreviewFragment;

/**
 * Created by msloup on 26.2.14.
 */
public class MealPreviewActivity extends AbstractPopupActionBarActivity {
	public static final String PARAM_MEAL = MealPreviewFragment.PARAM_MEAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showAsPopup(600, 400);

		Meal meal = getIntent().getParcelableExtra(PARAM_MEAL);

		setTitle(meal.name);

		if (savedInstanceState == null) {
			Fragment fragment = MealPreviewFragment.getInstance(meal);
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
		}
	}
}
