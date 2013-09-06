package com.arcao.menza.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;

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
    public static final String ARG_MENZA_ID = "MENZA_ID";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getResources().getText(R.string.list_empty));

        update();
    }

    @Override
	public void update() {
		setListShown(false);


		int menzaId = getArguments().getInt(ARG_MENZA_ID, 0) + 1;
		int dayId = getArguments().getInt(ARG_DAY_ID, 0);

        Log.d("UPDATE", "MenzaId:" + menzaId + " DayId: " + dayId);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, dayId - AppConstant.DAY_ID_TODAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date date = cal.getTime();

		VolleyHelper.addGetRequest(MenzaUrlGenerator.generateDayUrl(menzaId, date), Section[].class, createDayMenuReqSuccessListener(), createDayMenuReqErrorListener());
	}

	private Response.Listener<Section[]> createDayMenuReqSuccessListener() {
		return new Response.Listener<Section[]>() {
			@Override
			public void onResponse(Section[] response) {
                if (getActivity() == null)
                    return;

				setListAdapter(new DayMenuAdapter(getActivity(), response));

				setListShown(true);
			}
		};
	}


	private Response.ErrorListener createDayMenuReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.getMessage(), error);

				setListShown(true);
                Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG).show();
			}
		};
	}
}
