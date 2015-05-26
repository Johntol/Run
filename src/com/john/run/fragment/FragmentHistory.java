package com.john.run.fragment;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.john.run.R;
import com.john.run.db.RunDB;
import com.john.run.model.Step;

public class FragmentHistory extends Fragment implements OnClickListener {
	private AllAnimation ani;
	private View view;
	private ImageView iView;
	private TextView tView;
	private TextView number;
	private TextView ratio;
	private TextView aim;
	private ProgressBar progressBar;

	private DatePicker dPicker;
	private DatePickerDialog dialog;

	private Calendar calendar;
	private int year;
	private int month;
	private int day;
	private String date;

	private RunDB runDB;
	private Step step;

	private int count;
	private int progress;
	private int ratio1;
	private int target;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.history, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		insert();
	}

	@SuppressLint("SimpleDateFormat")
	private void init() {
		iView = (ImageView) view.findViewById(R.id.date_image);
		tView = (TextView) view.findViewById(R.id.date_text);
		number = (TextView) view.findViewById(R.id.number);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		ratio = (TextView) view.findViewById(R.id.ratio);
		aim = (TextView) view.findViewById(R.id.target);
		step = new Step();

		ani = new AllAnimation();
		ani.setDuration(2000);
		iView.setOnClickListener(this);

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		runDB = RunDB.getInstance(getActivity());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date1 = sdf.format(calendar.getTime());

		step = runDB.loadSteps(1, date1);
		view.startAnimation(ani);
		number.setText(count + "");
		progressBar.setProgress(progress);
		ratio.setText(ratio1 + "%");
		aim.setText(target+"");

	}

	/**
	 * 选择日期将其转换为字符串
	 */
	public void onClick(View arg0) {
		dialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {

			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				if (arg2+1 < 10 && arg3>10 ) {
					date = arg1 + "0" + (arg2 + 1)  + arg3;
				}
				else if (arg2+1 < 10 && arg3 < 10) {
					date = arg1 + "0" + (arg2 + 1) + "0" + arg3;
				}else if (arg2+1 >=10 &&arg3 < 10 ) {
					date = arg1 + "" + (arg2 + 1) + "0" + arg3;
				}else{
					date = arg1 + "" + (arg2 + 1) + "" + arg3;
				}
				tView.setText(date);
				queryStep();
			}
		}, year, month, day);
		dPicker = dialog.getDatePicker();
		dPicker.setSpinnersShown(false);
		dPicker.setCalendarViewShown(true);
		dialog.show();

	}

	private void insert() {
		Step step = new Step();
		step.setNumber(10000);
		step.setDate("20150504");
		step.setUserId(1);
		step.setTarget(4000);
		runDB.saveStep(step);

		step.setNumber(12000);
		step.setDate("20151201");
		step.setUserId(1);
		runDB.saveStep(step);

		step.setNumber(12000);
		step.setDate("20150502");
		step.setUserId(1);
		step.setTarget(2000);
		runDB.saveStep(step);

		step.setNumber(3000);
		step.setDate("20150503");
		step.setUserId(1);
		step.setTarget(3000);
		runDB.saveStep(step);
	}

	/**
	 * 查询选择日期所走的步数
	 */
	private void queryStep() {
		step = runDB.loadSteps(1, date);
		if (step != null) {
			Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
			progressBar.setProgress(0);
			number.setText(count + "");
			aim.setText(step.getTarget()+"");
			view.startAnimation(ani);

		}
	}

	private class AllAnimation extends Animation {
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				count = (int) (step.getNumber() * interpolatedTime);
				progress = (int) ((step.getNumber() / (double) 10000)
						* progressBar.getMax() * interpolatedTime);
				ratio1 = (int) ((step.getNumber() / (double) 10000) * 100 * interpolatedTime);
			} else {
				count = step.getNumber();
				progress = (int) ((step.getNumber() / (double) 10000) * progressBar
						.getMax());
				ratio1 = (int) ((step.getNumber() / (double) 10000) * 100);
			}
			view.postInvalidate();
			progressBar.setProgress(progress);
			number.setText(count + "");
			ratio.setText(ratio1 + "%");
			aim.setText(step.getTarget()+"");
		}
	}

}
