package com.katztech.vabc.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import com.katztech.vabc.model.Establishment;
import com.katztech.vabc.view.EstablishmentsTab;

public class LocationsController {
	private EstablishmentsTab locView;
	private RequestLocationsTask currTask;

	public LocationsController(EstablishmentsTab locView) {
		this.locView = locView;
	}

	public void requestLocationsAsync() {

		RequestLocationsTask newTask = new RequestLocationsTask();

		if (currTask != null) {
			currTask.cancel(true);
		}
		currTask = newTask;
		currTask.execute();
	}

	private class RequestLocationsTask extends
			AsyncTask<Integer, List<Establishment>, List<Establishment>> {

		@Override
		protected void onProgressUpdate(List<Establishment>... progress) {
			if(isCancelled()) {
				return;
			}
		}

		@Override
		protected void onPostExecute(List<Establishment> result) {
			// System.out
			// .println("Execution done, got our locs, starting map. Loc count: "
			// + result.size());
			if(isCancelled()) {
				return;
			}
			locView.setEstList(result);
		}

		@Override
		protected List<Establishment> doInBackground(Integer... params) {
			// System.out.println("Got params: " + params);
			List<Establishment> estList = locView.getEstList();
			if (!estList.isEmpty()) {

				return estList;
			}
			List<Establishment> locList = new ArrayList<Establishment>();
			HttpClient httpClient = new DefaultHttpClient();
			try {
				HttpResponse response = httpClient
						.execute(new HttpGet(
								"http://bakatz.com/scripts/get_vabc_data.php?type=establishments"));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					String responseString = out.toString();
					JSONArray ja = new JSONArray(responseString);

					for (int i = 0; i < ja.length(); i++) {
						if(isCancelled()) {
							locList.clear();
							return locList;
						}
						JSONObject jo = (JSONObject) ja.get(i);
						String name = jo.getString("name");
						String city = jo.getString("city");
						String state = "VA";
						String street = jo.getString("street");
						String phoneNo = jo.getString("phone_number");
						double lat = jo.getDouble("lat");
						double lon = jo.getDouble("lon");

						Establishment currEs = new Establishment(name, 0, 0);
						currEs.setState(state);
						currEs.setAddress(street);
						currEs.setLat(lat);
						currEs.setLon(lon);
						currEs.setPhoneNum(phoneNo);
						currEs.setCity(city);

						locList.add(currEs);

					}

				} else {
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				System.out.println("Network error encoutered while retreiving data in locations");
			}
			return locList;
		}
	}
}
