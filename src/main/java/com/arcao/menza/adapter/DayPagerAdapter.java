package com.arcao.menza.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.text.format.DateUtils;
import com.arcao.menza.R;
import com.arcao.menza.constant.AppConstant;
import com.arcao.menza.fragment.DayMenuFragment;
import com.arcao.menza.fragment.UpdatableFragment;
import java.util.Calendar;
import java.util.Date;

public class DayPagerAdapter extends FragmentStatePagerAdapter {
    private int placeId;
    private final Context mContext;

    public DayPagerAdapter(Context context, FragmentManager fm, int initialPlaceId) {
        super(fm);
        mContext = context;
        this.placeId = initialPlaceId;
    }

    @Override
    public Fragment getItem(int position) {
        return DayMenuFragment.newInstance(position, placeId);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UpdatableFragment) {
            UpdatableFragment fragment = (UpdatableFragment) object;
            fragment.getArguments().putInt(DayMenuFragment.ARG_PLACE_ID, placeId);
            fragment.update();
        }

        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return AppConstant.DAY_ID_MAX;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, position - AppConstant.DAY_ID_TODAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date date = cal.getTime();

        switch (position) {
            case AppConstant.DAY_ID_YESTERDAY:
            case AppConstant.DAY_ID_TODAY:
            case AppConstant.DAY_ID_TOMORROW:
                return mContext.getResources().getStringArray(R.array.relative_day_title)[position - AppConstant.DAY_ID_YESTERDAY];
            default:
                return DateUtils.formatDateTime(mContext, date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
        }
    }

    public void updatePlaceId(int placeId) {
        this.placeId = placeId;
        notifyDataSetChanged();
    }
}
