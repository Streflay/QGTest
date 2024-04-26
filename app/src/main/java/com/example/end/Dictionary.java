package com.example.end;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.end.dao.WordSQl;
import com.example.end.view.Fragment_Find;
import com.example.end.view.Fragment_Me;
import com.example.end.view.Fragment_Resite;
import com.example.end.view.Fragment_TransLate;

import java.util.ArrayList;
import java.util.List;

public class Dictionary extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3,btn4;
    ViewPager2 viewPager2;
    private FragmentManager fragmentManager;
    List<Fragment> fragmentList;

    fragementPageAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);
        viewPager2=findViewById(R.id.id_viewpager);
        initViewpager2(viewPager2);
        btn1=findViewById(R.id.btn_1);
        btn2=findViewById(R.id.btn_2);
        btn3=findViewById(R.id.btn_3);
        btn4=findViewById(R.id.btn_4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    private void initViewpager2(ViewPager2 viewPager2) {
        fragmentManager=getSupportFragmentManager();
        adapter=new fragementPageAdapter(fragmentManager,getLifecycle());
        fragmentList=new ArrayList<>();
        adapter.setFragmentList(fragmentList);
        fragmentList.add(new Fragment_TransLate());
        fragmentList.add(new Fragment_Resite());
        fragmentList.add(new Fragment_Find());
        fragmentList.add(new Fragment_Me());
        viewPager2.setAdapter(adapter);
    }

    private void init(ViewPager2 viewPager2,List<Fragment> list) {
        fragmentManager=getSupportFragmentManager();
        adapter=new fragementPageAdapter(fragmentManager,getLifecycle());
        adapter.setFragmentList(list);
        viewPager2.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_1:
                viewPager2.setCurrentItem(0);
                break;
            case R.id.btn_2:
                Fragment_Resite resite = new Fragment_Resite();
                fragmentList.set(1,resite);
                init(viewPager2,fragmentList);
                viewPager2.setCurrentItem(1);
                break;
            case R.id.btn_3:
                viewPager2.setCurrentItem(2);
                break;
            case R.id.btn_4:
                viewPager2.setCurrentItem(3);
                break;
        }
    }
}
