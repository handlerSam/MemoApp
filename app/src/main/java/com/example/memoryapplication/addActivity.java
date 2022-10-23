package com.example.memoryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class addActivity extends AppCompatActivity {
    EditText titleText;
    EditText userText;
    EditText passwordText;
    EditText password2Text;
    EditText remarkText;
    Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_password_layout);
        getSupportActionBar().hide();
        find();
        final Intent fatherIntent = getIntent();
        titleText.setText(fatherIntent.getStringExtra("t"));
        userText.setText(fatherIntent.getStringExtra("u"));
        passwordText.setText(fatherIntent.getStringExtra("p1"));
        password2Text.setText(fatherIntent.getStringExtra("p2"));
        remarkText.setText(fatherIntent.getStringExtra("r"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleText.getText().toString();
                if(!title.equals("")){
                    Intent intent = new Intent();
                    intent.putExtra("id",fatherIntent.getStringExtra("id"));
                    intent.putExtra("t",title);
                    intent.putExtra("u",userText.getText().toString());
                    intent.putExtra("p1",passwordText.getText().toString());
                    intent.putExtra("p2",password2Text.getText().toString());
                    intent.putExtra("r",remarkText.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(addActivity.this,"标题不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void find(){
        titleText = (EditText) findViewById(R.id.addTitle);
        userText = (EditText) findViewById(R.id.addUserText);
        passwordText = (EditText) findViewById(R.id.addPassword1Text);
        password2Text = (EditText) findViewById(R.id.addPassword2Text);
        remarkText = (EditText) findViewById(R.id.addRemarkText);
        button = (Button) findViewById(R.id.addButton);
    }
}
