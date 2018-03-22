package com.tat.team.calendarproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kitte on 2016-12-29.
 */

public class TaTCalendarWeekAdapter extends FragmentStatePagerAdapter {
    private HashMap<Integer, TaTCalendarWeekFragment> frgMap;
    private ArrayList<Long> listMonthByMillis = new ArrayList<>();
    private int numOfMonth;
    private TaTCalendarWeekFragment.OnFragmentListener onFragmentListener;

    public TaTCalendarWeekAdapter(FragmentManager fm) {
        super(fm);
        clearPrevFragments(fm);
        frgMap = new HashMap<Integer, TaTCalendarWeekFragment>();
    }

    private void clearPrevFragments(FragmentManager fm) {
        List<Fragment> listFragment = fm.getFragments();

        if (listFragment != null) {
            FragmentTransaction ft = fm.beginTransaction();

            for (Fragment f : listFragment) {
                if (f instanceof TaTCalendarWeekFragment) {
                    ft.remove(f);
                }
            }
            ft.commitAllowingStateLoss();
        }
    }
    @Override
    public Fragment getItem(int position) {
        TaTCalendarWeekFragment frg = null;
        if (frgMap.size() > 0) {
            frg = frgMap.get(position);
//            Log.d("TaTCalendarAdapter","frgMap not null position("+position+")");
        }
        if (frg == null) {
            frg = TaTCalendarWeekFragment.newInstance(position);
            frg.setOnFragmentListener(onFragmentListener);
            frgMap.put(position, frg);
//            Log.d("TaTCalendarAdapter","frgMap null position("+position+")");
        }
        frg.setTimeByMillis(listMonthByMillis.get(position));

        return frg;
    }

    @Override
    public int getCount() {
        return listMonthByMillis.size();
    }

    public void setNumOfWeek(int numOfMonth) {
        this.numOfMonth = numOfMonth;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_MONTH, -numOfMonth);

        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.WEEK_OF_MONTH,1);
        }

        notifyDataSetChanged();
    }

    public void addNext() {
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        for (int i = 0; i < numOfMonth; i++) {
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
            listMonthByMillis.add(calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);

        for (int i = numOfMonth; i > 0; i--) {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);

            listMonthByMillis.add(0, calendar.getTimeInMillis());
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public String getMonthDisplayed(int position) {
        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(listMonthByMillis.get(position));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월");
        Date date = new Date();
        date.setTime(listMonthByMillis.get(position));

        return sdf.format(date);
    }

    public void setOnFragmentListener(TaTCalendarWeekFragment.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }
}
