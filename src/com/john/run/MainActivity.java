package com.john.run;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;

import com.john.run.fragment.FragmentAdapter;
import com.john.run.fragment.FragmentAnalysis;
import com.john.run.fragment.FragmentCount;
import com.john.run.fragment.FragmentHistory;
import com.john.run.service.AutoSaveService;
import com.john.run.service.StepService;

public class MainActivity extends Activity {
	//
	public List<Fragment> fragments = new ArrayList<Fragment>();
	private RadioGroup rgs;
	Intent step_intent;
	Intent save_intent;
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.page_mian);
		
		rgs = (RadioGroup) findViewById(R.id.radioGroup);
		
		fragments.add(new FragmentHistory());
		fragments.add(new FragmentAnalysis());
		fragments.add(new FragmentCount());
		
		new FragmentAdapter(this, fragments, R.id.Fragment, rgs);

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		save_intent = new Intent(this, AutoSaveService.class);	
		startService(save_intent);
		stopService(save_intent);
	} 

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		step_intent = new Intent(this, StepService.class);
		stopService(step_intent);
		//防止忘记按保存直接退出
		save_intent = new Intent(this, AutoSaveService.class);	
		startService(save_intent);
		stopService(save_intent);
		}
}
