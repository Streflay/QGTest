package com.example.end.view;

import static com.example.end.util.SearchAdapter.*;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.end.R;
import com.example.end.bean.Simulation;
import com.example.end.util.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private static final String TAG = "SearchBoxActivity";
    private EditText et_search;
    private ListView listView;
    private SearchAdapter searchAdapter;
    ArrayList<String> list1;

    private ArrayList<Simulation> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        et_search = findViewById(R.id.et_search);
        listView = findViewById(R.id.listView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            list1 = extras.getStringArrayList("key");
            for (String s : list1) {
                Simulation simulation = new Simulation(s);
                list.add(simulation);
            }
        }
        searchAdapter = new SearchAdapter(this, list, new SearchAdapter.FilterListener() {
            @Override
            public void getFilterData(List<Simulation> list) {
                //这里可以拿到过滤后的数据，所以在这里可以对搜索后的数据进行操作
                Log.e(TAG, "接口回调成功");
                Log.e(TAG, list.toString());
                setItemClick(list);
            }
        });
        listView.setAdapter(searchAdapter);
        setListeners();
    }

    private void setListeners() {
        setItemClick(list);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //每次EditText文本改变的时候，会回调这个方法
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  if (searchAdapter!=null) {
                      searchAdapter.getFilter().filter(charSequence);;
                  }
            }

            //每次EditText文本改变之后的时候，会回调这个方法
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setItemClick(List<Simulation> list) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Search.this, list.get(i).getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}