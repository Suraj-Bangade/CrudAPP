package com.example.basiccrud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class CrudHelper {

    private DatabaseHelper dbHelper;

    public CrudHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insert(MyDataModel dataModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, dataModel.getName());
        values.put(DatabaseHelper.COLUMN_EMAIL, dataModel.getEmail());
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int update(MyDataModel dataModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, dataModel.getName());
        values.put(DatabaseHelper.COLUMN_EMAIL, dataModel.getEmail());
        int rowsAffected = db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dataModel.getId())});
        db.close();
        return rowsAffected;
    }

    public int delete(MyDataModel dataModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dataModel.getId())});
        db.close();
        return rowsAffected;
    }

    public List<MyDataModel> retrieve() {
        List<MyDataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                MyDataModel dataModel = new MyDataModel(id, name, email);
                dataList.add(dataModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }
}


