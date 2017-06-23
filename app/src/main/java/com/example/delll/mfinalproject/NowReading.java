package com.example.delll.mfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NowReading extends AppCompatActivity {
    private SimpleAdapter adapter;
    private ImageView clock;
    private ListView listView;
    private ImageView addPlanButton;
    private List<Map<String, String>> data;
    private  static  final String STATICACION="com.example.delll.mfinalproject.staticreceiver";
    private  SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_reading);

        listView = (ListView)findViewById(R.id.bookListView);
        addPlanButton = (ImageView)findViewById(R.id.addPlan_Button);
        clock=(ImageView)findViewById(R.id.clock);
        setListView();

        setClickListenr();

        adapter.notifyDataSetChanged();
    }

    private void setListView() {

        data = new ArrayList<Map<String, String>>();

       final ListView listview = (ListView) findViewById(R.id.bookListView);
        myDB db = new myDB(this);
        Cursor c = db.query();

        if (c != null) {
            int i=0;
            if (c.moveToFirst()) {
                do {
                    Map<String, String> temp = new LinkedHashMap<>();
                    temp.put("name", c.getString(c.getColumnIndex("name")));
                    temp.put("pages", c.getString(c.getColumnIndex("pages")));
                    temp.put("page", c.getString(c.getColumnIndex("page")));
                    temp.put("date", c.getString(c.getColumnIndex("date")));

                    data.add(temp);
                    i=i+1;
                } while (c.moveToNext());
            }
            // }

           adapter = new SimpleAdapter(NowReading.this, data, R.layout.item_listview, new String[]{"name", "page","pages", "date"}, new int[]{R.id.bookName, R.id.readNum,R.id.pageNum, R.id.aimDate});

            listview.setAdapter(adapter);


            final int x=i;
           clock.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("text",String.valueOf(x));
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(NowReading.this, AlarmSetting.class);
                    startActivity(intent);
                }
            });


        }

    }

    private void setClickListenr() {

        addPlanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NowReading.this, NewReadingPlan.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                final Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
                final String name = (String)item.get("name");
                final String pages = (String)item.get("pages");
                final String page = (String)item.get("page");
                final String date = (String)item.get("date");
                intent.putExtra("name", name);
                intent.putExtra("pages", pages);
                intent.putExtra("page", page);
                intent.putExtra("date", date);
                intent.setClass(NowReading.this, ReadingPlanDetails.class);
                startActivity(intent);
              Intent intent2 = new Intent(STATICACION);
                Bundle bundle = new Bundle();
                bundle.putString("names",name);
                bundle.putString("dates",date);
                intent2.putExtras(bundle);
                sendBroadcast(intent2);

                setListView();

            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
                final String name = (String)item.get("name");

                final  int tmp1 = position;
                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(NowReading.this); // 得到对话框构造器
                dialog.setTitle("确定删除读书计划"); // 设置标题
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        myDB db = new myDB(NowReading.this);

                        db.delete2DB(name);
                        data.remove(tmp1);

                        String FileName = name + ".txt";

                        deleteAllFiles(FileName);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss(); //关闭dialog
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create();
                dialog.show();

                return true;
            }
        });

    }

    public boolean deleteAllFiles(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }



}
