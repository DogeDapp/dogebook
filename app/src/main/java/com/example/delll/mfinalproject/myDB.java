package com.example.delll.mfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hj on 2016/11/22.
 */

public class myDB extends SQLiteOpenHelper{
    private static final String DB_NAME = "mydb.db";
    private static final String TABLE_NAME = "mydb_table";
    private static final int DB_VERSION = 1;



    public myDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO 创建数据库后，对数据库的操作
        String  CREATE_TABLE="CREATE TABLE if not exists "
                + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY,name TEXT,pages TEXT,page TEXT,date TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        // 添加数据，使用execSQL()方法

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
        // Android要求重写才能实例化
    }

    public void insert2DB(String name, String pages, String page, String date) {
        SQLiteDatabase db = getWritableDatabase();
        // ContentValues类是一个数据承载容器，主要用来向数据库表中添加一条数据
        // 创建新的一行
        ContentValues cv=new ContentValues();
        cv.put("name", name);
        cv.put("pages", pages);
        cv.put("page", page);
        cv.put("date", date);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void upDate2DB(String name, String pages,String page, String date) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name", name);
        cv.put("pages", pages);
        cv.put("page", page);
        cv.put("date", date);
        String whereClause="name = ?";
        String[] whereArgs={name};
        db.update(TABLE_NAME, cv, whereClause, whereArgs);
        // update（）方法有四个参数，分别是表名，表示列名和值的 ContentValues 对象
        // 可选的 WHERE 条件和可选的填充 WHERE 语句的字符串


    }

    public void delete2DB(String name) {
        SQLiteDatabase db=this.getWritableDatabase();

        String whereClause="name = ?";
        String[] whereArgs={name};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public Cursor queryALL() {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " +  TABLE_NAME , null);
        db.close();
        return cur;
    }


    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {"name", "pages","page", "date"}, null, null, null, null, null);
        return cursor;
    }


    public boolean query2DB(String name) {
        boolean flag = false;
        SQLiteDatabase db=this.getReadableDatabase();
        String[] whereArgs={name};
        Cursor cur = db.rawQuery("select * from " +  TABLE_NAME + " WHERE name=?", whereArgs);
        while(cur.moveToNext()) {
            flag = true;
        }
        db.close();
        return flag;
    }


}
