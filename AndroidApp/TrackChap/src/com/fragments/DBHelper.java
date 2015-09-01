package com.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	Context context;
	public static DBHelper dbHelper;
	private final String sort_order = "ASC";
	SQLiteDatabase sqLiteDatabase;
	public static final String DB_NAME = "tracker";
	public static final int DB_VERSION = 1;
	public static final String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS notification(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "uid text NOT NULL,"
			+ "title VARCHAR,"
			+ "posted_on VARCHAR)";
	

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
		sqLiteDatabase = getWritableDatabase();
	}
//	public Cursor getAllItemRecords() {
//		return sqLiteDatabase.query("notification", new String[] { "_id", "uid","title",
//				"posted_on" }, null, null, null, null,
//				"item_position " + sort_order);
//	}
	public static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}
	public boolean updateItemPosition(long rowId, Integer position) {
		ContentValues ItemArgs = new ContentValues();
		ItemArgs.put("item_position", position);
		return sqLiteDatabase.update("notification", ItemArgs, "_id" + "=" + rowId,null) >= 0;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_NOTIFICATION_TABLE);
	}
	public boolean deleteItemRecord(long rowId) {
		return sqLiteDatabase.delete("notification", "_id" + "=" + rowId, null) > 0;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS CurrentPlayList");
		onCreate(db);

	}

	public long insert(String tableName, ContentValues contentValues) {
		return sqLiteDatabase.insert(tableName, null, contentValues);
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return sqLiteDatabase.update(table, values, whereClause, whereArgs);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return sqLiteDatabase.delete(table, whereClause, whereArgs);
	}

	public Cursor select(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return sqLiteDatabase.query(table, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

	public void closeDb() {
		sqLiteDatabase.close();
	}
}
