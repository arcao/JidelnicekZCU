package com.arcao.menza.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class FeedbackHelper {
	private static final String TAG = "FeedbackHelper";

	private static final String FEEDBACK_IMAGE = "screenshot.jpg";
	private static final boolean ALLOW_GMS_FEEDBACK = false; // unable to get feedback from this
	private static int MAX_WIDTH = 600;
	private static int MAX_HEIGHT = 600;


	public static void sendFeedBack(Activity activity, int resEmail, int resSubject, int resMessageText, boolean includeScreensshot) {
		if (!ALLOW_GMS_FEEDBACK || !activity.bindService(new Intent(Intent.ACTION_BUG_REPORT), new FeedBackServiceConnection(activity.getWindow()), Context.BIND_AUTO_CREATE)) {
			String subject = activity.getString(resSubject, getApplicationName(activity), getVersion(activity));

            String email = activity.getString(resEmail);

			// send it old way
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, activity.getString(resMessageText));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			if (includeScreensshot) {
				// try to add screenshot
				try {
					Bitmap bitmap = getScreenshot(activity.getWindow(), MAX_WIDTH, MAX_HEIGHT);
					if (bitmap != null) {
						FileOutputStream fos = getCacheFileOutputStream(activity, FEEDBACK_IMAGE);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
						fos.flush();
						fos.close();
						bitmap.recycle();

						intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getCacheFileName(activity, FEEDBACK_IMAGE)));
					}
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}

			activity.startActivity(createEmailOnlyChooserIntent(activity, intent, null /*"Odeslat zpětnou vazbu pomocí"*/));
		}
	}

	/**
	 * Get a path including file name to save data
	 * @param context	Context
	 * @return	path to file
	 * @throws java.io.IOException If external storage isn't available or writable
	 */
	public static File getCacheFileName(Context context, String filename) throws IOException {
		File cacheFile = context.getFileStreamPath(filename);
		Log.d(TAG, "Cache file for Locus: " + cacheFile.toString());

		return cacheFile;
	}

	/**
	 * Get a OutputFileStream to save data
	 * @param context Context
	 * @return OutputFileStream object for world readable file returned by getCacheFileName method
	 * @throws IOException If I/O error occurs
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	public static FileOutputStream getCacheFileOutputStream(Context context, String filename) throws IOException {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			return context.openFileOutput(filename, Context.MODE_WORLD_READABLE); // file has to be readable for external APP
		} else {
			File file = getCacheFileName(context, filename);
			FileOutputStream fos = new FileOutputStream(file);
			fos.flush(); // create empty file
			file.setReadable(true, false); // file has to be readable for external APP

			return fos;
		}
	}

	protected static Bitmap getScreenshot(Window window, int maxWidth, int maxHeight) {
		try {
			View rootView = window.getDecorView().getRootView();
			rootView.setDrawingCacheEnabled(true);
			Bitmap bitmap = rootView.getDrawingCache();
			if (bitmap != null)
			{
				double height = bitmap.getHeight();
				double width = bitmap.getWidth();
				double ratio = Math.min(maxHeight / width, maxWidth / height);
				return Bitmap.createScaledBitmap(bitmap, (int)Math.round(width * ratio), (int)Math.round(height * ratio), true);
			}
		} catch (Exception e) {
			Log.e("Screenshoter", "Error getting current screenshot: ", e);
		}
		return null;
	}

	public static String getApplicationName(Context context) {
		int stringId = context.getApplicationInfo().labelRes;
		return context.getString(stringId);
	}

	public static String getVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			return "0.0";
		}
	}

    protected static Intent createEmailOnlyChooserIntent(Context context, Intent source, CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@domain.com", null));
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0), chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }

	protected static class FeedBackServiceConnection implements ServiceConnection {
		protected Window mWindow;

		public FeedBackServiceConnection(Window window) {
			this.mWindow = window;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			try {
				Parcel parcel = Parcel.obtain();
				Bitmap bitmap = getScreenshot(mWindow, MAX_WIDTH, MAX_HEIGHT);
				if (bitmap != null) {
					bitmap.writeToParcel(parcel, 0);
				}
				service.transact(IBinder.FIRST_CALL_TRANSACTION, parcel, null, 0);
				parcel.recycle();
			} catch (RemoteException e) {
				Log.e("ServiceConn", e.getMessage(), e);
			}
		}

		public void onServiceDisconnected(ComponentName name) {	}
	}

}
