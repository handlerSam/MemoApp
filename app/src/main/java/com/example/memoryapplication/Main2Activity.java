package com.example.memoryapplication;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private List<Password> passwordList = new ArrayList<>();
    public static final int MAXPASSWARDNUMBER = 200;
    public RecyclerView recyclerView;
    public PasswordAdapter adapter;
    public EditText searchEditText;
    public static Main2Activity main2Activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_list_layout);
        main2Activity = this;
        getSupportActionBar().hide();
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1:
                    //文件中的格式统一为 "p1t" "p1u" "p1p1" "p1p2" "p1r"...
                    int id = Integer.parseInt(data.getStringExtra("id"));
                    SharedPreferences.Editor editor = getSharedPreferences("password", MODE_PRIVATE).edit();
                    editor.putString("p"+id+"t",data.getStringExtra("t"));
                    editor.putString("p"+id+"u",data.getStringExtra("u"));
                    editor.putString("p"+id+"p1",data.getStringExtra("p1"));
                    editor.putString("p"+id+"p2",data.getStringExtra("p2"));
                    editor.putString("p"+id+"r",data.getStringExtra("r"));
                    editor.apply();
                    loadList();
                    adapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    }

    private void init(){
        loadList();


        //生成recyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PasswordAdapter(passwordList);
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //添加按钮：
        ImageView addButton = (ImageView)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = getAvailablePassword();
                if(temp >= 0){
                    Intent intent = new Intent(Main2Activity.this,addActivity.class);
                    intent.putExtra("id",String.valueOf(temp));
                    intent.putExtra("t","");
                    intent.putExtra("u","");
                    intent.putExtra("p1","");
                    intent.putExtra("p2","");
                    intent.putExtra("r","");
                    startActivityForResult(intent, 1);
                }else{
                    Toast.makeText(Main2Activity.this,"您添加的密码太多啦！",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //帮助按钮
        ImageView helpButton = (ImageView)findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(Main2Activity.main2Activity);
                dialog2.setTitle("Help");//标题
                dialog2.setMessage("密码备忘录V1.1 \n 制作者：sgy \n 1.长按条目标题可以修改或删除条目；\n 2.单击密码条目可以复制文本");//正文
                dialog2.setCancelable(true);//是否能点击屏幕取消该弹窗
                dialog2.show();
            }
        });

        //修改登陆密码
        TextView t = (TextView)findViewById(R.id.changeLoginPassword);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(Main2Activity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this)
                        .setTitle("请设置密码：")
                        .setMessage("请一定设置为自己不会忘记的密码！")
                        .setCancelable(true)
                        .setView(editText)
                        .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str = editText.getText().toString();
                                        if(str.equals("")){
                                            Toast.makeText(Main2Activity.this,"内容不能为空！",Toast.LENGTH_SHORT).show();
                                        }else{
                                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                            editor.putString("password", str);
                                            editor.apply();
                                            Toast.makeText(Main2Activity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                builder.create().show();
            }
        });

        //复制登陆密码
        TextView copyPassward = findViewById(R.id.copyPassword);
        copyPassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copyText = "";
                for(Password pas : passwordList){
                    copyText += pas.id+"\n";
                    copyText += pas.title+"\n";
                    copyText += pas.user+"\n";
                    copyText += pas.password+"\n";
                    copyText += pas.password2+"\n";
                    copyText += pas.remark+"\n\n";
                }
                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label",copyText);
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Main2Activity.this,"已复制所有密码",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getAvailablePassword(){
        //找到一个空的格子来让用户添加密码
        SharedPreferences pref = getSharedPreferences("password", MODE_PRIVATE);
        //文件中的格式统一为 "p1t" "p1u" "p1p1" "p1p2" "p1r"...
        for(int i = 0; i < MAXPASSWARDNUMBER; i++){
            if(pref.getString("p"+i+"t", "").equals("")){
                return i;
            }
        }
        return -1;
    }

    private void sortList(List<Password> passwordList){
        Collections.sort(passwordList, new Comparator<Password>() {
            @Override
            public int compare(Password o1, Password o2) {
                return o1.title.compareTo(o2.title);
            }
        });
    }

    public void loadList(){
        SharedPreferences pref = getSharedPreferences("password", MODE_PRIVATE);
        String temp = "";
        passwordList.clear();
        //文件中的格式统一为 "p1t" "p1u" "p1p1" "p1p2" "p1r"...
        for(int i = 0; i < MAXPASSWARDNUMBER; i++){
            temp = pref.getString("p"+i+"t", "");
            if(!temp.equals("")){
                Password p = new Password();
                p.title = temp;
                p.user = pref.getString("p"+i+"u","");
                p.password = pref.getString("p"+i+"p1","");
                p.password2 = pref.getString("p"+i+"p2","");
                p.remark = pref.getString("p"+i+"r","");
                p.id = i;
                passwordList.add(p);
            }
        }

        //按照title排序
        sortList(passwordList);
    }
}
