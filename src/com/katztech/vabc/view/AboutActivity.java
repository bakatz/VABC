package com.katztech.vabc.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.katztech.vabc.R;

public class AboutActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setContentView(R.layout.activity_about);
		TextView view = (TextView) findViewById(R.id.textView1);
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
		
	    int itemId = menuItem.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        startActivity(new Intent(this, FragmentTabs.class));
	        return true;
	    }

	    return false;
    }
	
	
}
