package com.arcao.menza;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class AbstractBaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(getTitle());
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
