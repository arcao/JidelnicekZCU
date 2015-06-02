package com.arcao.menza.fragment.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.arcao.menza.R;

public class ShortcutDialogFragment extends AbstractDialogFragment implements DialogInterface.OnClickListener {
	public static final String TAG = "ShortcutDialogFragment";

	public interface ShortcutDialogListener {
		void onCreateShortcut(int placeId);
		void onCancel();
	}

	private ShortcutDialogListener listener;

	public static ShortcutDialogFragment newInstance() {
		return new ShortcutDialogFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (ShortcutDialogListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement ShortcutDialogListener");
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.create_shortcut)
			.setItems(R.array.places, this)
			.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		listener.onCreateShortcut(which);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);

		listener.onCancel();
	}
}
