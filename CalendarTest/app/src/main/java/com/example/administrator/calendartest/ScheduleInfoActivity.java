package com.example.administrator.calendartest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-12-05.
 */

public class ScheduleInfoActivity extends AppCompatActivity {
    private TextView title;
    private LinearLayout main;
    private int id;
    private static SQLiteDatabase database;
    private final String DATABASE_NAME = "helperDatabase";
    private Vibrator vibrator;
    private final long[] vibPattern = {0, 100, 1000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_info);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        initView();
        initDatabase();

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        if(intent.getBooleanExtra("vib", false)) {
            startVib();
        }
        setInfo();
    }

    public void initView() {
        main = (LinearLayout)findViewById(R.id.MAIN);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
            }
        });
        title = (TextView)findViewById(R.id.TITLE);
    }

    public void initDatabase() {
        database = openOrCreateDatabase(DATABASE_NAME, Activity.MODE_PRIVATE, null);
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

    public void setInfo() {
        Cursor result = database.rawQuery("SELECT * from schedule " +
                "where id="+id, null);
        result.moveToNext();
        title.setText(result.getString(1));
    }

    public void startVib() {
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibPattern, 0);
    }
}

