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

		setContentView(R.layout.activity_fragment);

		showAsPopup(R.dimen.popup_width, R.dimen.popup_height);

		if (savedInstanceState == null) {
			Meal meal = getIntent().getParcelableExtra(PARAM_MEAL);
			Fragment fragment = MealPreviewFragment.getInstance(meal);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
		}
	}
}
