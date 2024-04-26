package com.example.end.view;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.end.Dictionary;
import com.example.end.R;
import com.example.end.dao.WordSQl;
import com.example.end.bean.Word;
import com.example.end.util.TranslateResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fragment_TransLate extends Fragment implements View.OnClickListener {
    private LinearLayout beforeLay;//翻译之前的布局
    private Spinner Language;//语言选择下拉框
    private LinearLayout afterLay;//翻译之后的布局
    private TextView tvfrom;//翻译源语言
    private TextView tvTo;//翻译目标语言

    private Context context;
    View view;

    private EditText edContent;//输入框（要翻译的内容）
    private ImageView ivClearTx;//清空输入框按钮
    private TextView tvTranslation;//翻译

    private LinearLayout resultLay;//翻译结果布局
    private TextView Result;//翻译的结果
    private ImageView isCopyTx;//复制翻译的结果

    private String fromLanguage="auto"; //目标语言
    private String toLanguage="auto"; //翻译语言

    private ClipboardManager myClipboard; //复制文本
    private ClipData myClip; //剪辑数据

    private String appId="20240414002023974";
    private String key="zJv3A7FD1WkewYkg5Sqd";
    private WordSQl wordSQl;
    private Word w;


    private List<String> data=new LinkedList<>(Arrays.asList(
            "自动检测语言", "中文 → 英文", "英文 → 中文",
            "中文 → 繁体中文","中文 → 粤语","中文 → 日语",
            "中文 → 韩语", "中文 → 法语", "中文 → 俄语",
            "中文 → 阿拉伯语", "中文 → 西班牙语 ", "中文 → 意大利语"));
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.translate,container,false);
        this.context=getActivity();
        wordSQl=new WordSQl(context);
        wordSQl.open();
        initView();
        wordSQl.close();
        return view;
    }



    private void initView() {
        //设置亮色状态栏模式
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        beforeLay=view.findViewById(R.id.before_lay);
        Language=view.findViewById(R.id.sp_language);
        afterLay=view.findViewById(R.id.after_lay);

        tvfrom=view.findViewById(R.id.tv_from);
        tvTo=view.findViewById(R.id.tv_to);

        edContent=view.findViewById(R.id.ed_content);
        ivClearTx=view.findViewById(R.id.iv_clear_tx);
        tvTranslation=view.findViewById(R.id.tv_translation);

        resultLay=view.findViewById(R.id.result_lay);
        Result=view.findViewById(R.id.tv_result);
        isCopyTx=view.findViewById(R.id.iv_copy_tx);

        ivClearTx.setOnClickListener(this);
        isCopyTx.setOnClickListener(this);
        tvTranslation.setOnClickListener(this);

        adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Language.setAdapter(adapter);
        editTextListener();//输入框监听
        spinnerListener();//下拉框选择监听
        myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
    }

    private void editTextListener() {
        edContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivClearTx.setVisibility(View.VISIBLE);//显示清除按钮
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivClearTx.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ivClearTx.setVisibility(View.VISIBLE);
                String content=edContent.getText().toString().trim();
                if (content.isEmpty()) {
                    resultLay.setVisibility(View.GONE);
                    tvTranslation.setVisibility(View.VISIBLE);
                    beforeLay.setVisibility(View.VISIBLE);
                    afterLay.setVisibility(View.GONE);
                    ivClearTx.setVisibility(View.GONE);
                }
            }
        });
    }


    private void spinnerListener() {
        Language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0://自动检测
                        fromLanguage = "auto";
                        toLanguage = fromLanguage;
                        break;
                    case 1://中文 → 英文
                        fromLanguage = "zh";
                        toLanguage = "en";
                        break;
                    case 2://英文 → 中文
                        fromLanguage = "en";
                        toLanguage = "zh";
                        break;
                    case 3://中文 → 繁体中文
                        fromLanguage = "zh";
                        toLanguage = "cht";
                        break;
                    case 4://中文 → 粤语
                        fromLanguage = "zh";
                        toLanguage = "yue";
                        break;
                    case 5://中文 → 日语
                        fromLanguage = "zh";
                        toLanguage = "jp";
                        break;
                    case 6://中文 → 韩语
                        fromLanguage = "zh";
                        toLanguage = "kor";
                        break;
                    case 7://中文 → 法语
                        fromLanguage = "zh";
                        toLanguage = "fra";
                        break;
                    case 8://中文 → 俄语
                        fromLanguage = "zh";
                        toLanguage = "ru";
                        break;
                    case 9://中文 → 阿拉伯语
                        fromLanguage = "zh";
                        toLanguage = "ara";
                        break;
                    case 10://中文 → 西班牙语
                        fromLanguage = "zh";
                        toLanguage = "spa";
                        break;
                    case 11://中文 → 意大利语
                        fromLanguage = "zh";
                        toLanguage = "it";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "你未选择语言", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.iv_clear_tx:
                 edContent.setText("");
                 ivClearTx.setVisibility(View.GONE);
                 break;
             case R.id.iv_copy_tx:
                 String inviteCode=Result.getText().toString();
                 myClip=ClipData.newPlainText("text",inviteCode);
                 myClipboard.setPrimaryClip(myClip);
                 showMsg("已复制");
                 break;
             case R.id.tv_translation:
                 translation();
                 break;
             default:
                 break;
         }
    }

    private void translation() {
        String inputTx=edContent.getText().toString().trim();
        if (!inputTx.isEmpty()||!"".equals(inputTx)){
            tvTranslation.setText("翻译中...");
            tvTranslation.setEnabled(false);
            String salt=num(1);
            String spliceStr=appId+inputTx+salt+key;//根据百度要求，拼接字符串加密
            String sign=stringToMD5(spliceStr);
            asycnGet(inputTx,fromLanguage,toLanguage,salt,sign);
            w=new Word();
            w.setSrc(inputTx);
        }else{
            showMsg("请输入要翻译的内容");
        }
    }

    private void asycnGet(String content, String fromLanguage, String toLanguage, String salt, String sign) {
        String httpStr = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        String httpsStr = "https://fanyi-api.baidu.com/api/trans/vip/translate";
        //拼接请求的地址
        String url = httpsStr +
                "?appid=" + appId + "&q=" + content + "&from=" + fromLanguage + "&to=" +
                toLanguage + "&salt=" + salt + "&sign=" + sign;
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder().
                url(url).get().build();
        Call call =okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                goToUIThread(e.toString(),0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 goToUIThread(response.body().string(),1);
            }
        });
    }

    // object 接受一个返回对象
    // ket 正常还是异常
    private void goToUIThread(final Object object, final int key) {
        ((Dictionary)getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTranslation.setText("翻译");
                tvTranslation.setEnabled(true);

                if (key==0) {
                    showMsg("异常信息：" + object.toString());
                    Log.e("Dictionary",object.toString());
                }else{
                    final TranslateResult result=new Gson().fromJson(object.toString(),TranslateResult.class);
                    tvTranslation.setVisibility(View.GONE);
                    //显示翻译的结果
                    if (result.getTrans_result().get(0).getDst()==null){
                        showMsg("数据为空");
                    }
                    String end=result.getTrans_result().get(0).getDst();
                    w.setDst(end);
                    RecordW(w);
                    Result.setText(end);
                    resultLay.setVisibility(View.VISIBLE);
                    beforeLay.setVisibility(View.GONE);
                    afterLay.setVisibility(View.VISIBLE);
                    initAfter(result.getFrom(),result.getTo());
                }
            }
        });
    }

    private void RecordW(Word w) {
        wordSQl.open();
        wordSQl.createWord(w);
        wordSQl.close();
    }

    //将字符串转成MD5值
    private static String stringToMD5(String spliceStr) {
        byte[] hash;
        try {
            hash= MessageDigest.getInstance("MD5").digest(spliceStr.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex=new StringBuilder(hash.length*2);
        for (byte b : hash) {
            if ((b&0xFF)<0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b&0xFF));
        }
        return hex.toString();
    }

    //翻译成功后的语言显示
    private void initAfter(String from,String to){
        if (("zh").equals(from)) {
            tvfrom.setText("中文");
        } else if (("en").equals(from)) {
            tvfrom.setText("英文");
        } else if (("yue").equals(from)) {
            tvfrom.setText("粤语");
        } else if (("cht").equals(from)) {
            tvfrom.setText("繁体中文");
        } else if (("jp").equals(from)) {
            tvfrom.setText("日语");
        } else if (("kor").equals(from)) {
            tvfrom.setText("韩语");
        } else if (("fra").equals(from)) {
            tvfrom.setText("法语");
        } else if (("ru").equals(from)) {
            tvfrom.setText("俄语");
        } else if (("ara").equals(from)) {
            tvfrom.setText("阿拉伯语");
        } else if (("spa").equals(from)) {
            tvfrom.setText("西班牙语");
        } else if (("it").equals(from)) {
            tvfrom.setText("意大利语");
        }
        if (("zh").equals(to)) {
            tvTo.setText("中文");
        } else if (("en").equals(to)) {
            tvTo.setText("英文");
        } else if (("yue").equals(to)) {
            tvTo.setText("粤语");
        } else if (("cht").equals(to)) {
            tvTo.setText("繁体中文");
        } else if (("jp").equals(to)) {
            tvTo.setText("日语");
        } else if (("kor").equals(to)) {
            tvTo.setText("韩语");
        } else if (("fra").equals(to)) {
            tvTo.setText("法语");
        } else if (("ru").equals(to)) {
            tvTo.setText("俄语");
        } else if (("ara").equals(to)) {
            tvTo.setText("阿拉伯语");
        } else if (("spa").equals(to)) {
            tvTo.setText("西班牙语");
        } else if (("it").equals(to)) {
            tvTo.setText("意大利语");
        }
    }

    public static String num(int a) {
        Random r = new Random(a);
        int ran1 = 0;
        for (int i = 0; i < 5; i++) {
            ran1 = r.nextInt(100);
            System.out.println(ran1);
        }
        return String.valueOf(ran1);
    }

    private void showMsg(String msg) {
        Toast.makeText(context, "msg", Toast.LENGTH_SHORT).show();
    }
}
