package com.arcao.menza.fragment.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
	private float newRating = -1F;

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

		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setTitle(R.string.header_rating)
			.setView(view)
			.setNegativeButton(R.string.button_cancel, null)
			.setPositiveButton(R.string.button_send, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (listener != null && newRating > 0)
						listener.onRatingChanged(newRating);
				}
			})
			.create();

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface di) {
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
			}
		});

		return dialog;
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		if (!fromUser)
			return;

		newRating = rating;
		((AlertDialog)getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
	}
}
