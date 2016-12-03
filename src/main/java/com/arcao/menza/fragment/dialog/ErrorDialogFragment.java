package com.arcao.menza.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.arcao.menza.R;

public class ErrorDialogFragment extends AbstractDialogFragment {
    public static final String TAG = "ErrorDialogFragment";

    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_MESSAGE = "MESSAGE";

    public static ErrorDialogFragment newInstance(int resTitle, int resMessage) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, resTitle);
        args.putInt(ARG_MESSAGE, resMessage);
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getInt(ARG_TITLE))
                .setMessage(getArguments().getInt(ARG_MESSAGE))
                .setPositiveButton(R.string.button_ok, null)
                .create();
    }
}
