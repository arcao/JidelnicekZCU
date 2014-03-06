package com.arcao.menza.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by msloup on 6.3.14.
 */
public class FeedbackHelper {
	private static final String TAG = "FeedbackHelper";
	private static final String FEEDBACK_IMAGE = "screenshot.jpg";

	private static int MAX_WIDTH = 600;
	private static int MAX_HEIGHT = 600;


	public static void sendFeedBack(Activity activity) {
		if (!activity.bindService(new Intent(Intent.ACTION_BUG_REPORT), new FeedBackServiceConnection(activity.getWindow()), Context.BIND_AUTO_CREATE)) {
			// send it old way
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/email");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"arcao@arcao.com"});
			intent.putExtra(Intent.EXTRA_SUBJECT, "JídelníčekZČU: Zpětná vazba");
			intent.putExtra(Intent.EXTRA_TEXT, "Váš text...");

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

			activity.startActivity(Intent.createChooser(intent, "Odeslat zpětnou vazbu pomocí"));
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
	public static FileOutputStream getCacheFileOutputStream(Context context, String filename) throws IOException {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return context.openFileOutput(filename, Context.MODE_WORLD_READABLE); // file has to be readable for Locus
		} else {
			File file = getCacheFileName(context, filename);
			FileOutputStream fos = new FileOutputStream(file);
			fos.flush(); // create empty file
			file.setReadable(true, false); // file has to be readable for Locus

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
