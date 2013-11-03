package com.arcao.menza.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcao.menza.R;
import com.arcao.menza.adapter.DayMenuAdapter;
import com.arcao.menza.api.MenzaUrlGenerator;
import com.arcao.menza.api.data.Section;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.volley.VolleyHelper;

import java.util.Calendar;
import java.util.Date;

public class DayMenuListFragment extends ListFragment implements UpdateableFragment {
	public static final String ARG_DAY_ID = "DAY_ID";
	public static final String ARG_PLACE_ID = "PLACE_ID";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// set correct padding left / right for list view
		int horizontalPadding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		int verticalPadding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);

		getListView().setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
		getListView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

		update();
	}

	@Override
	public void update() {
		setListShown(false);

		if (getActivity() != null) {
			setListAdapter(new DayMenuAdapter(getActivity(), new Section[0]));
		}

		int placeId = getArguments().getInt(ARG_PLACE_ID, 0) + 1;
		int dayId = getArguments().getInt(ARG_DAY_ID, 0);

		Log.d("UPDATE", "PlaceId:" + placeId + " DayId: " + dayId);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, dayId - AppConstant.DAY_ID_TODAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date date = cal.getTime();

		VolleyHelper.addGetRequest(MenzaUrlGenerator.generateDayUrl(placeId, date), Section[].class, createDayMenuReqSuccessListener(), createDayMenuReqErrorListener());
	}

	private Response.Listener<Section[]> createDayMenuReqSuccessListener() {
		return new Response.Listener<Section[]>() {
			@Override
			public void onResponse(Section[] response) {
				if (getActivity() == null)
					return;

				setEmptyText(getResources().getText(R.string.list_empty));

				setListAdapter(new DayMenuAdapter(getActivity(), response));
				setListShown(true);
			}
		};
	}


	private Response.ErrorListener createDayMenuReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (getActivity() == null)
					return;

				Log.e("VOLLEY", error.getMessage(), error);

				setEmptyText(getResources().getText(R.string.connection_error));
				setListShown(true);
			}
		};
	}
}
