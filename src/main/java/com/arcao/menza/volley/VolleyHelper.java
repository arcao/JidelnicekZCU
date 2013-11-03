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
	protected static RequestQueue requestQueue;

	public static void init(Context mContext) {
		requestQueue = Volley.newRequestQueue(mContext);
	}

	public static RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public static <T> void addGetRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		JacksonRequest<T> request = new JacksonRequest<T>(Method.GET, url, clazz, listener, errorListener);

		requestQueue.add(request);
	}

	public static <T> void addPostRequest(String url, final Bundle params, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		JacksonRequest<T> request = new JacksonRequest<T>(Method.POST, url, clazz, listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> paramsMap = new HashMap<String, String>();

				if (params != null) {
					for (String key : params.keySet()) {
						paramsMap.put(key, params.get(key).toString());
					}
				}

				return paramsMap;
			}
		};

		requestQueue.add(request);
	}
}
