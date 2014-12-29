package com.arcao.menza.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcao.menza.MealPreviewActivity;
import com.arcao.menza.R;
import com.arcao.menza.adapter.DayMenuRecyclerAdapter;
import com.arcao.menza.api.MenzaUrlGenerator;
import com.arcao.menza.api.data.Meal;
import com.arcao.menza.api.data.Section;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.volley.VolleyHelper;
import com.arcao.menza.widget.decorator.DayMenuDividerItemDecoration;

import java.util.Calendar;
import java.util.Date;

public class DayMenuFragment extends Fragment implements UpdateableFragment, DayMenuRecyclerAdapter.OnItemClickListener {
	public static final String ARG_DAY_ID = "DAY_ID";
	public static final String ARG_PLACE_ID = "PLACE_ID";

	protected int placeId = 0;
	protected Date date = null;

	protected DayMenuRecyclerAdapter adapter;

	protected View progressContainer;
	protected View listContainer;
	protected TextView textEmpty;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		update();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_day_menu, container, false);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

		adapter = new DayMenuRecyclerAdapter(getActivity());
		adapter.setOnItemClickListener(this);

		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.addItemDecoration(new DayMenuDividerItemDecoration(getActivity(), DayMenuDividerItemDecoration.VERTICAL_LIST));

		progressContainer = view.findViewById(R.id.progressContainer);
		listContainer = view.findViewById(R.id.listContainer);
		textEmpty = (TextView) view.findViewById(R.id.textEmpty);

		setEmptyText("");
		setListShown(false);

		return view;
	}

	@Override
	public void update() {
		setListShown(false);

		if (getActivity() != null) {
			adapter.clearItems();
		}

		placeId = getArguments().getInt(ARG_PLACE_ID, 0);
		int dayId = getArguments().getInt(ARG_DAY_ID, 0);

		Log.d("UPDATE", "PlaceId:" + placeId + " DayId: " + dayId);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, dayId - AppConstant.DAY_ID_TODAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		date = cal.getTime();

		VolleyHelper.addGetRequest(MenzaUrlGenerator.generateDayUrl(placeId, date), Section[].class, createDayMenuReqSuccessListener(), createDayMenuReqErrorListener());
	}

	private void setListShown(boolean visible) {
		if (visible) {
			progressContainer.setVisibility(View.GONE);
			listContainer.setVisibility(View.VISIBLE);
		} else {
			progressContainer.setVisibility(View.VISIBLE);
			listContainer.setVisibility(View.GONE);
		}
		textEmpty.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
	}

	private void setEmptyText(CharSequence text) {
		textEmpty.setText(text);
	}

	@Override
	public void onItemClick(Meal item) {
		Intent i = new Intent(getActivity(), MealPreviewActivity.class);
		i.putExtra(MealPreviewActivity.PARAM_PLACE_ID, getArguments().getInt(ARG_PLACE_ID, 0));
		i.putExtra(MealPreviewActivity.PARAM_DATE, date.getTime());
		i.putExtra(MealPreviewActivity.PARAM_MEAL, item);
		getActivity().startActivity(i);
	}

	private Response.Listener<Section[]> createDayMenuReqSuccessListener() {
		return new Response.Listener<Section[]>() {
			@Override
			public void onResponse(Section[] response) {
				if (getActivity() == null)
					return;

				setEmptyText(getResources().getText(R.string.list_empty));
				adapter.fillItems(response);
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

				adapter.clearItems();
				setEmptyText(getResources().getText(R.string.connection_error));
				setListShown(true);
			}
		};
	}
}