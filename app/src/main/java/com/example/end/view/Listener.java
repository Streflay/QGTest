package com.example.end.view;

import android.widget.ImageButton;
import android.widget.TextView;

//实现RecycleView的点击事件
public interface Listener {
    void OnClick(int position, ImageButton btn,TextView t);
}
