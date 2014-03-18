package com.arcao.menza.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import com.arcao.menza.R;

public class ErrorDialogFragment extends AbstractDialogFragment {
	public static final String TAG = "ErrorDialogFragment";

	protected static String ARG_TITLE = "TITLE";
	protected static String ARG_MESSAGE = "MESSAGE";

	public static ErrorDialogFragment newInstance(int resTitle, int resMessage) {
		ErrorDialogFragment fragment = new ErrorDialogFragment();

		Bundle args = new Bundle();
		args.putInt(ARG_TITLE, resTitle);
		args.putInt(ARG_MESSAGE, resMessage);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getArguments().getInt(ARG_TITLE));
		builder.setMessage(getArguments().getInt(ARG_MESSAGE));
		builder.setPositiveButton(R.string.button_ok, null);

		return builder.create();
	}
}
