package com.example.end.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DateBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context _context;
    public static final String DATABASE_NAME="BaseDatabase";
    public static final int VERSION=1;

    private static final String CREATE_USER="create table " +UserSQL.SQLLITE_TABLE +" ("
            +UserSQL.id+" integer primary key, "
            +UserSQL.tel+", "
            +UserSQL.passWord+", "
            +UserSQL.rem
            +")";

    private static final String CREATE_WORD = "create table " +WordSQl.SQLLITE_TABLE +" ("
            +WordSQl.id+" integer primary key, "
            +WordSQl.str+", "
            +WordSQl.dst+", "
            +WordSQl.user1+", "
            +WordSQl.user2+", "
            +WordSQl.user3+", "
            +WordSQl.user4+", "
            +WordSQl.user5
            +")";


    public DateBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        _context = context;

    }

    public DateBaseHelper(Context context,String name){
        super(context,name,null,VERSION);
        db=this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_WORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public DateBaseHelper open() throws SQLException
    {
        db=this.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public boolean execSQL(String sql){
        try {
            db.execSQL(sql);
        } catch (android.database.SQLException e) {
            Toast toast = Toast.makeText(_context,"android.database.sqlite.sOLiteException",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            Log.i("sqlerr 1og----->",e.toString());
            Log.i("err_sq1------->",sql);
            return false;
        }
        return true;
    }

    public boolean execSQL(String sql,boolean Throw){
        try {
            db.execSQL(sql);
        } catch (android.database.SQLException e) {
            if (Throw)
                throw e;
            return false;
        }
        return true;
    }
}
