package com.arcao.menza.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Method;

/**
 * @author Marc Holder Kluver (marchold), Aidan Follestad (afollestad)
 */
public class MaterialListPreference extends ListPreference {

	private Context context;
	private AlertDialog mDialog;

	public MaterialListPreference(Context context) {
		super(context);
		init(context);
	}

	public MaterialListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)
			setWidgetLayoutResource(0);
	}

	@Override
	public Dialog getDialog() {
		return mDialog;
	}

	@Override
	protected void showDialog(Bundle state) {
		if (getEntries() == null || getEntryValues() == null) {
			throw new IllegalStateException(
							"ListPreference requires an entries array and an entryValues array.");
		}

		int preselect = findIndexOfValue(getValue());
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
						.setTitle(getDialogTitle())
						.setMessage(getDialogMessage())
						.setIcon(getDialogIcon())
						.setNegativeButton(getNegativeButtonText(), this);

		builder.setSingleChoiceItems(getEntries(), preselect,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								MaterialListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
								if (which >= 0 && getEntryValues() != null) {
									String value = getEntryValues()[which].toString();
									if (callChangeListener(value) && isPersistent())
										setValue(value);
								}
								dialog.dismiss();
							}
						});

		builder.setPositiveButton(null, null);

		final View contentView = onCreateDialogView();
		if (contentView != null) {
			onBindDialogView(contentView);
			builder.setView(contentView);
		} else {
			builder.setMessage(getDialogMessage());
		}

		try {
			PreferenceManager pm = getPreferenceManager();
			Method method = pm.getClass().getDeclaredMethod(
							"registerOnActivityDestroyListener",
							PreferenceManager.OnActivityDestroyListener.class);
			method.setAccessible(true);
			method.invoke(pm, this);
		} catch (Exception ignored) {
		}

		mDialog = builder.create();
		if (state != null)
			mDialog.onRestoreInstanceState(state);
		mDialog.show();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		try {
			PreferenceManager pm = getPreferenceManager();
			Method method = pm.getClass().getDeclaredMethod(
							"unregisterOnActivityDestroyListener",
							PreferenceManager.OnActivityDestroyListener.class);
			method.setAccessible(true);
			method.invoke(pm, this);
		} catch (Exception ignored) {
		}
	}

	@Override
	public void onActivityDestroy() {
		super.onActivityDestroy();
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

	@Override
	public void setValue(String value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			super.setValue(value);
		} else {
			String oldValue = getValue();
			super.setValue(value);
			if (!TextUtils.equals(value, oldValue))
				notifyChanged();
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		Dialog dialog = getDialog();
		if (dialog == null || !dialog.isShowing()) {
			return superState;
		}

		final SavedState myState = new SavedState(superState);
		myState.isDialogShowing = true;
		myState.dialogBundle = dialog.onSaveInstanceState();
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		final SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		if (myState.isDialogShowing) {
			showDialog(myState.dialogBundle);
		}
	}

	// From DialogPreference
	private static class SavedState extends BaseSavedState {
		boolean isDialogShowing;
		Bundle dialogBundle;

		public SavedState(Parcel source) {
			super(source);
			isDialogShowing = source.readInt() == 1;
			dialogBundle = source.readBundle();
		}

		@Override
		public void writeToParcel(@NonNull Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(isDialogShowing ? 1 : 0);
			dest.writeBundle(dialogBundle);
		}

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public static final Parcelable.Creator<SavedState> CREATOR =
						new Parcelable.Creator<SavedState>() {
							public SavedState createFromParcel(Parcel in) {
								return new SavedState(in);
							}

							public SavedState[] newArray(int size) {
								return new SavedState[size];
							}
						};
	}
}