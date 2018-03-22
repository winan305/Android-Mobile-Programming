package com.tat.team.calendarproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by kitte on 2016-12-29.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button monthBtn = (Button)findViewById(R.id.calendar_month_btn);
        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TaTCalendarActivity.class);
                startActivity(intent);
            }
        });

        Button weekBtn = (Button)findViewById(R.id.calendar_week_btn);
        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TaTCalendarWeekActivity.class);
                startActivity(intent);
            }
        });
    }
}
