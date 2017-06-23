package com.example.delll.mfinalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewReadingPlan extends AppCompatActivity {

    private Context content;
    private Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reading_plan);

        content = this;
        final myDB db = new myDB(content);

        finish = (Button)findViewById(R.id.finishButton);
        final EditText addName = (EditText)findViewById(R.id.bookNameEditText);
        final EditText addPages = (EditText)findViewById(R.id.pageNumEditText);
        final EditText addReadedPages = (EditText)findViewById(R.id.pageEditText);
        final EditText addData = (EditText) findViewById(R.id.finishDate);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(addName.getText().toString())) {
                    Toast.makeText(NewReadingPlan.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(addPages.getText().toString())) {
                    Toast.makeText(NewReadingPlan.this, "页数为空，请完善", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(addData.getText().toString())) {
                    Toast.makeText(NewReadingPlan.this, "完成日期为空，请完善", Toast.LENGTH_SHORT).show();
                } else if (db.query2DB(addName.getText().toString())) {
                    Toast.makeText(NewReadingPlan.this, "名字重复啦，请核查", Toast.LENGTH_SHORT).show();
                } else {
                    // add data into the database and back to the mainactivity

                    db.insert2DB(addName.getText().toString(), addPages.getText().toString(),addReadedPages.getText().toString(), addData.getText().toString());

                    Intent intent = new Intent(NewReadingPlan.this, NowReading.class);
                    intent.putExtra("name", addName.getText().toString());
                    intent.putExtra("pages", addPages.getText().toString());
                    intent.putExtra("page", addReadedPages.getText().toString());
                    intent.putExtra("date", addData.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }



}
