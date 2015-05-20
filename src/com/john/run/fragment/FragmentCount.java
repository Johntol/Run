package com.john.run.fragment;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
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

import com.john.run.R;
import com.john.run.step.StepDetector;
import com.john.run.step.StepService;

@SuppressLint("NewApi")
public class FragmentCount extends Fragment implements OnClickListener {

	private int total_step = 0;
	private Thread thread;
	private float calories = 0;
	private float step_length = 60; // centimeters
	private float weight = 60;
	private float mDistance = 0;

	private TextView step_value;
	private TextView calories_value;
	private TextView distance_value;
	
	private Button start;
	private Button quit;
	
	Intent intent;

	private View view;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.main, container, false);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();

	}

	/**
	 * 初始化
	 */
	private void init() {
		
		step_value = (TextView) view.findViewById(R.id.step_value);
		calories_value = (TextView) view.findViewById(R.id.calories_value);
		distance_value = (TextView) view.findViewById(R.id.distance_value);
		start = (Button) view.findViewById(R.id.start);
		quit  = (Button) view.findViewById(R.id.quit);
		
		//设置监听
		start.setOnClickListener(this);
		quit.setOnClickListener(this);
		
		Message msg = new Message();
		handler.sendMessage(msg);
		mThread();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			total_step = StepDetector.CURRENT_SETP;
			//步数计算
			step_value.setText(total_step + "");
			//卡路里消耗计算
			calories = (int) (weight * total_step * step_length * 0.01 * 0.01);
			calories_value.setText(calories + "");
			//路程计算
			mDistance = (float) (step_length * total_step / 100.0); // centimeters/meter
			String distance = String.format(new DecimalFormat("###.###").format(mDistance));

			distance_value.setText(distance);
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
		switch (v.getId()) {
		case R.id.start:
			intent = new Intent(getActivity(), StepService.class);
			getActivity().startService(intent);
			break;
			
		case R.id.quit:
			getActivity().stopService(intent);
		default:
			break;
		}
	}
	
	public int getTotal_step() {
		return total_step;
	}

}
