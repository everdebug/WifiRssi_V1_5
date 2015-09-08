package com.MyApk.wifirssi_v1_5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {

	private static String DB_NAME = "Pos.db";

	private static int DB_VERSION = 1;

	private SQLiteDatabase database;

	public MyDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	/***
	 * SQLlite数据库连接
	 */

	public SQLiteDatabase openConnection() {
		if (!database.isOpen()) {
			database = getWritableDatabase();
		}
		return database;
	}

	/***
	 * 关闭数据库
	 */

	public void closeConnection() {
		try {
			if (database != null && database.isOpen())
				database.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean createTable(String createTableSQL) {
		try {
			openConnection();
			database.execSQL(createTableSQL);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
		return true;
	}

	public boolean insert(String tableName, ContentValues values) {
		try {
			openConnection();
			database.insert(tableName, null, values);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
		return true;
	}
//getData()
	public List<Map<String, Object>> tableNameQuery() {
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		Cursor cursor = database.rawQuery("select * from sqlite_sequence ",
				null);
		Map<String, Object> map;
		while (cursor.moveToNext()) {
			// 遍历出表名
			map = new HashMap<String, Object>();
			map.put("title",cursor.getString(cursor.getColumnIndex("name")));
			map.put("info", cursor.getString(cursor.getColumnIndex("seq")));
			temp.add(map);
		}
		return temp;
	}

	public boolean deleteTable(String deleteTableSQL) {

		try {
			openConnection();
			database.execSQL(deleteTableSQL);// DROP TABLE MyTable
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
		return true;

	}
}
