package com.john.run.fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.john.run.R;
import com.john.run.db.RunDB;
import com.john.run.model.Step;
import com.john.run.service.AutoSaveService;
import com.john.run.service.StepDetector;
import com.john.run.service.StepService;

@SuppressLint("NewApi")
public class FragmentCount extends Fragment implements OnClickListener {

	/**
	 * Ŀ�경��
	 */
	public static  int target_step = 3000;
	private int total_step = 0;
	private Thread thread;
	private float calories = 0;
	private float step_length = 60; // centimeters
	private float weight = 60;
	private float mDistance = 0;
	private Step step;
	
	
	Drawable draw;
	
	//��ʼ�Ʋ� 2Ϊֹͣ�Ʋ�
	private int Type = 1;

	private TextView step_value;
	private TextView calories_value;
	private TextView distance_value;
	private TextView target_value;
	
	
	private Button start_stop;
	private Button save;
	private Button target_raise;
	private Button target_lower;
	
	Intent step_intent;
	Intent save_intent;
	
	private Calendar calendar;
	SimpleDateFormat sdf;
	private String today;

	private View view;
	
	private RunDB runDB;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.view = inflater.inflate(R.layout.main, container, false);
		init();
		
		//���ز���
		if((step = runDB.loadSteps(1, today)) != null){
			StepDetector.CURRENT_SETP = step.getNumber();
			target_step = step.getTarget();
		}
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		mThread();

	}

	/**
	 * ��ʼ��
	 */
	@SuppressLint("SimpleDateFormat")
	private void init() {
		
		step_value = (TextView) view.findViewById(R.id.step_value);
		calories_value = (TextView) view.findViewById(R.id.calories_value);
		distance_value = (TextView) view.findViewById(R.id.distance_value);
		start_stop = (Button) view.findViewById(R.id.start_stop);
		save  = (Button) view.findViewById(R.id.save);
		target_value = (TextView) view.findViewById(R.id.target);
		target_raise = (Button) view.findViewById(R.id.button_target_raise);
		target_lower =  (Button) view.findViewById(R.id.button_target_lower);
		
		//��ȡ����
		calendar = Calendar.getInstance();
		sdf = new SimpleDateFormat("yyyyMMdd");
		today = sdf.format(calendar.getTime());
		
		//�������ݿ��ļ�
		step = new Step();
		runDB = RunDB.getInstance(getActivity());

		//���ü���
		start_stop.setOnClickListener(this);
		save.setOnClickListener(this);
		target_raise.setOnClickListener(this);
		target_lower.setOnClickListener(this);
		
		Message msg = new Message();
		handler.sendMessage(msg);
		mThread();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			//����Ŀ��
			target_value.setText(target_step+"");
			
			total_step = StepDetector.CURRENT_SETP;
			//��������
			step_value.setText(total_step + "");
			//��·�����ļ���
			calories = (int) (weight * total_step * step_length * 0.01 * 0.01);
			calories_value.setText(calories + "");
			//·�̼���
			mDistance = (float) (step_length * total_step / 100.0); // centimeters/meter
			String distance = String.format(new DecimalFormat("###.###").format(mDistance));	
			distance_value.setText(distance);
			
			//��ʼ�Ʋ�
			if (Type == 1) {
				 start_stop.setText(R.string.start);		
				 draw = getResources().getDrawable(R.color.display_background_green);	
			}
			//ֹͣ�Ʋ�
			else  if (Type == 2){
				draw = getResources().getDrawable(R.color.display_background_orange);
				start_stop.setText(R.string.stop);			
			}
				start_stop.setBackground(draw);
		}
	};

	private void mThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (StepService.flag) {
							Message msg = new Message();
							handler.sendMessage(msg);
						}
					}
				}
			});
			thread.start();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			
			case R.id.start_stop:
				//��ʼ�Ʋ�
				if (Type == 1) {
					step_intent = new Intent(getActivity(), StepService.class);
					getActivity().startService(step_intent);
					Type = 2;
				} 
				//ֹͣ�Ʋ�
				else if (Type == 2) {
					Type = 1;
					getActivity().stopService(step_intent);
				}
				Message msg = new Message();
				handler.sendMessage(msg);
				break;
			
			case R.id.save:
				//�ڿ����Ʋ�����
				if(isServiceRunning(getActivity().getApplicationContext(), "com.john.run.service.StepService")){	
					start_stop.setText(R.string.start);		
					draw = getResources().getDrawable(R.color.display_background_green);
					start_stop.setBackground(draw);
					getActivity().stopService(step_intent);
					Toast.makeText(getActivity(), "ֹͣ�Ʋ�!", Toast.LENGTH_SHORT).show();
				}//û�����Ʋ�����
				//����
				save_intent = new Intent(getActivity(), AutoSaveService.class);	
				getActivity().startService(save_intent);
				Toast.makeText(getActivity(), "����ɹ�!", Toast.LENGTH_SHORT).show();
				getActivity().stopService(save_intent);
				break;
				
			case R.id.button_target_raise:
				target_step += 100;
				target_value.setText(target_step+"");
				break;
			case R.id.button_target_lower:
				if(target_step > 0){
				target_step -= 100;
				target_value.setText(target_step+"");
				}else{
					Toast.makeText(getActivity(), "�����Ŀ��Ҳ̫���˰�", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	
	}
	
	public int getTarget_step() {
		return target_step;
	}
	
	public int getTotal_step() {
		return total_step;
	}
	
	
	/**  
     * �����жϷ����Ƿ�����.  
     * @param context  
     * @param className �жϵķ�������  
     * @return true ������ false ��������  
     */  
    public static boolean isServiceRunning(Context mContext,String className) {   
        boolean isRunning = false;   
        ActivityManager activityManager = (ActivityManager)   
        mContext.getSystemService(Context.ACTIVITY_SERVICE);    
        List<ActivityManager.RunningServiceInfo> serviceList    
        = activityManager.getRunningServices(30);   
       if (!(serviceList.size()>0)) {   
            return false;   
        }   
        for (int i=0; i<serviceList.size(); i++) {   
            if (serviceList.get(i).service.getClassName().equals(className) == true) {   
                isRunning = true;   
                break;   
            }   
        }   
        return isRunning;   
    }   
	
	@Override
	public void onDestroy() {
		super.onDestroy();	
		step.setNumber(total_step);
		step.setTarget(target_step);
		step.setDate(today);
		step.setUserId(1);
		runDB.saveStep(step);
	}
	
	public Step savestep(Step step) {
		step = this.step;
		return step;

	}

}
