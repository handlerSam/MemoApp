package com.example.memoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        password = pref.getString("password","");
        if(password.equals("")){//头一次开启软件：
            final EditText editText = new EditText(MainActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("请设置密码：")
                    .setMessage("请一定设置为自己不会忘记的密码！")
                    .setCancelable(false)
                    .setView(editText)
                    .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = editText.getText().toString();
                                    if(str.equals("")){
                                        Toast.makeText(MainActivity.this,"内容不能为空！",Toast.LENGTH_SHORT).show();
                                    }else{
                                        password = str;
                                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                        editor.putString("password", str);
                                        editor.apply();
                                        Intent intent1 = new Intent(MainActivity.this,Main2Activity.class);
                                        startActivity(intent1);
                                    }
                                }
                            }
                    );
            builder.create().show();
        }else{//正常情况：
            final EditText editText2 = findViewById(R.id.password);
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String temp = editText2.getText().toString();
                    if(temp.equals(password)){
                        //正确打开了锁
                        Intent intent1 = new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(intent1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}