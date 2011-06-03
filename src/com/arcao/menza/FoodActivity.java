package com.arcao.menza;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.arcao.menza.adapter.list.BuldingListAdapter;
import com.arcao.menza.adapter.list.FoodListAdapter;
import com.arcao.menza.api.DownloadException;
import com.arcao.menza.api.FoodNotFoundException;
import com.arcao.menza.api.MenzaApi;
import com.arcao.menza.dialog.RatingDialog;
import com.arcao.menza.dialog.RatingDialog.OnRatingListener;
import com.arcao.menza.dto.Food;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodActivity extends Activity {
	private static final String TAG = "JidelnicekZCU|FoodActivity";
	private final static String PREFS_NAME = "JidelnicekZcuPrefs";
		
	private int menzaId;
	private JSONObject foods; 
	private SharedPreferences settings;
	private Resources res;
	private Calendar cal;
	private ListView list;
	private SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyyMMdd");
	private Handler handler;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		handler = new Handler();
		
		settings = getSharedPreferences(PREFS_NAME, 0);
		res = getResources();

		
		Bundle extras = getIntent().getExtras();
		menzaId = extras.getInt("menzaId");
		
		cal = Calendar.getInstance();
		
		TextView title = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        title.setText(BuldingListAdapter.getBuilding(menzaId).getName());
        
        TextView day = (TextView) findViewById(R.id.header).findViewById(R.id.day);
        day.setText(String.format("%1$te.%1$tm", cal));       
        
        if (savedInstanceState != null) {
        	String jsonFoods = savedInstanceState.getString("food");
        	try {
				foods = (JSONObject) new JSONTokener(jsonFoods).nextValue();
			} catch (JSONException e) {
				foods = null;
			}
        }
        
        refresh();        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.food_activity_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.price_source:
	    	goPriceSourceSelect();
	        return true;
	    case R.id.select_date:
	    	goSelectDate(null);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		FoodListAdapter adapter = (FoodListAdapter) list.getAdapter();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		
		menu.setHeaderTitle(adapter.getItem(info.position).getName());
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.food_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.vote:
				vote(info.position);
				return true;
			case R.id.share:
				share(info.position);
				return true;
			default:
				return super.onContextItemSelected(item);
		  }
	}
	
	private void vote(int position) {
		FoodListAdapter adapter = (FoodListAdapter) list.getAdapter();
		
		// check vote
		final String hash = adapter.getItem(position).getHash();
		final String key = ymdFormat.format(cal.getTime()) + "_" + hash;
		if (settings.contains(key)) {
			Toast.makeText(this, res.getString(R.string.already_voted), Toast.LENGTH_LONG).show();
			return;
		}
		
		final RatingDialog dialog = new RatingDialog(this, new OnRatingListener() {
			public void onRatingSelected(DialogInterface dialog, float rating) {
				pd = ProgressDialog.show(FoodActivity.this, null, res.getString(R.string.vote_progress));
				new VoteThread(hash, rating).start();
			}
		});
		dialog.setCancelable(true);
		dialog.setTitle(R.string.vote_menu_item);
		dialog.show();
	}
	
	private void share(int position) {
		FoodListAdapter adapter = (FoodListAdapter) list.getAdapter();
		
		Food food = adapter.getItem(position);
		String shareText = res.getString(R.string.share_text, BuldingListAdapter.getBuilding(menzaId).getName(), food.getName(), food.getPrice());
		
		Intent sendIntent = new Intent(Intent.ACTION_SEND); 
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText); 
        sendIntent.setType("text/plain");

        startActivity(Intent.createChooser(sendIntent, res.getText(R.string.share_chooser_title)));
	}
	
	public void goRefresh(View view) {
		foods = null;
        refresh();
    }
	
    public void goSelectDate(View view) {
    	DatePickerDialog dp = new DatePickerDialog(this, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				cal.set(year, monthOfYear, dayOfMonth);
				foods = null;
				refresh();
			}
		}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    	dp.show();
    }

    private void refresh() {
        pd = ProgressDialog.show(this, null, res.getString(R.string.food_list_loading), false, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				FoodActivity.this.finish();
			}
		});
                
        new Thread() {
            @Override
            public void run() {
                try {
                	if (foods == null)
                		foods = MenzaApi.getFoods(menzaId, cal.getTime());                	
                	
                	handler.post(new Runnable() {
                        public void run() {
                        	setContentView(R.layout.detail);
                        	
                        	TextView title = (TextView) findViewById(R.id.header).findViewById(R.id.title);
                            title.setText(BuldingListAdapter.getBuilding(menzaId).getName());
                            
                            TextView day = (TextView) findViewById(R.id.header).findViewById(R.id.day);
                            day.setText(String.format("%1$te.%1$tm", cal));
                  
                            list = (ListView) findViewById(R.id.list);
                            registerForContextMenu(list);
                            list.setAdapter(new FoodListAdapter(FoodActivity.this, foods));
                            
                            pd.dismiss();
                        }
                    });
                } catch (final FoodNotFoundException e) {
                	Log.e(TAG, e.getMessage(), e);
                	
                	handler.post(new Runnable() {
                        public void run() {
                        	setContentView(R.layout.no_food);
                        	
                        	TextView title = (TextView) findViewById(R.id.header).findViewById(R.id.title);
                            title.setText(BuldingListAdapter.getBuilding(menzaId).getName());
                            
                            TextView day = (TextView) findViewById(R.id.header).findViewById(R.id.day);
                            day.setText(String.format("%1$te.%1$tm", cal));
                            
                        	pd.dismiss();
                        }
                	});
                } catch (final Exception e) {
                	Log.e(TAG, e.getMessage(), e);

                	handler.post(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(FoodActivity.this, res.getString(R.string.food_list_loading_error), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        }.start();
    }
    
    @Override
    protected void onSaveInstanceState (Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	if (foods != null) {
    		outState.putString("food", foods.toString());
    	}
    }
    
    protected void goPriceSourceSelect() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.price_source);
    	
    	builder.setItems(R.array.price_source_values, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	        setPriceSource(item);
    	        refresh();
    	    }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    protected void setPriceSource(int source) {
    	Editor editor = settings.edit();
    	editor.putInt("PriceSource", source);
    	editor.commit();
    }
    
    public int getPriceSource() {
    	return settings.getInt("PriceSource", 0);
    }
    
    private class VoteThread extends Thread {
    	private final String hash;
    	private final float rating;
    	
    	public VoteThread(String hash, float rating) {
			this.hash = hash;
			this.rating = rating;
		}
    	
    	@Override
    	public void run() {
    		try {
				MenzaApi.vote(hash, (int) rating);
				final String key = ymdFormat.format(cal.getTime()) + "_" + hash;
				
				Editor edit = settings.edit();
				edit.putBoolean(key, true);
				edit.commit();
						
				handler.post(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(FoodActivity.this, res.getString(R.string.vote_finished), Toast.LENGTH_LONG).show();
						foods = null;
				        refresh();
					}
				});
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
				
				handler.post(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(FoodActivity.this, res.getString(R.string.vote_failed), Toast.LENGTH_LONG).show();
					}
				});
			}
    	}
    }
}
