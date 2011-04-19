package com.arcao.menza;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodActivity extends Activity {
	private final static String PREFS_NAME = "JidelnicekZcuPrefs";
		
	private int menzaId;
	private JSONObject foods; 
	private SharedPreferences settings;
	private Resources res;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
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
	
	public void goRefresh(View view) {
		foods = null;
        refresh();
    }
	
    public void goSelectDate(View view) {
    	DatePickerDialog dp = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				cal.set(year, monthOfYear, dayOfMonth);
				foods = null;
				refresh();
			}
		}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    	dp.show();
    }

    private void refresh() {
        final ProgressDialog pd = ProgressDialog.show(this, null, res.getString(R.string.food_list_loading), false, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				FoodActivity.this.finish();
			}
		});
        final Handler handler = new Handler();
        
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
                            
                            ListView list = (ListView) findViewById(R.id.list);
                            list.setAdapter(new FoodListAdapter(FoodActivity.this, foods));
                            
                            pd.dismiss();
                        }
                    });
                } catch (final FoodNotFoundException e) {
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
                } catch (final Exception ex) {
                    handler.post(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            ex.printStackTrace();
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
}
