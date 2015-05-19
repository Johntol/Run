package com.john.run.step;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepDetector implements SensorEventListener{
	
	//当前步伐         
	public static int CURRENT_SETP = 0;
	//灵敏度
	public static float SENSITIVITY = 0;
	
	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;
	
	/**
	 * 最后加速度方向
	 * event.values[0]表示X轴方向上的加速度。
	   event.values[1]表示Y轴方向上的加速度。
       event.values[2]表示Z轴方向上的加速度。
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = {new float[3 * 2],new float[3 * 2]};
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;
	
	/**
	 * 传入上下文的构造函数
	 * 
	 * @param context
	 */
	public StepDetector(Context context){
		super();
		//初始化参数
		int h = 480;
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
		SENSITIVITY = 10;
	}
	
	/**
	 * 传感器改变
	 * @param event
	 */
	public void onSensorChanged(SensorEvent event){
		Sensor sensor = event.sensor;
		synchronized (this) {
			if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				float vSum = 0;
				for(int i = 0;i < 3; i++){
					final float v = mYOffset + event.values[i] * mScale[1];
					vSum += v;							
				}
				int k = 0;
				float v = vSum / 3;
				
				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {
					//方向改变
					int extType = (direction > 0 ? 0 : 1);
					mLastExtremes[extType][k] = mLastValues[k];
					float diff = Math.abs(mLastExtremes[extType][k]
							- mLastExtremes[1 - extType][k]);
					
					if (diff > SENSITIVITY) {
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
						boolean isNotContra = (mLastMatch != 1 - extType);
						
						if (isAlmostAsLargeAsPrevious
								&& isPreviousLargeEnough && isNotContra) {
							end = System.currentTimeMillis();
							if (end - start > 500) {
								// 此时判断为走了一步
								Log.i("StepDetector", "CURRENT_SETP:"
										+ CURRENT_SETP);
								CURRENT_SETP++;
								mLastMatch = extType;
								start = end;
							}
						} else {
							mLastMatch = -1;
						}
					}
					mLastDiff[k] = diff;
				}
				mLastDirections[k] = direction;
				mLastValues[k] = v;
				}
			}
		}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	
}
