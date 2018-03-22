package com.example.administrator.calendartest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private final String DATABASE_NAME = "helperDatabase";
    private static ListView scheduleList;
    private static ArrayAdapter scheduleAdapter;
    private static String[] titles;
    private static int[] ids;
    private static CalendarViewPager calendarViewPager;
    public static Context mContext;
    final static int MAX_PAGE = 1200;                         //View Pager의 총 페이지 갯수를 나타내는 변수 선언

    private static SQLiteDatabase database;
    private final String[] dbCoulmns = {
            "id", "title", "priority",
            "start_year", "start_month", "start_day", "start_hour", "start_min",
            "end_year", "end_month", "end_day", "end_hour","end_min",
            "place", "content",
            "alarm_hour","alarm_min", "alarm_ok"};

    private final String[] dbTables = {"schedule"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initCalendar();
        initDatabase();
        initScheduleList();
    }

    public void initCalendar() {
        calendarViewPager = (CalendarViewPager)findViewById(R.id.viewPager);        //Viewpager 선언 및 초기화
        calendarViewPager.setAdapter(new adapter(getSupportFragmentManager()));     //선언한 viewpager에 adapter를 연결
        calendarViewPager.setCurrentItem(MAX_PAGE/2);
    }

    private class adapter extends FragmentStatePagerAdapter {                    //adapter클래스
        public adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            CalendarViewPage calendarViewPage = new CalendarViewPage();
            Bundle offset = new Bundle(1);
            calendarViewPage.setArguments(offset);
            offset.putInt("offset",position - MAX_PAGE/2);
            return calendarViewPage;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.WRITE:
                startActivity(new Intent(MainActivity.this, ScheduleAddActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initScheduleList() {
        scheduleList = (ListView)findViewById(R.id.SCHEDULE_LIST);
        scheduleList.setOnItemClickListener(onClickListItem);
        Calendar calendar = Calendar.getInstance();

        showSchedules(calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DAY_OF_MONTH));
    }
    // 리스트뷰 아이템(스케쥴 제목) 클릭 이벤트
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, ScheduleInfoActivity.class);
            intent.putExtra("id", ids[position]);
            startActivity(intent);
        }
    };

    public void setAlarm(int[] alarmSetting, int scheduleID) {

        new AlarmHATT(getApplicationContext()).setAlarm(alarmSetting, scheduleID);
    }

    // 고유번호, 제목, 년, 월, 일(시작, 끝 두개)시간, 위치, 내용, 중요도, 알람오프셋(언제부터 알람?), 알람여부
    // id, title, (year, month, day, time)*2, content, alarmoffset, alarmok
    // 데이터베이스 초기화
    public void initDatabase() {
        database = openOrCreateDatabase(DATABASE_NAME, Activity.MODE_PRIVATE, null);
        //database.execSQL("drop table schedule");
        database.execSQL("create table if not exists schedule(" +
                "id integer primary key autoincrement, " +
                "title text not null, " +
                "priority integer not null," +
                "start_year integer not null, " +
                "start_month integer not null, " +
                "start_day  integer not null, " +
                "start_hour integer not null," +
                "start_min integer not null," +
                "end_year integer not null, " +
                "end_month integer not null, " +
                "end_day  integer not null, " +
                "end_hour integer not null," +
                "end_min integer not null," +
                "place text, " +
                "content text, " +
                "alarm_hour integer, " +
                "alarm_min integer, " +
                "alarm_ok integer not null)");
    }
    // 저장된 스케쥴 어댑터에 뿌려주기

    public static Cursor searchSchdules(int year, int month, int day) {
        Cursor result = database.rawQuery("SELECT title, id, priority, " +
                "start_year, start_month, start_day, start_hour, start_min," +
                "end_year, end_month, end_day, end_hour, end_min from schedule " +
                "where start_year <= " + year +
                " and start_month <= " + month +
                " and start_day <= " + day +
                " and end_year >= " + year +
                " and end_month >= " + month +
                " and end_day >= " + day, null);

        return result;
    }
    public static void showSchedules(int year, int month, int day) {
        Cursor c = searchSchdules(year, month, day);

        titles = new String[c.getCount()];
        ids = new int[c.getCount()];

        for(int i = 0; i < titles.length; i++) {
            c.moveToNext();
            String title = c.getString(0);
            int priority = c.getInt(2);
            String start_year = c.getString(3);
            String start_month = c.getString(4);
            String start_day = c.getString(5);
            String start_hour = c.getString(6);
            String start_min = c.getString(7);
            String end_year = c.getString(8);
            String end_month = c.getString(9);
            String end_day = c.getString(10);
            String end_hour = c.getString(11);
            String end_min = c.getString(12);

            String star = "";
            String start = start_year +"/"+start_month +"/"+start_day + " " + start_hour +":" + start_min;
            String end = end_year +"/"+end_month +"/"+end_day + " " + end_hour +":" + end_min;
            for(int p = 0; p < priority; p++) star += "★";
            titles[i] = title + " " + star + "\n" + start + " ~ " + end;

            ids[i] = c.getInt(1);
        }
        scheduleAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, titles);
        scheduleList.setAdapter(scheduleAdapter);
    }

    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context = context;
        }

        public void setAlarm(int[] alarmSetting, int scheduleID) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putInt("scheduleID", scheduleID);
            intent.putExtras(bundle);
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, alarmSetting[0]);
            calendar.set(Calendar.MONTH, alarmSetting[1]-1);
            calendar.set(Calendar.DATE, alarmSetting[2]);
            calendar.set(Calendar.HOUR_OF_DAY, alarmSetting[3]);
            calendar.set(Calendar.MINUTE, alarmSetting[4]);
            calendar.set(Calendar.SECOND, 00);
            //알람 예약

            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }
}
