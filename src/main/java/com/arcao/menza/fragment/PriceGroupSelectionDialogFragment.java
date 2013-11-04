package com.arcao.menza.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.arcao.menza.R;

import java.lang.ref.WeakReference;

/**
 * Created by msloup on 4.11.13.
 */
public class PriceGroupSelectionDialogFragment extends DialogFragment {
	public static final String TAG = "PriceGroupDialog";

	public interface OnPriceGroupSelectedListener {
		void onPriceGroupSelected(String priceGroup);
	}

	protected WeakReference<OnPriceGroupSelectedListener> priceGroupSelectedListenerRef;

	public static PriceGroupSelectionDialogFragment newInstance() {
		return new PriceGroupSelectionDialogFragment();
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			priceGroupSelectedListenerRef = new WeakReference<>((OnPriceGroupSelectedListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnPriceGroupSelectedListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.pref_price_group_title)
			.setItems(R.array.pref_price_group_entries, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					OnPriceGroupSelectedListener listener = priceGroupSelectedListenerRef.get();
					if (listener != null) {
						listener.onPriceGroupSelected(getResources().getStringArray(R.array.pref_price_group_values)[which]);
					}
				}
			})
			.create();
	}
}
