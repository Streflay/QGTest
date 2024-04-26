package com.example.end;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.end.dao.DateBaseHelper;
import com.example.end.dao.UserSQL;
import com.example.end.dao.WordSQl;
import com.example.end.bean.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText tel, pwd;
    Button btnlogin, btnreg;
    CheckBox ck_rem;
    private String Tel;
    private DateBaseHelper dateBaseHelper;
    public  UserSQL userSQL;
    public WordSQl wordSQL;
    private boolean isRemember=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateBaseHelper=new DateBaseHelper(this);
        dateBaseHelper.open();
        tel=findViewById(R.id.tel);
        pwd=findViewById(R.id.pwd);
        btnlogin=findViewById(R.id.login);
        btnreg=findViewById(R.id.reg);
        ck_rem=findViewById(R.id.rem_password);
        userSQL=new UserSQL(MainActivity.this);
        userSQL.open();
        wordSQL=new WordSQl(MainActivity.this);
        wordSQL.open();
        ck_rem.setOnCheckedChangeListener(new CheckListener());
        pwd.setOnFocusChangeListener(this);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            boolean flag;
            @Override
            public void onClick(View view) {
                Tel=tel.getText().toString();
                String password=pwd.getText().toString();
                if (Tel.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "电话号码或密码不能为空", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else {
                    if (userSQL.checkUserExist(Tel,password)){
                        flag=true;
                    }else {
                        flag=false;
                    }
                }
                if (flag){
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    if (isRemember) {
                        User u=new User();
                        u.setRem("是");
                        userSQL.open();
                        userSQL.updateUserTable(u,Tel);
                    }
                    userSQL.close();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,Dictionary.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "用户名或密码不存在", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Register.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "前往注册", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {

    }


    //设置是否记住密码的勾选监听器
    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId()==R.id.rem_password){
                isRemember=isChecked;
            }
        }
    }


    @Override
    public void onFocusChange(View v,boolean hasFocus) {
        String phone=tel.getText().toString();
        if (v.getId()==R.id.pwd){
            if (phone.length()>0 && hasFocus) {
                User u= userSQL.findbyTel(phone);
                if (u!=null&&u.getRem()!=null) {
                    if (u.getRem().equals("是")) {
                        pwd.setText(u.getPassWord());
                    }
                }
            }
        }
    }

}

