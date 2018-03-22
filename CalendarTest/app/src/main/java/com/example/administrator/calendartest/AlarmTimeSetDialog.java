package com.example.administrator.calendartest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-12-15.
 */

public class AlarmTimeSetDialog extends Dialog {
    private Button complete, cancel;
    private TimePicker timePicker;
    Context mContext;

    public int[] timeSetting = new int[2];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmtimeset_dialog);
        initView();
        setButton();
    }

    public void initView() {
        complete = (Button)findViewById(R.id.COMPLETE);
        cancel = (Button)findViewById(R.id.CANCEL);
        timePicker = (TimePicker)findViewById(R.id.TIME);
    }

    public void setButton() {
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeInfo();
                ((ScheduleAddActivity)mContext).setAlarmTime(timeSetting);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public AlarmTimeSetDialog(Context context) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        mContext = context;
    }

    public void setTimeInfo() {
        timeSetting[0] = timePicker.getHour();
        timeSetting[1] = timePicker.getMinute();
    }
}

