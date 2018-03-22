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
import java.util.Locale;

/**
 * Created by kitte on 2016-12-25.
 */

public class TaTCalendarAdapter extends FragmentStatePagerAdapter {
    private HashMap<Integer, TaTCalendarFragment> frgMap;
    private ArrayList<Long> listMonthByMillis = new ArrayList<>();
    private int numOfMonth;
    private TaTCalendarFragment.OnFragmentListener onFragmentListener;

    public TaTCalendarAdapter(FragmentManager fm) {
        super(fm);
        clearPrevFragments(fm);
        frgMap = new HashMap<Integer, TaTCalendarFragment>();
    }

    private void clearPrevFragments(FragmentManager fm) {
        List<Fragment> listFragment = fm.getFragments();

        if (listFragment != null) {
            FragmentTransaction ft = fm.beginTransaction();

            for (Fragment f : listFragment) {
                if (f instanceof TaTCalendarFragment) {
                    ft.remove(f);
                }
            }
            ft.commitAllowingStateLoss();
        }
    }
    @Override
    public Fragment getItem(int position) {
        TaTCalendarFragment frg = null;
        if (frgMap.size() > 0) {
            frg = frgMap.get(position);
//            Log.d("TaTCalendarAdapter","frgMap not null position("+position+")");
        }
        if (frg == null) {
            frg = TaTCalendarFragment.newInstance(position);
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

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;

        Calendar calendar = Calendar.getInstance();
        ///< 12달 전
        calendar.add(Calendar.MONTH, -numOfMonth);
//        Log.d("TaTCalendarAdapter",calendar.toString());
        calendar.set(Calendar.DATE, 1);
//        Log.d("TaTCalendarAdapter",calendar.toString());

        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH, 1);
//            Log.d("TaTCalendarAdapter",calendar.toString());
        }
        Log.d("TaTCalendarAdapter","numOfMonth("+numOfMonth+") listMonthByMillis size("+listMonthByMillis.size()+")");
        notifyDataSetChanged();
    }

    public void addNext() {
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        for (int i = 0; i < numOfMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            listMonthByMillis.add(calendar.getTimeInMillis());
        }
        Log.d("TaTCalendarAdapter","numOfMonth("+numOfMonth+") listMonthByMillis size("+listMonthByMillis.size()+")");

        notifyDataSetChanged();
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        calendar.set(Calendar.DATE, 1);
        for (int i = numOfMonth; i > 0; i--) {
            calendar.add(Calendar.MONTH, -1);

            listMonthByMillis.add(0, calendar.getTimeInMillis());
        }
        Log.d("TaTCalendarAdapter","numOfMonth("+numOfMonth+") listMonthByMillis size("+listMonthByMillis.size()+")");

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
//        if (yyyy != calendar.get(Calendar.YEAR)) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년. MM");
//            Date date = new Date();
//            date.setTime(listMonthByMillis.get(position));
//
//            return sdf.format(date);
//        } else {
//            return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
//        }
    }

    public void setOnFragmentListener(TaTCalendarFragment.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }
}
