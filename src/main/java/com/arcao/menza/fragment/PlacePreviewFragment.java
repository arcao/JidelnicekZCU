package com.arcao.menza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.arcao.menza.R;
import com.arcao.menza.api.data.Place;

public class PlacePreviewFragment extends Fragment {
	protected static final String PARAM_PLACE = "PLACE";

	public static PlacePreviewFragment getInstance(Place place) {
		PlacePreviewFragment fragment = new PlacePreviewFragment();

		Bundle args = new Bundle();
		args.putParcelable(PARAM_PLACE, place);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.place_preview, container, false);

		Place place = getArguments().getParcelable(PARAM_PLACE);

		((TextView)view.findViewById(R.id.address)).setText(place.address);

		TextView descriptionView = (TextView) view.findViewById(R.id.description);
		descriptionView.setText(Html.fromHtml(place.description));
		descriptionView.setLinksClickable(true);
		descriptionView.setMovementMethod(LinkMovementMethod.getInstance());

		return view;
	}

}
