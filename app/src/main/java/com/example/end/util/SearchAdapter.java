package com.example.end.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.end.R;
import com.example.end.bean.Simulation;
import com.example.end.bean.Word;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Simulation> list = new ArrayList<>();
    private MyFilter filter;
    private FilterListener listener=null;

    public SearchAdapter(Context context, ArrayList<Simulation> list, FilterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final ViewHold hold;
            if (convertView == null) {
                hold = new ViewHold();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
                hold.tv_simulation = convertView.findViewById(R.id.tv_simulation);
                convertView.setTag(hold);
            } else {
                hold = (ViewHold) convertView.getTag();
            }
            Simulation simulation = list.get(position);
            hold.tv_simulation.setText(simulation.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new MyFilter(list);
        }
        return filter;
    }

    class MyFilter extends Filter {
        //创建集合保存原始数据
        private ArrayList<Simulation> original = new ArrayList<>();

        public MyFilter(ArrayList<Simulation> original) {
            this.original = original;
        }

        //该方法返回搜索过滤后的数据
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //创建FilterResults对象
            FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                //取出当前的数据源的值和集合元素个数
                //此时返回的filterResults就是原始的数据，不进行过滤
                filterResults.values = original;
                filterResults.count = original.size();
            } else {
                ArrayList<Simulation> mList = new ArrayList<>();
                //创建集合保护过滤后的数据
                for (Simulation s : original) {
                    //这里的toLowerCase():是将字符串中的字母全部变为小写，而非字母则不做改变
                    if (s.getText().trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                        //规则匹配的话就往集合中添加该数据
                        mList.add(s);
                    }
                }
                filterResults.values = mList;
                filterResults.count = mList.size();
            }
            return filterResults;
        }

        //该方法用来刷新用户界面，根据过滤后的数据重新展示列表
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //获取过滤后的数据
            list = (ArrayList<Simulation>) results.values;
            //如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候重写的方法里执行
            if (listener != null) {
                listener.getFilterData(list);
            }
            //刷新数据源显示
            //通知数据观察者当前所关联的数据源已经发生改变，任何与该数据有关的视图都应该去刷新自己。
            notifyDataSetChanged();
        }
    }

    public interface FilterListener{
        void getFilterData(List<Simulation> list);
    }

    public final class ViewHold {
        private TextView tv_simulation;
    }

}
