package com.arcao.menza.volley;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyHelper {
	private static RequestQueue requestQueue;

	public static void init(Context mContext) {
		requestQueue = Volley.newRequestQueue(mContext);
	}

	public static <T> void addGetRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		requestQueue.add(createGetRequest(url, clazz, listener, errorListener));
	}

	private static <T> JacksonRequest<T> createGetRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		return new JacksonRequest<>(Method.GET, url, clazz, listener, errorListener);
	}

	public static <T> void addPostRequest(String url, final Bundle params, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		JacksonRequest<T> request = new JacksonRequest<T>(Method.POST, url, clazz, listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> paramsMap = new HashMap<>();

				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value != null)
							paramsMap.put(key, String.valueOf(value));
					}
				}

				return paramsMap;
			}
		};

		requestQueue.add(request);
	}

	/**
	 * Invalidates an entry in the cache.
	 * @param url Url to invalidate
	 * @param fullExpire True to fully expire the entry, false to soft expire
	 */
	public static void invalidateCache(String url, boolean fullExpire) {
		requestQueue.getCache().invalidate(url, fullExpire);
	}
}
