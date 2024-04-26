package com.example.end.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.end.R;
import com.example.end.dao.WordSQl;
import com.example.end.bean.Word;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Resite extends Fragment {
    View view;
    public RecyclerView list;
    Context context;
    private List<Word> words;
    WordSQl wordSQl;


    public class Adapter_Resite extends RecyclerView.Adapter <Adapter_Resite.ViewHolder> {

        private List<Word> words;
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView src;
            ImageButton btn;
            TextView btn0;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                src=itemView.findViewById(R.id.word);
                btn=itemView.findViewById(R.id.dst);
                btn0=itemView.findViewById(R.id.instead);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener!=null){
                            listener.OnClick(getAdapterPosition(),btn,btn0);
                        }
                    }
                });
            }
        }

        public Adapter_Resite(List<Word> words) {
            this.words = words;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
             Word w=words.get(position);
             holder.src.setText(w.getSrc());

        }

        @Override
        public int getItemCount() {
            return words.size();
        }

        Listener listener;

        public void setListener(Listener listener) {
            this.listener = listener;
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.recite,container,false);
        context=view.getContext();
        list=view.findViewById(R.id.recycleview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        wordSQl=new WordSQl(context);
        words=getWords();
        ArrayList<String> data = new ArrayList<>();
        for (Word word : words) {
            data.add(word.getSrc());
        }
        Button btn=view.findViewById(R.id.start_search);
        if (words!=null){
            Adapter_Resite resite=new Adapter_Resite(words);
            list.setAdapter(resite);
            resite.setListener(new Listener() {
                @Override
                public void OnClick(int position,ImageButton btn,TextView t) {
                    Word w=words.get(position);
                    btn.setContentDescription(w.getDst());
                    t.setText(w.getDst());
                }
            });
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent() ;
                intent.setClass(context,Search.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("key",data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private List<Word> getWords() {
        List<Word> words1=new ArrayList<>();
        wordSQl.open();
        words1=wordSQl.fetchAll();
        wordSQl.close();
        return words1;
    }


}
