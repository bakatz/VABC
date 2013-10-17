package com.katztech.vabc.view;

import java.text.MessageFormat;
import java.util.Calendar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.katztech.vabc.R;
import com.katztech.vabc.model.Establishment;

public class EstablishmentDetailActivity extends SherlockActivity {
	private Establishment est;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setContentView(R.layout.activity_location_detail);
		est = (Establishment) this.getIntent().getSerializableExtra("location");
		TextView addressText = (TextView) findViewById(R.id.establishmentAddressText);
		addressText.setText(est.getAddress() + "\n" + est.getCity() + ", VA");

		MessageFormat phoneMsgFmt = new MessageFormat("({0}) {1}-{2}");
		String[] phoneNumArr = { est.getPhoneNum().substring(0, 3),
				est.getPhoneNum().substring(3, 6),
				est.getPhoneNum().substring(6) };

		TextView phoneText = (TextView) findViewById(R.id.phoneNumberText);
		phoneText.setText(phoneMsgFmt.format(phoneNumArr));

		Button callButton = (Button) findViewById(R.id.callButton);
		callButton.setOnClickListener(new CallListener());

		Button navButton = (Button) findViewById(R.id.navigateButton);
		navButton.setOnClickListener(new NavListener());

		computeHours();
	}

	private class CallListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String number = "tel:" + est.getPhoneNum();
			Intent callIntent = new Intent(Intent.ACTION_CALL,
					Uri.parse(number));
			startActivity(callIntent);
		}

	}

	private class NavListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("google.navigation:q=" + est.getLat() + ","
								+ est.getLon())));
			} catch (ActivityNotFoundException e) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						EstablishmentDetailActivity.this).create();
				alertDialog.setTitle("Error");
				alertDialog
						.setMessage("You don't have Google Navigation installed. Install it from the Play Store to enable routing to the selected ABC store.");
				alertDialog.setIcon(R.drawable.ic_launcher);
				alertDialog.show();
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		int itemId = menuItem.getItemId();
		switch (itemId) {
		case android.R.id.home:
			startActivity(new Intent(this, FragmentTabs.class));
			return true;
		}

		return false;
	}

	private void computeHours() {
		Calendar cal = Calendar.getInstance();
		TextView statusText = (TextView) findViewById(R.id.statusText);
		TextView infoText = (TextView) findViewById(R.id.infoText);

		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		// Sunday -- 1 pm to 6 pm
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			if (h < 13 || h > 18) {
				statusText.setText("CLOSED");
				statusText.setTextColor(Color.RED);
				// int hours = 8 - cal.get(Calendar.HOUR_OF_DAY);
				int mins = 60 - m;
				int hours = (h >= 18 ? 33 - h : 12 - h) + (mins == 60 ? 1 : 0);
				infoText.setText("This store will open in " + hours
						+ " hours and " + mins + " minutes.");
				return;
			} else {
				statusText.setText("OPEN");
				statusText.setTextColor(Color.GREEN);
				int mins = 60 - m;
				int hours = 20 - h + (mins == 60 ? 1 : 0);
				mins %= 60;
				infoText.setText("This store will close in " + hours
						+ " hours and " + mins + " minutes.");
				return;
			}
		} else { // All other days -- 10 am to 9 pm
			if (h < 10 || h >= 21) {
				statusText.setText("CLOSED");
				statusText.setTextColor(Color.RED);
				int mins = 60 - m;
				int hours = (h >= 21 ? 33 - h : 9 - h) + (mins == 60 ? 1 : 0);
				mins %= 60;
				infoText.setText("This store will open in " + hours
						+ " hours and " + mins + " minutes.");
				return;
			} else {
				statusText.setText("OPEN");
				statusText.setTextColor(Color.GREEN);
				int mins = 60 - m;
				int hours = 20 - h + (mins == 60 ? 1 : 0);
				mins %= 60;
				infoText.setText("This store will close in " + hours
						+ " hours and " + mins + " minutes.");
				return;
			}
		}

	}
}
