package com.tat.team.calendarproject;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void calendarWeekTesT() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        assertEquals(1,dayOfWeek);
        System.out.println(dayOfWeek+":"+cal.get(Calendar.DATE));
        cal.add(Calendar.WEEK_OF_MONTH,-50);
//        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        for(int i=0; i < 100; i++) {
            cal.add(Calendar.WEEK_OF_MONTH,1);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
            Date date = new Date();
            date.setTime(cal.getTimeInMillis());
            System.out.println(dayOfWeek+":"+sdf.format(date));
        }
    }
}