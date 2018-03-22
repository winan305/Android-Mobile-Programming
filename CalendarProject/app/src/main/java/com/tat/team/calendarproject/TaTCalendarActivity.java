package com.tat.team.calendarproject;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kitte on 2016-12-25.
 */

public class TaTCalendarActivity extends AppCompatActivity implements TaTCalendarFragment.OnFragmentListener{
    private static final String TAG = "TaTCalendarActivity";
    private static final int COUNT_PAGE = 12;
    private ViewPager viewPager;
    private TaTCalendarAdapter taTCalendarAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        viewPager = (ViewPager)findViewById(R.id.calendar_pager);
        taTCalendarAdapter = new TaTCalendarAdapter(getSupportFragmentManager());
        viewPager.setAdapter(taTCalendarAdapter);

        taTCalendarAdapter.setOnFragmentListener(this);
        taTCalendarAdapter.setNumOfMonth(COUNT_PAGE);
        viewPager.setCurrentItem(COUNT_PAGE);
        String title = taTCalendarAdapter.getMonthDisplayed(COUNT_PAGE);
        getSupportActionBar().setTitle(title);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = taTCalendarAdapter.getMonthDisplayed(position);
                getSupportActionBar().setTitle(title);

                if (position == 0) {
                    taTCalendarAdapter.addPrev();
                    viewPager.setCurrentItem(COUNT_PAGE, false);
                    Log.d("TaTCalendarActivity","position("+position+") COUNT_PAGE("+COUNT_PAGE+")");
                } else if (position == taTCalendarAdapter.getCount() - 1) {
                    taTCalendarAdapter.addNext();
                    viewPager.setCurrentItem(taTCalendarAdapter.getCount() - (COUNT_PAGE + 1), false);
                    Log.d("TaTCalendarActivity","position("+position+") COUNT_PAGE("+(taTCalendarAdapter.getCount() - (COUNT_PAGE + 1))+")");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onFragmentListener(View view) {
        resizeHeight(view);
    }

    public void resizeHeight(View mRootView) {

        if (mRootView.getHeight() < 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        if (layoutParams.height <= 0) {
            layoutParams.height = mRootView.getHeight();
            viewPager.setLayoutParams(layoutParams);
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(viewPager.getLayoutParams().height, mRootView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
                layoutParams.height = val;
                viewPager.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }
}
