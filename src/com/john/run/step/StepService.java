package com.john.run.step;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class StepService extends Service {

	public static Boolean flag = false; 
	//管理传感器
	private SensorManager mSensorManager;
	private StepDetector mStepDetector;
	//电源管理
	private PowerManager powerManager;
	//电源管理锁机制
	private WakeLock wakeLock;
	
	private Sensor mSensor;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		flag = true;
		mStepDetector = new StepDetector(this);
		//取得SensorManager
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		//注册Detector
		registerDetector();
		
		
		//创建PowerManager.WakeLock实例
		powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK |
				 PowerManager.ACQUIRE_CAUSES_WAKEUP, "John");
		//获取相应的锁
		wakeLock.acquire();
	}
	
	@Override
	public void onDestroy() {
        flag = false;
		if(mStepDetector != null){
			StepService.this.unregisterDetector();
			// Stop detecting
			mSensorManager.unregisterListener(mStepDetector);
		}
		
		if(wakeLock != null){
			wakeLock.release();
		}
		super.onDestroy();
	}

	
    /**
     * 注册Detector
     */
    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER );
        mSensorManager.registerListener(mStepDetector,
            mSensor,
            SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    /**
     * 取消注册Detector
     */
    private void unregisterDetector() {
        mSensorManager.unregisterListener(mStepDetector);
    }
    
}
