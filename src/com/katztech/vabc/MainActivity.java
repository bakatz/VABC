package com.katztech.vabc;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.katztech.vabc.view.FragmentTabs;

public class MainActivity extends SherlockFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, FragmentTabs.class));
	}
	
	@Override
	public void onBackPressed() { 
		this.moveTaskToBack(true);
	}
}
