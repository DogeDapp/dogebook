package com.example.delll.mfinalproject;

/**
 * Created by delll on 2016/12/9.
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReadingTimer extends Activity{
    private Chronometer chronometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_timer);
        Intent i=getIntent();
        TextView tv=(TextView) findViewById(R.id.Timerbookname);
        tv.setText("《"+i.getStringExtra("ReadingBookName")+"》");
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        final Button begin=(Button)findViewById(R.id.begin);
        final Button stop=(Button)findViewById(R.id.stop);
        final Button back=(Button)findViewById(R.id.back);
        begin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                chronometer.setBase((long) ((long) (SystemClock.elapsedRealtime() - (long) Double
                        .parseDouble(chronometer.getText().toString().split(":")[0]) * 60000) - Double
                        .parseDouble(chronometer.getText().toString().split(":")[1]) * 1000));
                chronometer.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                chronometer.stop();
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ReadingTimer.this); // 得到对话框构造器
                dialog.setTitle("残忍离开读书计时吗？"); // 设置标题
                dialog.setPositiveButton("残忍离开", new DialogInterface.OnClickListener() { // 设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  chronometer.setBase(SystemClock.elapsedRealtime());
                        Intent intent = new Intent();
                        intent.setClass(ReadingTimer.this, NowReading.class);
                        startActivity(intent);
                        dialog.dismiss(); //关闭dialog
                        Toast.makeText(ReadingTimer.this, "您已经读了"+chronometer.getText().toString()+"很棒哦~", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("再读一会儿", new DialogInterface.OnClickListener() { // 设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create();
                dialog.show();


            }
        });
    }


}
