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

public class TimeSetDialog extends Dialog {
    private TextView textView;
    private Button complete, cancel;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private int timeFlag;
    Context mContext;

    public int[] timeSetting = new int[5];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeset_dialog);
        initView();
        setButton();
    }

    public void initView() {
        textView = (TextView)findViewById(R.id.TEXTVIEW);
        complete = (Button)findViewById(R.id.COMPLETE);
        cancel = (Button)findViewById(R.id.CANCEL);
        datePicker = (DatePicker)findViewById(R.id.DATE);
        timePicker = (TimePicker)findViewById(R.id.TIME);

        if(timeFlag == 0) textView.setText("시작날짜");
        else textView.setText("종료날짜");
    }

    private void setTimeInfo() {
        timeSetting[0] = datePicker.getYear();
        timeSetting[1] = datePicker.getMonth()+1;
        timeSetting[2] = datePicker.getDayOfMonth();
        timeSetting[3]= timePicker.getHour();
        timeSetting[4] = timePicker.getMinute();
    }

    public void setButton() {
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeInfo();
                if(timeFlag == 0) ((ScheduleAddActivity)mContext).setStartTime(timeSetting);
                else ((ScheduleAddActivity)mContext).setEndTime(timeSetting);
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
    public TimeSetDialog(Context context, int timeFlag) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        mContext = context;
        this.timeFlag = timeFlag;
    }

}

