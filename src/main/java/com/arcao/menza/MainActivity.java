package com.arcao.menza;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.arcao.menza.adapter.DayPagerAdapter;
import com.arcao.menza.constant.AppConstant;

public class MainActivity extends ActionBarActivity {
    private DayPagerAdapter mDayPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mDayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDayPagerAdapter);
        mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY);

        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                mDayPagerAdapter.updateMenzaId(position);
                return true;
            }
        };

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.places, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                mViewPager.setCurrentItem(AppConstant.DAY_ID_TODAY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
