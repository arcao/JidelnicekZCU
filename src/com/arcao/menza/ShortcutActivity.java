package com.arcao.menza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.arcao.menza.dto.Building;

public class ShortcutActivity extends Activity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.create_shortcut);
		builder.setItems(Building.getBuildingNames(), this);
		builder.setOnCancelListener(this);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void onClick(DialogInterface dialog, int which) {
		// create shortcut
		
		Intent shortcutIntent = new Intent(this, FoodActivity.class);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcutIntent.putExtra("menzaId", which);
			 
		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, Building.getBuilding(which).getName());
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
			 
		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		setResult(RESULT_OK, addIntent);
		finish();
	}

	public void onCancel(DialogInterface dialog) {
		finish();		
	}
}
