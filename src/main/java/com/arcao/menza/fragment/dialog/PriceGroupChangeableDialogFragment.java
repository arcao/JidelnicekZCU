package com.arcao.menza.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.arcao.menza.R;

public class PriceGroupChangeableDialogFragment extends DialogFragment {
    public static final String TAG = "PriceGroupDialog";

    public static PriceGroupChangeableDialogFragment newInstance() {
        return new PriceGroupChangeableDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pref_price_group_title)
                .setMessage(R.string.price_group_changeable)
                .setPositiveButton(R.string.button_continue, null)
                .create();
    }
}
