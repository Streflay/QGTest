package com.example.end;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.end.dao.UserSQL;
import com.example.end.model.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText usertel,usepwd,userpwd2;
    Button submit;
    User u;
    Pattern p1,p2;
    Matcher m1,m2;
    UserSQL userSQL;
    boolean flg=true;
    boolean flg1=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        p1=null;//正则判断
        m1=null;
        //注册电话号和密码
        usertel=findViewById(R.id.usertel);
        usepwd=findViewById(R.id.usepwd);
        userpwd2=findViewById(R.id.usepwd2);
        submit=findViewById(R.id.submit);
        userSQL=new UserSQL(this);
        userSQL.open();
        submit.setOnClickListener(new View.OnClickListener() {
            boolean flag=true;
            @Override
            public void onClick(View view) {
                String tel = usertel.getText().toString();  //用户名
                String pwd01 = usepwd.getText().toString();   //密码
                String pwd02 = userpwd2.getText().toString(); //二次输入的密码

                p1=Pattern.compile("[0-9]{11}");
                m1=p1.matcher(tel);
                flg=m1.matches();
                p2=Pattern.compile("[a-zA-Z0-9]{6,16}");
                m2=p2.matcher(pwd01);
                flg1=m2.matches();

                if (tel.equals("") || pwd01.equals("") || pwd02.equals("")) {
                    Toast.makeText(Register.this, "电话号码或密码不能为空", Toast.LENGTH_LONG).show();
                    flag=false;
                } else if (!flg) {
                    Toast.makeText(Register.this, "电话号码必须是11位数字", Toast.LENGTH_LONG).show();
                    flag=false;
                } else if (!flg1) {
                    Toast.makeText(Register.this, "密码由（字母和数字构成，不能小于6位，不能超过16位)", Toast.LENGTH_LONG).show();
                    flag=false;
                } else {
                    if (userSQL.checkUserExist(tel)){
                        flag=false;
                        Toast.makeText(Register.this, "电话号码不能重复", Toast.LENGTH_LONG).show();
                    }else{
                        u=new User();
                        u.setTel(tel);
                        u.setPassWord(pwd01);
                        flag=true;
                    }
                }
                if (flag==true) {
                    if (pwd01.equals(pwd02)){
                        userSQL.open();
                        userSQL.createUsers(u);
                        userSQL.close();
                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClass(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(Register.this, "密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
