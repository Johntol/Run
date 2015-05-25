package com.john.run.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.john.run.db.RunDB;
import com.john.run.fragment.FragmentCount;
import com.john.run.model.Step;

public class AutoSaveService extends Service {
	
	private RunDB runDB;
	private Step step;
	private int userid;
	private String date;

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	private void init() {
		step = new Step();
		runDB = RunDB.getInstance(this);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		//初始化
		   init();
		//第一次开启
		 if(runDB.loadSteps(userid, date) == null){
			step.setNumber(0);
			step.setDate(date);
			step.setUserId(userid);
			step.setTarget(3000);
			runDB.saveStep(step);
			Toast.makeText(getApplicationContext(), "onStartCommand 第一次加载", Toast.LENGTH_SHORT).show();
		 }
		else{
			step.setNumber(StepDetector.CURRENT_SETP);
			step.setDate(date);
			step.setUserId(userid);
			step.setTarget(FragmentCount.target_step);
			Toast.makeText(getApplicationContext(), "onStartCommand 保存", Toast.LENGTH_SHORT).show();
			runDB.saveStep(step);
	     }
		return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
}
