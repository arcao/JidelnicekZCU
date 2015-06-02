package com.arcao.menza.fragment.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.arcao.menza.R;
import com.arcao.menza.constant.AppConstant;

public class RatingDialogFragment extends AbstractDialogFragment implements RatingBar.OnRatingBarChangeListener {
	public static final String TAG = "RatingDialogFragment";

	public interface OnRatingChangeListener {
		void onRatingChanged(float rating);
	}

	private OnRatingChangeListener listener;

	public static RatingDialogFragment newInstance() {
		return new RatingDialogFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (OnRatingChangeListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnRatingChangeListener");
		}
	}

	@Override
	public void onDetach() {
		listener = null;
		super.onDetach();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		@SuppressLint("InflateParams")
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rating, null);

		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

		ratingBar.setMax(AppConstant.RATING__MAX);
		ratingBar.setNumStars(AppConstant.RATING__NUM_STARS);
		ratingBar.setOnRatingBarChangeListener(this);

		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.header_rating)
			.setView(view)
			.create();
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		dismiss();

		if (listener != null && fromUser)
			listener.onRatingChanged(rating);
	}
}
