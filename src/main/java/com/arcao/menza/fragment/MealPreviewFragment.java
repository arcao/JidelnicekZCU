package com.arcao.menza.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arcao.menza.R;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.fragment.dialog.ErrorDialogFragment;
import com.arcao.menza.fragment.dialog.RatingDialogFragment;
import com.arcao.menza.util.RatingChecker;

import java.util.Date;

public class MealPreviewFragment extends Fragment {
    private static final String PARAM_PLACE_ID = "PLACE_ID";
    private static final String PARAM_MEAL = "MEAL";
    private static final String PARAM_DATE = "DATE";

    private RatingChecker ratingChecker;

    private Date date;
    private Meal meal;

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
        ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_meal_preview, container, false);

        date = new Date(getArguments().getLong(PARAM_DATE));
        meal = getArguments().getParcelable(PARAM_MEAL);

        ((TextView) view.findViewById(R.id.priceStudent)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStudent));
        ((TextView) view.findViewById(R.id.priceStaff)).setText(AppConstant.PRICE_FORMAT.format(meal.priceStaff));
        ((TextView) view.findViewById(R.id.priceExternal)).setText(AppConstant.PRICE_FORMAT.format(meal.priceExternal));

        RatingBar ratingBar = ((RatingBar) view.findViewById(R.id.ratingBar));
        prepareRatingBar(ratingBar, meal);

        prepareAllergens(inflater, (ViewGroup) view.findViewById(R.id.alergens_container), meal);

        // fix for situation when scroll view scroll to first focusable view
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener((v, event) -> {
            v.requestFocusFromTouch();
            return false;
        });

        return view;
    }

    private void prepareAllergens(LayoutInflater inflater, ViewGroup container, Meal meal) {
        if (meal.allergens == null || meal.allergens.length == 0)
            return;

        container.removeAllViews();

        String[] allergenTexts = getResources().getStringArray(R.array.allergens);

        for (int allergenId : meal.allergens) {
            if (allergenId < 1 || allergenId > allergenTexts.length)
                continue;

            View allergenView = inflater.inflate(R.layout.view_alergen_item, container, false);
            ((TextView) allergenView.findViewById(R.id.number)).setText(String.valueOf(allergenId));
            ((TextView) allergenView.findViewById(R.id.text)).setText(String.valueOf(allergenTexts[allergenId]));

            container.addView(allergenView);
        }
    }

    private void prepareRatingBar(RatingBar ratingBar, final Meal meal) {
        ratingBar.setMax(AppConstant.RATING__MAX);
        ratingBar.setNumStars(AppConstant.RATING__NUM_STARS);
        ratingBar.setClickable(true);
        ratingBar.setFocusable(true);
        ratingBar.setFocusableInTouchMode(true);

        if (meal.quality >= 0) {
            ratingBar.setProgress((int) (AppConstant.RATING__STEP_SIZE + meal.quality / AppConstant.RATING__QUANTIFIER));
        }

        ratingBar.setOnClickListener(v -> performVote());

        ratingBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                performVote();
            }
            return true;
        });
    }

    private void performVote() {
        if (ratingChecker.isRated(date, meal.hash)) {
            ErrorDialogFragment.newInstance(R.string.vote_error_title, R.string.vote_already_before).show(getActivity().getFragmentManager(), ErrorDialogFragment.TAG);
            return;
        }

        if (!DateUtils.isToday(date.getTime())) {
            ErrorDialogFragment.newInstance(R.string.vote_error_title, R.string.vote_today_only).show(getActivity().getFragmentManager(), ErrorDialogFragment.TAG);
            return;
        }

        RatingDialogFragment.newInstance().show(getActivity().getFragmentManager(), RatingDialogFragment.TAG);
    }
}
