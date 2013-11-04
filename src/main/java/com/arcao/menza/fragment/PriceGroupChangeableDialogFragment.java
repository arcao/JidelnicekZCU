package com.arcao.menza.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.arcao.menza.R;

/**
 * Created by msloup on 4.11.13.
 */
public class PriceGroupChangeableDialogFragment extends DialogFragment {
	public static final String TAG = "PriceGroupDialog";

	public static PriceGroupChangeableDialogFragment newInstance() {
		return new PriceGroupChangeableDialogFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.pref_price_group_title)
				.setMessage(R.string.price_group_changeable)
				.setPositiveButton(R.string.button_continue, null)
				.create();
	}
}
