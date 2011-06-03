package com.arcao.menza.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.arcao.menza.R;

public class RatingDialog extends AlertDialog implements OnRatingBarChangeListener {

	private RatingBar ratingBar = null;
	private OnRatingListener listener;
	
	private int numStars = 5;
	private float rating = 0;
	private float stepSize = 1;
	
	public interface OnRatingListener {
		void onRatingSelected(DialogInterface dialog, float rating);
	}
    	
	public RatingDialog(Context context, OnRatingListener listener) {
		super(context);
		this.listener = listener;
		
		setIcon(R.drawable.ic_dialog_star_half);
		
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.rating_dialog, null);
		setView(layout);
		
		// configure rating bar
        ratingBar = (RatingBar) layout.findViewById(R.id.rating_dialog_rating_bar);
        ratingBar.setRating(rating);
        ratingBar.setNumStars(numStars);
        ratingBar.setStepSize(stepSize);
        ratingBar.setOnRatingBarChangeListener(this);
	}

	
	public void setOnRatingListener(OnRatingListener listener) {
		this.listener = listener;
	}

	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		this.rating = rating; 
		
		if (listener != null)
			listener.onRatingSelected(this, rating);
		dismiss();
	}
	
	public void setRating(float rating) {
		this.rating = rating;
		if (ratingBar != null)
			ratingBar.setRating(rating);
	}
	
	public float getRating() {
		return rating;
	}
	
	public void setNumStars(int numStars) {
		this.numStars = numStars;
		if (ratingBar != null)
			ratingBar.setNumStars(numStars);
	}
	
	public int getNumStars() {
		return numStars;
	}
	
	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
		if (ratingBar != null)
			ratingBar.setStepSize(stepSize);
	}
	
	public float getStepSize() {
		return stepSize;
	}
}
