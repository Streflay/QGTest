package com.example.end.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.end.model.User;

import java.util.ArrayList;

//用户表
public class UserSQL {
    public static final String id="_id";
    public static final String tel="电话号码";
    public static final String passWord="密码";
    public static final String rem="记住密码";

    public static final String TAG="UserSQL";
    private DatabaseHelper uDbHelper;
    private SQLiteDatabase uDb;

    public static final String SQLLITE_TABLE="UserTable";
    private Context uCt;

    private class DatabaseHelper extends SQLiteOpenHelper {

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

        public boolean checkUserExist(String Tel){
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.query(UserSQL.SQLLITE_TABLE,new String[]{UserSQL.id},UserSQL.tel+"=?",new String[]{Tel},null,
                    null,null);
            boolean exists=cursor.getCount()>0;
            cursor.close();
            db.close();
            return exists;
        }

        public boolean checkUserExist(String Tel,String password){
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.query(UserSQL.SQLLITE_TABLE,new String[]{UserSQL.id},UserSQL.tel+"=? AND "+UserSQL.passWord+"=?",
                    new String[]{Tel,password},null,
                    null,null);
            boolean exists=cursor.getCount()>0;
            cursor.close();
            db.close();
            return exists;
        }

        public Cursor searchAccount(String query){
            SQLiteDatabase db=this.getReadableDatabase();
            String selection = tel+" LIKE ?";
            String[] selectionArgs={"%"+query+"%"};
            return db.query(SQLLITE_TABLE,null,selection,selectionArgs,null,null,null);
        }

        public User findbyTel(String Tel){
            User u=new User();
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.query(UserSQL.SQLLITE_TABLE,new String[]{id,tel,passWord,rem},UserSQL.tel+"=?",new String[]{Tel},null,
                    null,null);
            if (cursor!=null&&cursor.moveToFirst()) {
                u.setTel(cursor.getString(cursor.getColumnIndexOrThrow(tel)));
                u.setPassWord(cursor.getString(cursor.getColumnIndexOrThrow(passWord)));
                u.setRem(cursor.getString(cursor.getColumnIndexOrThrow(rem)));
                return u;
            }else{
                return null;
            }
        }

    }

    public User findbyTel(String Tel) {
        return uDbHelper.findbyTel(Tel);
    }

    public boolean checkUserExist(String Tel) {
        return uDbHelper.checkUserExist(Tel);
    }

    public boolean checkUserExist(String Tel,String password){
        return uDbHelper.checkUserExist(Tel,password);
    }

    public UserSQL(Context uCt) {
        this.uCt = uCt;
    }

    public UserSQL open() throws SQLException {
        uDbHelper=new DatabaseHelper(uCt);
        uDb=uDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if (uDbHelper!=null){
            uDbHelper.close();
        }
    }

    public long createUsers(User u) {
        long createResult=0;
        ContentValues initValues=new ContentValues();
        initValues.put(tel,u.getTel());
        initValues.put(passWord,u.getPassWord());
        initValues.put(rem,u.getRem());
        try {
            createResult=uDb.insert(SQLLITE_TABLE,null,initValues);
        } catch (Exception e){
            e.printStackTrace();
        }
        return createResult;
    }

    public long updateUserTable(User u,String Tel)
    {
        long createResult=0;
        ContentValues Values=new ContentValues();
        Values.put(rem,u.getRem());
        try {
            createResult=uDb.update(SQLLITE_TABLE,Values,UserSQL.tel+"=?",new String[]{Tel});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createResult;
    }


    public ArrayList<User> fetchAll() {
        ArrayList<User> allUser=new ArrayList<>();
        Cursor cursor=null;
        cursor=uDb.query(SQLLITE_TABLE,new String[]{id,tel,passWord,rem},null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                User u=new User();
                u.setTel(cursor.getString(cursor.getColumnIndexOrThrow(tel)));
                u.setPassWord(cursor.getString(cursor.getColumnIndexOrThrow(passWord)));
                u.setRem(cursor.getString(cursor.getColumnIndexOrThrow(rem)));
                allUser.add(u);
            } while (cursor.moveToNext());
        } if (cursor!=null && cursor.isClosed()) {
            cursor.close();
        }
        return allUser;
    }


}
