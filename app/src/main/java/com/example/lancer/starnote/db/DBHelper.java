package com.example.lancer.starnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author: Lancer
 * date：2018/9/5
 * des:创建NoteBook的数据库，存储数据
 * email:tyk790406977@126.com
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String COOLNOTE_DATABASE_NAME = "NoteBook";

    public static final String NOTE_TABLE_NAME = "tyk_NoteBook";

    public static final String CREATE_NOTE_TABLE = "create table "
            + NOTE_TABLE_NAME
            + " (_id integer primary key autoincrement, objectid text, iid integer,"
            + " time varchar(10), date varchar(10), content text, color integer)";
    public DBHelper(Context context) {
        super(context, COOLNOTE_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE/*"create table tyk_NoteBook (_id integer primary key autoincrement,objectid text,iid integer,time varchar(10),date varchar(10),content text,color integer)"*/);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
