package com.arcao.menza;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {
	private static final String TAG = "JidelnicekZCU|Main";
	
	private ListView list; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(new BuldingListAdapter(this));
        
        TextView day = (TextView) findViewById(R.id.header).findViewById(R.id.day);
        System.out.println(String.format("%1$te.%1$tm", new Date()));
        day.setText(String.format("%1$te.%1$tm", new Date()));
        
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view, int i, long _id) {
                final Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                intent.putExtra("menzaId", (int) _id);
                Log.d(TAG, "MenzaId:" + _id);
                startActivity(intent);
            }
        }); 
    }
    
    public void goRefresh(View view) {
    }
    
    public void goSelectDate(View view) {
    }
}