package com.arcao.menza.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.arcao.menza.R;

/**
 * Created by Martin on 19.3.14.
 */
public class ShortcutDialogFragment extends AbstractDialogFragment implements DialogInterface.OnClickListener {
	public static final String TAG = "ShortcutDialogFragment";

	public interface ShortcutDialogListener {
		public void onCreateShortcut(int placeId);
		public void onCancel();
	}

	protected ShortcutDialogListener listener;

	public static ShortcutDialogFragment newInstance() {
		return new ShortcutDialogFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (ShortcutDialogListener) getActivity();
		} catch (ClassCastException e) {
			Log.e(TAG, "Activity must implement ShortcutDialogListener", e);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.create_shortcut);
		builder.setItems(R.array.places, this);
		return builder.create();
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
