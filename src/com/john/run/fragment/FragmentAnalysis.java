package com.john.run.fragment;


import java.util.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.john.run.R;
import com.john.run.db.RunDB;
import com.john.run.model.Step;
import com.john.run.widet.HistogramView;

public class FragmentAnalysis extends Fragment implements OnClickListener{	
	private HistogramView hv1;
	private HistogramView hv2;
	private HistogramView hv3;
	private HistogramView hv4;
	private HistogramView hv5;
	private HistogramView hv6;
	private HistogramView hv7;
	
	private RunDB runDB;
	
	private TextView day1;
	private TextView day2;
	private TextView day3;
	private TextView day4;
	private TextView day5;
	private TextView day6;
	private TextView day7;
	
	//平均步数
	private TextView average_step;
	//总步数
	private TextView sum_step;
	
	private Step step;
	private String day;
	private Calendar calendar;
	
	private int average = 0;
	private int sum = 0;
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//加载动画
		view  = inflater.inflate(R.layout.analyze, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//创建Activity
		super.onActivityCreated(savedInstanceState);
		//初始化
		init();
		setWeek();
		
	}
	
	private void setWeek() {
		int day = calendar.get(Calendar.DAY_OF_WEEK);
//		Toast.makeText(getActivity(), day + "", Toast.LENGTH_LONG).show();
		day1.setText(week(day));
		day2.setText(week(day - 1));
		day3.setText(week(day - 2));
		day4.setText(week(day - 3));
		day5.setText(week(day - 4));
		day6.setText(week(day - 5));
		day7.setText(week(day - 6));
	}
	private String week(int day) {
		if(day < 1){
			day += 7;
		}
		switch(day){	
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		case 7:
			return "周六";
		default:
			return "";		
		}
		
	}
	/**
	 * 初始化
	 */
	private void init()
	{
		average_step = (TextView) view.findViewById(R.id.average_step);
		sum_step = (TextView) view.findViewById(R.id.sum_step);
		
		calendar = Calendar.getInstance();
		
		hv1 = (HistogramView) view.findViewById(R.id.map1);
		hv2 = (HistogramView) view.findViewById(R.id.map2);
		hv3 = (HistogramView) view.findViewById(R.id.map3);
		hv4 = (HistogramView) view.findViewById(R.id.map4);
		hv5 = (HistogramView) view.findViewById(R.id.map5);
		hv6 = (HistogramView) view.findViewById(R.id.map6);
		hv7 = (HistogramView) view.findViewById(R.id.map7);
		hv1.setProgress(0.1);
		hv2.setProgress(0.9);
		hv3.setProgress(0.6);
		hv4.setProgress(0.8);
		hv5.setProgress(0.5);
		hv6.setProgress(0.6);
		hv7.setProgress(0.7);
		hv1.setOnClickListener(this);
		hv2.setOnClickListener(this);
		hv3.setOnClickListener(this);
		hv4.setOnClickListener(this);
		hv5.setOnClickListener(this);
		hv6.setOnClickListener(this);
		hv7.setOnClickListener(this);	
		
		day1 = (TextView) view.findViewById(R.id.Monday);
		day2 = (TextView) view.findViewById(R.id.Tuesday);
		day3 = (TextView) view.findViewById(R.id.Wednesday);
		day4 = (TextView) view.findViewById(R.id.Thursday);
		day5 = (TextView) view.findViewById(R.id.Friday);
		day6 = (TextView) view.findViewById(R.id.Saturday);
		day7 = (TextView) view.findViewById(R.id.Sunday);
	}
	
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.map1:
			hv1.setText(true);
			hv2.setText(false);
			hv3.setText(false);
			hv4.setText(false);
			hv5.setText(false);
			hv6.setText(false);
			hv7.setText(false);
			break;
		case R.id.map2:
			hv1.setText(false);
			hv2.setText(true);
			hv3.setText(false);
			hv4.setText(false);
			hv5.setText(false);
			hv6.setText(false);
			hv7.setText(false);
			break;
		case R.id.map3:
			hv1.setText(false);
			hv2.setText(false);
			hv3.setText(true);
			hv4.setText(false);
			hv5.setText(false);
			hv6.setText(false);
			hv7.setText(false);
			break;
		case R.id.map4:
			hv1.setText(false);
			hv2.setText(false);
			hv3.setText(false);
			hv4.setText(true);
			hv5.setText(false);
			hv6.setText(false);
			hv7.setText(false);
			break;
		case R.id.map5:
			hv1.setText(false);
			hv2.setText(false);
			hv3.setText(false);
			hv4.setText(false);
			hv5.setText(true);
			hv6.setText(false);
			hv7.setText(false);
			break;
		case R.id.map6:
			hv1.setText(false);
			hv2.setText(false);
			hv3.setText(false);
			hv4.setText(false);
			hv5.setText(false);
			hv6.setText(true);
			hv7.setText(false);
			break;
		case R.id.map7:
			hv1.setText(false);
			hv2.setText(false);
			hv3.setText(false);
			hv4.setText(false);
			hv5.setText(false);
			hv6.setText(false);
			hv7.setText(true);
			break;

		default:
			break;
		}
		
	}

}
