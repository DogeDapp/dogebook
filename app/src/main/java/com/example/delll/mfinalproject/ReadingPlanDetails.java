package com.example.delll.mfinalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReadingPlanDetails extends AppCompatActivity {

    private String FileName = "SAVE.txt";
    private Button searchButton, save, clear, change,timer;
    private EditText queryEditText;

    private EditText text;
    private TextView englishTextView, yinBiaoTextView, chineseTextView;
    private static final int UPDATE_CONTENT = 0;

    private myDB db = new myDB(this);
    private List<Map<String, String>> data;
    private  SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_plan_details);

        final Intent intent =  getIntent();

        final String name = intent.getStringExtra("name");

        FileName = name + ".txt";
        final String pages = intent.getStringExtra("pages");
        final String page = intent.getStringExtra("page");
        final String date = intent.getStringExtra("date");
        // int pos = intent.getIntExtra("post");



        final TextView book_ = (TextView)findViewById(R.id.book_name);
        final TextView date_ = (TextView)findViewById(R.id.updata_date);
        final TextView page_ = (TextView)findViewById(R.id.current_pageNum);

        book_.setText(name);
        date_.setText(date);
        page_.setText(page);

        searchButton = (Button)findViewById(R.id.search_button);
        queryEditText = (EditText)findViewById(R.id.query_eidtText);
        text = (EditText) findViewById(R.id.edit_plan);
        englishTextView = (TextView)findViewById(R.id.english_TextView);
        yinBiaoTextView = (TextView)findViewById(R.id.yinbiao_TextView);
        chineseTextView = (TextView)findViewById(R.id.chinese_TextView);
        save = (Button)findViewById(R.id.save_plan);
        clear = (Button)findViewById(R.id.clear_plan);
        change = (Button)findViewById(R.id.change);
        timer = (Button)findViewById(R.id.timer);

        load();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected())
                    Toast.makeText(ReadingPlanDetails.this, "当前无可用网络", Toast.LENGTH_SHORT).show();

                else if (isConnected() && queryEditText.getText().toString().equals(""))
                    Toast.makeText(ReadingPlanDetails.this, "请输入查询单词", Toast.LENGTH_SHORT).show();

                else {
                    sendRequestWithHttpURLConnection();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fileOutputStream = openFileOutput(FileName, MODE_PRIVATE);
                    String content = text.getText().toString();
                    fileOutputStream.write(content.getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(ReadingPlanDetails.this, "保存成功~", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    Log.e("TAG", "Fail to save file");
                    Toast.makeText(ReadingPlanDetails.this, "保存失败~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ReadingBookName",name);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(ReadingPlanDetails.this, ReadingTimer.class);
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(ReadingPlanDetails.this);
                View DialogView = factory.inflate(R.layout.dialoglayout, null);

                final TextView nameEdit = (TextView) DialogView.findViewById(R.id.nameEdit);
                final TextView totalPages = (TextView) DialogView.findViewById(R.id.TotalPages);
                final EditText pagesEdit = (EditText) DialogView.findViewById(R.id.pagesEdit);
                final EditText dateEdit = (EditText) DialogView.findViewById(R.id.dateEdit);

                nameEdit.setText(name);
                pagesEdit.setText(page);
                dateEdit.setText(date);
                totalPages.setText(pages);

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadingPlanDetails.this,android.R.style.Theme_Translucent_NoTitleBar);
                builder.setView(DialogView);
                builder.setTitle("");
                builder.setPositiveButton("放弃修改", new DialogInterface.OnClickListener() { // 设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                // final  int tmp1 = position;

                builder.setNegativeButton("保存修改", new DialogInterface.OnClickListener() { // 设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        book_.setText(nameEdit.getText().toString());
                        page_.setText(pagesEdit.getText().toString());
                        date_.setText(dateEdit.getText().toString());
                        db.upDate2DB(nameEdit.getText().toString(),pages, pagesEdit.getText().toString(), dateEdit.getText().toString());

                        Intent intent = new Intent(ReadingPlanDetails.this, NowReading.class);
                        intent.putExtra("name", nameEdit.getText().toString());
                        intent.putExtra("page", pages);
                        intent.putExtra("page", pagesEdit.getText().toString());
                        intent.putExtra("date", dateEdit.getText().toString());
                        startActivity(intent);
                        dialog.dismiss(); //关闭dialog
                    }
                });
                builder.create();
                builder.show();

            }
        });



    }

    private void load() {
        try  {
            FileInputStream fileInputStream = openFileInput(FileName);
            String content = "";
            byte[] buff = new byte[1024];
            int hasRead = 0;

            while((hasRead = fileInputStream.read(buff)) > 0) {
                content += new String(buff, 0, hasRead);
            }
            fileInputStream.close();
            text.setText(content);
            text.setSelection(content.length());
          //  Toast.makeText(ReadingPlanDetails.this, "Load successfully", Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            Log.e("TAG", "Fail to read file");
//            e.printStackTrace();
          //  Toast.makeText(ReadingPlanDetails.this, "Fail to load file", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_CONTENT:
                    List<String> list = (List<String>) message.obj;

                    for (int i = 0; i < list.size(); i++)
                        Log.i("" + i, list.get(i));

                    if (list.size() == 1) {
                        Toast.makeText(ReadingPlanDetails.this, "单词不存在，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        englishTextView.setText(list.get(0));
                        yinBiaoTextView.setText(list.get(1));
                        chineseTextView.setText(list.get(3));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void sendRequestWithHttpURLConnection() {
        final String webServiceUrl = "http://fy.webxml.com.cn/webservices/EnglishChinese.asmx/TranslatorString";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    Log.i("key", "Begin the connetcion.");
                    URL url = new URL(webServiceUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setRequestMethod("POST");
                    connection.connect();

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String request = queryEditText.getText().toString();
                    request = URLEncoder.encode(request, "utf-8");
                    out.writeBytes("wordKey=" + request);
                    out.flush();
                    out.close();

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
                    message.obj = parseXMLWithPull(response.toString());
                    handler.sendMessage(message);

                    Log.i("response", response.toString());
                }
                catch (Exception ex) {
                    Log.i("error", "Fail to connect:" + ex.toString());
                    ex.printStackTrace();
                }
                finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


    private boolean isConnected() {
        //获取手机当前所有连接管理对象
        boolean isConnected = false;
        Context context = this.getApplicationContext();
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected())
            {
                isConnected = false;
            }
            else
            {
                isConnected = true;
            }
        }
        return isConnected;
    }

    private List<String> parseXMLWithPull(String xml) throws XmlPullParserException, IOException {
        try {
            List<String> list = new ArrayList<>();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                            String str = parser.nextText();
                            list.add(str);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            return  list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
