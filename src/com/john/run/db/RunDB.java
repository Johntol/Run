package com.john.run.db;

import com.john.run.model.Step;
import com.john.run.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RunDB {

	public static final String DB_NAME = "run.db";
	
	public static final int VERSION = 1;
	
	private static RunDB runDB;
	
	private SQLiteDatabase db;
	
	private RunDB(Context context){
		RunOpenHelper runHelper = new RunOpenHelper(context, DB_NAME, null, VERSION);
		db = runHelper.getWritableDatabase();
	}
	
	public synchronized static RunDB getInstance(Context context){
		if(runDB == null){
			runDB = new RunDB(context);
		}
		return runDB;
	}
	
	public void saveUser(User user){
		if(user != null){
			ContentValues values = new ContentValues();
			values.put("name", user.getName());
			values.put("sex", user.getSex());
			values.put("height", user.getHeight());
			values.put("weight", user.getWeight());
			values.put("birthday", user.getBirthday());
			values.put("sensitivity", user.getSensitivity());
			values.put("step_length", user.getStep_length());
			db.insert("user", null,values);
		}		
	}
	
	public void saveStep(Step step){
		if(step != null){
			ContentValues values = new ContentValues();
			values.put("number", step.getNumber());
			values.put("date", step.getDate());
			values.put("userId", step.getUserId());
			db.insert("step", null, values);
		}
	}
	
	/**
	 * 加载指定一天的步数
	 * @param userId
	 * @param date
	 * @return
	 */
	public Step loadSteps(int userId, int date){
		Step step = new Step();
		Cursor cursor = db.query("step", null, "userId=? and date = ?",
				new String[]{String.valueOf(userId),String.valueOf(date)},
				null, null, null);
		if(cursor.moveToFirst()){
			do{
				step.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
				step.setDate(cursor.getInt(cursor.getColumnIndex("date")));
				step.setId(userId);
			}while(cursor.moveToNext());
		}else{
			Log.i("tag", "step is null!");
		}
		
		return step;
	}
	
}
