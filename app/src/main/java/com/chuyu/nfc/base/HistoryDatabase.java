package com.chuyu.nfc.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 赵政 on 2018/11/6.
 */

public class HistoryDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String HISTORY="HISTORY";
    //三个不同参数的构造函数
    //带全部参数的构造函数，此构造函数必不可少
    public HistoryDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, name, factory, version);

    }
    //带两个参数的构造函数，调用的其实是带三个参数的构造函数
    public HistoryDatabase(Context context, String name){
        this(context,name,VERSION);
    }
    //带三个参数的构造函数，调用的是带所有参数的构造函数
    public HistoryDatabase(Context context, String name, int version){
        this(context, name,null,version);
    }
    //创建数据库
    public void onCreate(SQLiteDatabase db) {
        Log.i(HISTORY,"create a Database");
        //创建数据库sql语句
        String sql = "create table history(id int,name varchar(20))";
        //执行创建数据库操作
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //创建成功，日志输出提示
        Log.i(HISTORY,"update a Database");
    }

}
