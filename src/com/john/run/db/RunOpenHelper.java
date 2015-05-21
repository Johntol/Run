package com.john.run.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RunOpenHelper extends SQLiteOpenHelper {

	/**
	 * 创建步数表
	 */
	public static final String CREATE_STEP = "create table step("
			+ "id integer primary key autoincrement,"
			+ "number integer,"
			+ "date integer,"
			+ "userId integer)";
	
	/**
	 * 创建用户表
	 */
	public static final String CREATE_USER = "create table user("
			+ "id integer primary key autoincrement,"
			+ "name text,"
			+ "sex text,"
			+ "height integer,"
			+ "weight integer,"
			+ "birthday integer,"
			+ "sensitivity integer,"
			+ "step_length integer)";

	
	public RunOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STEP);
		db.execSQL(CREATE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
