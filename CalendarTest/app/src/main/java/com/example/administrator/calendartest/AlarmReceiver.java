package com.example.administrator.calendartest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-12-14.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "일정을 확인해주세요!!", Toast.LENGTH_SHORT).show();

        Bundle extras = intent.getExtras();
        int id = extras.getInt("scheduleID");
        Intent alarm = new Intent(context, ScheduleInfoActivity.class);
        alarm.putExtra("id", id);
        alarm.putExtra("vib", true);
        context.startActivity(alarm);
    }
}
