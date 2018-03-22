package com.tat.team.calendarproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tat.team.calendarproject.widget.TaTCalendarItemView;
import com.tat.team.calendarproject.widget.TaTCalendarView;

import java.util.Calendar;

/**
 * Created by kitte on 2016-12-25.
 */

public class TaTCalendarFragment extends Fragment {
    private int position;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View mRootView;
    private TaTCalendarView calendarView;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener{
        public void onFragmentListener(View view);
    }

    public static TaTCalendarFragment newInstance(int position) {
        TaTCalendarFragment frg = new TaTCalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("poisition");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tat_calendar, null);
        calendarView = (TaTCalendarView) mRootView.findViewById(R.id.calendarview);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);

//        Log.d("TaTCanlendarFragment",calendar.toString());
        // 1일의 요일
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        ///< 1일이 일요일이 아닐 경우 이전월의 날짜를 넣기 위한 개수 구하기.
        int preOfDay = dayOfWeek-1;
        //이달의 마지막 날
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(timeByMillis);
        calendar1.set(Calendar.DATE, maxDateOfMonth);
        ///< 마지막 날이 토요일이 아닐경우 다음월의 날짜를 넣기 위한 개수 구하기
        int nextOfDay = 7 - calendar1.get(Calendar.DAY_OF_WEEK);
        Log.d("TaTCanlendarFragment","dayofWeek("+dayOfWeek+") maxDateOfMonth("+maxDateOfMonth+") lastDayofWeek("+calendar1.get(Calendar.DAY_OF_WEEK)+")");
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);
        /**
         * 요일 설정
         */
        for(int i=0; i < 7; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setDate(calendar.getTimeInMillis());
            child.setDayOfWeek(i);
            calendarView.addView(child);
        }
        /**
         * 이전달에 일자를 먼저 세팅
         * - 1일이 일요일이 아니면, 비워진 칸을 이전달 마지막 일자로 세팅함
         */
        for(int i=0; i < preOfDay; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setTextColorChange(true);
            if(i ==0) {
                calendar.add(Calendar.DATE,-(preOfDay-i));
            }else{
                calendar.add(Calendar.DATE,1);
            }
            /**
             * 이전월 시간을 설정하기 위해서 날짜 계산 이후에 세팅함
             * - 현재 달로 설정되어 있으므로 계산 이후에 세팅함
             */
            child.setDate(calendar.getTimeInMillis());

            calendarView.addView(child);
        }

        /**
         * 이전월 세팅이 이루어지면, 현재월 세팅을 위해서 하루를 더함
         */
        if(preOfDay > 0) {
            calendar.add(Calendar.DATE,1);
        }

        /**
         * 현재월을 세팅
         */
        for (int i = 0; i < maxDateOfMonth; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setTextColorChange(false);
            if (i == 20) {
                child.setEvent(R.color.colorPrimaryDark);
            }
            child.setDate(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, 1);

            calendarView.addView(child);
        }

        /**
         * 현재월의 마지막이 토요일이 아닐경우 다음월 날짜를 토요일까지 세팅함
         */
        for(int i=0; i < nextOfDay; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setTextColorChange(true);
            child.setDate(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, 1);
            calendarView.addView(child);
        }
//        for (int i = 0; i < maxDateOfMonth + 7; i++) {
//            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
//            if (i == 20) {
//                child.setEvent(R.color.colorPrimaryDark);
//            }
//            child.setDate(calendar.getTimeInMillis());
//            if (i < 7) {
//                child.setDayOfWeek(i);
//            } else {
//                calendar.add(Calendar.DATE, 1);
//            }
//            calendarView.addView(child);
//        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {

            mRootView.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    onFragmentListener.onFragmentListener(mRootView);
                }
            });

        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }
}
