package com.example.delll.mfinalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText newPassword;
    private EditText confirmPassword;
    private Button RegisterButton;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView lockImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newPassword = (EditText)findViewById(R.id.new_password);
        confirmPassword = (EditText)findViewById(R.id.confirm_password);
        lockImage = (ImageView)findViewById(R.id.image_lock);
        RegisterButton = (Button)findViewById(R.id.register_button);
        preferences = getSharedPreferences("Password", MODE_PRIVATE);
        editor = preferences.edit();

        if (preferences.getString("Password", "").equals("")) {
            RegisterButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (!newPassword.getText().toString().equals("")
                            && newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                        editor.putString("Password", newPassword.getText().toString());
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, NowReading.class);
                        startActivity(intent);
                    }
                    else if (newPassword.getText().toString().equals("") ||
                            confirmPassword.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "密码不可以为空，请检查", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "密码错误，请检查", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            newPassword.setVisibility(View.INVISIBLE);
            lockImage.setVisibility(View.INVISIBLE);
            confirmPassword.setHint("请输入密码");
            RegisterButton.setText("登录");
            RegisterButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (preferences.getString("Password", "").equals(confirmPassword.getText().toString())) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, NowReading.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "密码错误，请检查", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
