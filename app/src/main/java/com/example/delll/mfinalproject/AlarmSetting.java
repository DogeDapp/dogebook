package com.example.delll.mfinalproject;

/**
 * Created by delll on 2016/12/9.
 */

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmSetting extends Activity {
    private  static  final String DYNAMICACION="com.example.delll.mfinalproject.alarmreceiver";
    private static final String TAG = MainActivity.class.getSimpleName();
    private TimePicker mTimePicker;
    private Button mButton1;
    private Button mButton2;
    private Button mButtonCancel;
    private int mHour = -1;
    private int mMinute = -1;
    public static final long DAY = 1000L * 60 * 60 * 24;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsetting);

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if(mHour == -1 && mMinute == -1) {
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
        }

        mTimePicker = (TimePicker)findViewById(R.id.timePicker);
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinute);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                mHour = hourOfDay;
                mMinute = minute;
            }
        });

        mButton2 = (Button)findViewById(R.id.repeating_button);
        mButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i=getIntent();
                String pass =i.getStringExtra("text");
               // Toast.makeText(AlarmSetting.this, pass, Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(AlarmSetting.this, AlarmReceiver.class);
                Bundle bundle = new Bundle();
                bundle.putString("text", pass);
                intent.putExtras(bundle);

                PendingIntent sender = PendingIntent.getBroadcast(AlarmSetting.this, 0, intent, 0);
                sendBroadcast(intent);
                long firstTime = SystemClock.elapsedRealtime();	// 开机之后到现在的运行时间(包括睡眠时间)
                long systemTime = System.currentTimeMillis();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
                calendar.set(Calendar.MINUTE, mMinute);
                calendar.set(Calendar.HOUR_OF_DAY, mHour);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // 选择的每天定时时间
                long selectTime = calendar.getTimeInMillis();
                // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                if(systemTime > selectTime) {
                    Toast.makeText(AlarmSetting.this, "设置的时间小于当前时间，请重新设置", Toast.LENGTH_SHORT).show();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    selectTime = calendar.getTimeInMillis();
                }
                // 计算现在时间到设定时间的时间差
                long time = selectTime - systemTime;
                firstTime += time;
                // 进行闹铃注册
                AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        firstTime, 10*1000, sender);

                Log.i(TAG, "time ==== " + time + ", selectTime ===== "
                        + selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);
                Toast.makeText(AlarmSetting.this, "设置提醒时间成功! ", Toast.LENGTH_LONG).show();

            }
        });

        mButtonCancel = (Button)findViewById(R.id.cancel_button);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AlarmSetting.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(AlarmSetting.this,
                        0, intent, 0);

                // 取消闹铃
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                am.cancel(sender);
            }
        });
    }


}
