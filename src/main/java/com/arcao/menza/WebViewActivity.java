package com.arcao.menza;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebViewActivity extends AbstractBaseActivity {
	public static final String PARAM_RAW_RESOURCE = "RAW_RESOURCE";
	public static final String PARAM_TITLE = "TITLE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_webview);

		setTitle(getIntent().getIntExtra(PARAM_TITLE, 0));

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		WebView webView = (WebView) findViewById(R.id.content);
		webView.setLongClickable(false);
		webView.setOnLongClickListener(v -> true);

		try
		{
			webView.loadDataWithBaseURL("file:///android_asset/", getHtmlPageData(getIntent().getIntExtra(PARAM_RAW_RESOURCE, 0)), "text/html", "UTF-8", null);
		} catch (IOException localIOException) {
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getHtmlPageData(int res) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(res), "UTF-8"));

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
	}
}