package com.arcao.menza.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JacksonRequest <T> extends Request<T> {
	private final Class<T> mClazz;
	private final Listener<T> mListener;


	public JacksonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.mClazz = clazz;
		this.mListener = listener;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}


	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			return Response.success(JsonMapper.INSTANCE.mapper().readValue(response.data, mClazz), HttpHeaderParser.parseCacheHeaders(response));
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
}