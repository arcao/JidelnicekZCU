package com.arcao.menza.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

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
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getArguments().getInt(ARG_TITLE));
		builder.setMessage(getArguments().getInt(ARG_MESSAGE));
		builder.setPositiveButton(R.string.button_ok, null);

		return builder.create();
	}
}
