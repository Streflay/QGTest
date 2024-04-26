package com.example.end.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.end.bean.Word;

import java.util.ArrayList;


//单词表
public class WordSQl {
    public static final String id="_id";
    public static final String str="原文";
    public static final String dst="译文";
    public static final String user1="用户1";
    public static final String user2="用户2";
    public static final String user3="用户3";
    public static final String user4="用户4";
    public static final String user5="用户5";

    public static final String TAG="WordSQL";
    private WordSQl.DatabaseHelper wDbHelper;
    private SQLiteDatabase wDb;
    public static final String SQLLITE_TABLE="WordSQL";
    private Context wCt;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context) {
            super(context, DateBaseHelper.DATABASE_NAME, null, DateBaseHelper.VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int olderVersion, int newVersion) {
            Log.w(TAG,"Upgrading database from version "+olderVersion+" to "
                    +newVersion+",which will destroy all older data");
            db.execSQL("drop table if exists "+SQLLITE_TABLE);
            onCreate(db);
        }
    }

    public WordSQl(Context wCt) {
        this.wCt=wCt;
    }

    public WordSQl open() throws SQLException {
        wDbHelper=new WordSQl.DatabaseHelper(wCt);
        wDb=wDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if (wDbHelper!=null){
            wDbHelper.close();
        }
    }

    //创建表字段
    public long createWord(Word w){
        long createResult=0;
        ContentValues initValues=new ContentValues();
        initValues.put(str,w.getSrc());
        initValues.put(dst,w.getDst());
        initValues.put(user1,w.getUser1());
        initValues.put(user2,w.getUser2());
        initValues.put(user3,w.getUser3());
        initValues.put(user4,w.getUser4());
        initValues.put(user5,w.getUser5());
        try {
            createResult=wDb.insert(SQLLITE_TABLE,null,initValues);
        } catch (Exception e){
            e.printStackTrace();
        }
        return createResult;
    }

    //根据名字删（用于注销账号）
    public boolean deleteBossByName(String Name){
        int isDelete;
        String[] tName;
        tName=new String[]{Name};
        isDelete=wDb.delete(SQLLITE_TABLE,user1+"=?",tName);
        Log.e("DeleteAccount","isDelete:"+isDelete+"||"+"AccountName"+Name);
        return isDelete>0;
    }

    public ArrayList<Word> fetchAll(){
        ArrayList<Word> words=new ArrayList<>();
        Cursor wCursor=null;
        wCursor=wDb.query(SQLLITE_TABLE,new String[]{id,str,dst,user1,user2,user3,user4,user5
                },null,null,null,null,null,null);
        if (wCursor.moveToFirst()){
            do {
                Word w=new Word();
                w.setSrc(wCursor.getString(wCursor.getColumnIndexOrThrow(str)));
                w.setDst(wCursor.getString(wCursor.getColumnIndexOrThrow(dst)));
                w.setUser1(wCursor.getString(wCursor.getColumnIndexOrThrow(user1)));
                w.setUser2(wCursor.getString(wCursor.getColumnIndexOrThrow(user2)));
                w.setUser3(wCursor.getString(wCursor.getColumnIndexOrThrow(user3)));
                w.setUser4(wCursor.getString(wCursor.getColumnIndexOrThrow(user4)));
                w.setUser5(wCursor.getString(wCursor.getColumnIndexOrThrow(user5)));
                words.add(w);
            } while (wCursor.moveToNext());
        }
        if (wCursor!=null&&!wCursor.isClosed()){
            wCursor.close();
        }
        return words;
    }
}
