package com.arcao.menza.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class MenzaApi {
	public static final String TAG = "JidlenicekZCU|MenzaAPI";
	
	public static JSONObject getFoods(int menzaId, Date date) throws DownloadException, FoodNotFoundException {
		try {
			Object foods = new JSONTokener(downloadData(new URL("http://menza.arcao.com/api/get/" + (menzaId + 1) + "/" + String.format("%1$tY%1$tm%1$td", date)))).nextValue();
			if (foods instanceof JSONArray && ((JSONArray)foods).length() == 0)
				throw new FoodNotFoundException();
			return (JSONObject) foods;
		} catch (MalformedURLException e) {
			throw new DownloadException(e);
		} catch (JSONException e) {
			throw new DownloadException(e);
		}
	}
	
	public static void vote(String hash, int vote) throws DownloadException {
		try {
			Object result = new JSONTokener(downloadData(new URL("http://menza.arcao.com/api/vote?hash=" + URLEncoder.encode(hash) + "&vote=" + vote))).nextValue();
			if (!(result instanceof JSONObject) || !((JSONObject) result).getString("state").equals("ok"))
				throw new DownloadException("Vote on " + hash + " with " + vote + " failed.");
		} catch (MalformedURLException e) {
			throw new DownloadException(e);
		} catch (JSONException e) {
			throw new DownloadException(e);
		}
	}
	
	@Deprecated
	public static JSONObject getFoods(int menzaId) throws DownloadException, FoodNotFoundException {
		return getFoods(menzaId, new Date());
	}
	
	protected static String downloadData(URL url) throws DownloadException {
		char[] c = new char[8192];
        int n;
        
        Log.i(TAG + "|downloadData", "Downloading: " + url);
        
        try {
        	HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        	uc.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			uc.setRequestProperty("Accept-Encoding", "gzip");
			
			final String encoding = uc.getContentEncoding();
			InputStream is;

			if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				Log.i(TAG + "|downloadData", "GZIP OK");
				is = new GZIPInputStream(uc.getInputStream());
			} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				Log.i(TAG + "|downloadData", "DEFLATE OK");
				is = new InflaterInputStream(uc.getInputStream(), new Inflater(true));
			} else {
				Log.i(TAG + "|downloadData", "WITHOUT ENCODING");
				is = uc.getInputStream();
			}
		
			InputStreamReader isr = new InputStreamReader(is);
			
	        StringBuilder sb = new StringBuilder();
	        while((n = isr.read(c)) != -1) {
	            sb.append(new String(c, 0, n));
	        }
	        is.close();
	        
	        return sb.toString(); 
        } catch (IOException e) {
        	throw new DownloadException(e);
        }
	}
}

	