package com.arcao.menza.fragment.dialog;

import android.support.v4.app.DialogFragment;

public class AbstractDialogFragment extends DialogFragment {
	// This is to work around what is apparently a bug. If you don't have it
	// here the dialog will be dismissed on rotation, so tell it not to dismiss.
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);

		super.onDestroyView();
	}

	public boolean isShowing() {
		return getDialog() != null && getDialog().isShowing();
	}
}
