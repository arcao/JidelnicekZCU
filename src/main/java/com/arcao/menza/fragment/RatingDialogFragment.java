package com.arcao.menza.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import com.arcao.menza.R;

public class RatingDialogFragment extends AbstractDialogFragment implements RatingBar.OnRatingBarChangeListener {
	public static final String TAG = "RatingDialogFragment";

	protected RatingBar.OnRatingBarChangeListener listener;

	public static RatingDialogFragment newInstance() {
		return new RatingDialogFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (RatingBar.OnRatingBarChangeListener) getActivity();
		} catch (ClassCastException e) {
			Log.e(TAG, "Activity must implement RatingBar.OnRatingBarChangeListener.");
		}
	}

	@Override
	public void onDetach() {
		listener = null;
		super.onDetach();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rating, null);

		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
		ratingBar.setOnRatingBarChangeListener(this);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.header_rating);
		builder.setView(view);

		return builder.create();
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		dismiss();
		if (listener != null)
			listener.onRatingChanged(ratingBar, rating, fromUser);
	}
}
