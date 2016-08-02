package com.arcao.menza;

import android.content.Intent;

import com.arcao.menza.fragment.dialog.ShortcutDialogFragment;

public class ShortcutActivity extends AbstractBaseActivity implements ShortcutDialogFragment.ShortcutDialogListener {
	private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();

		if (getSupportFragmentManager().findFragmentByTag(ShortcutDialogFragment.TAG) == null) {
			ShortcutDialogFragment.newInstance().show(getFragmentManager(), ShortcutDialogFragment.TAG);
		}
	}

	@Override
	public void onCreateShortcut(int placeId) {
		Intent shortcutIntent = new Intent(this, MainActivity.class);
		shortcutIntent.putExtra(MainActivity.PARAM_PLACE_ID, placeId);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Intent installIntent = new Intent(ACTION_INSTALL_SHORTCUT);
		installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getStringArray(R.array.places)[placeId]);
		installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));

		setResult(RESULT_OK, installIntent);
		finish();
	}

	@Override
	public void onCancel() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
