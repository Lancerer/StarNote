package com.example.lancer.starnote.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lancer.starnote.bean.NoteBookData;
import com.example.lancer.starnote.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Lancer
 * date：2018/9/5
 * des:NoteBook数据库的操作类
 * email:tyk790406977@126.com
 */

public class NoteDataDao {

    private DBHelper mDBHelper;

    public NoteDataDao(Context context) {
        super();
        mDBHelper = new DBHelper(context);
    }
    /**
     * 增
     *
     * @param data
     */
    public void insert(NoteBookData data) {
        String sql = "insert into " + DBHelper.NOTE_TABLE_NAME;

        sql += "(_id, objectid, iid, time, date, content, color) values(?, ?, ?, ?, ?, ?, ?)";

        SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[] { data.getId() + "",
                data.getIid() + "", data.getObjectId(), data.getUnixTime() + "", data.getDate(),
                data.getContent(), data.getColor() + "" });
        sqlite.close();
    }

    /**
     * 删
     *
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
        String sql = ("delete from " + DBHelper.NOTE_TABLE_NAME + " where _id=?");
        sqlite.execSQL(sql, new Integer[] { id });
        sqlite.close();
    }

    /**
     * 改
     *
     * @param data
     */
    public void update(NoteBookData data) {
        SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
        String sql = ("update " + DBHelper.NOTE_TABLE_NAME + " set iid=?, objectid=?, time=?, date=?, content=?, color=? where _id=?");
        sqlite.execSQL(sql,
                new String[] { data.getIid() + "", data.getObjectId() + "", data.getUnixTime() + "",
                        data.getDate(), data.getContent(),
                        data.getColor() + "", data.getId() + "" });
        sqlite.close();
    }

    public List<NoteBookData> query() {
        return query(" ");
    }

    /**
     * 查
     *
     * @param where
     * @return
     */
    public List<NoteBookData> query(String where) {
        SQLiteDatabase sqlite = mDBHelper.getReadableDatabase();
        ArrayList<NoteBookData> data = null;
        data = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DBHelper.NOTE_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NoteBookData notebookData = new NoteBookData();
            notebookData.setId(cursor.getInt(0));
            notebookData.setObjectId(cursor.getString(1));
            notebookData.setIid(cursor.getInt(2));
            notebookData.setUnixTime(cursor.getString(3));
            notebookData.setDate(cursor.getString(4));
            notebookData.setContent(cursor.getString(5));
            notebookData.setColor(cursor.getInt(6));
            data.add(notebookData);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 重置
     *
     * @param datas
     */
    public void reset(List<NoteBookData> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DBHelper.NOTE_TABLE_NAME);
            // 重新添加
            for (NoteBookData data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    /**
     * 保存一条数据到本地(若已存在则直接覆盖)
     *
     * @param data
     */
    public void save(NoteBookData data) {
        List<NoteBookData> datas = query(" where _id=" + data.getId());
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }


    public void destroy() {
        mDBHelper.close();
    }


}
