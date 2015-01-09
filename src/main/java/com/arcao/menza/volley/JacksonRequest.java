package com.arcao.menza.volley;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.arcao.menza.constant.AppConstant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JacksonRequest<T> extends Request<T> {
	private final Class<T> mClazz;
	private final Listener<T> mListener;


	public JacksonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mClazz = clazz;
		this.mListener = listener;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<>();
		String credentials = String.format("%s:%s", AppConstant.CONSUMER_KEY, AppConstant.CONSUMER_SECRET);
		String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
		headers.put("Authorization", auth);
		return headers;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			Cache.Entry cacheEntry;

			if (getMethod() == Method.GET) {
				cacheEntry = parseIgnoreCacheHeaders(response);
			} else {
				cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
			}

			return Response.success(JsonMapper.INSTANCE.mapper().readValue(response.data, mClazz), cacheEntry);
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonMappingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonParseException e) {
			return Response.error(new ParseError(e));
		} catch (IOException e) {
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
	 * Cache-control headers are ignored. SoftTtl == 30 min, ttl == 24 hours.
	 *
	 * @param response The network response to parse headers from
	 * @return a cache entry for the given response, or null if the response is not cacheable.
	 */
	private static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
		long now = System.currentTimeMillis();

		Map<String, String> headers = response.headers;
		long serverDate = 0;
		String serverEtag = null;
		String headerValue;

		headerValue = headers.get("Date");
		if (headerValue != null) {
			serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
		}

		serverEtag = headers.get("ETag");

		final long cacheHitButRefreshed = 30 * 60 * 1000; // in 30 minutes cache will be hit, but also refreshed on background
		final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
		final long softExpire = now + cacheHitButRefreshed;
		final long ttl = now + cacheExpired;

		Cache.Entry entry = new Cache.Entry();
		entry.data = response.data;
		entry.etag = serverEtag;
		entry.softTtl = softExpire;
		entry.ttl = ttl;
		entry.serverDate = serverDate;
		entry.responseHeaders = headers;

		return entry;
	}
}