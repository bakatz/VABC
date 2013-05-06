package com.katztech.vabc.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.katztech.vabc.model.Drink;
import com.katztech.vabc.view.DrinksTab;
import com.katztech.vabc.view.DrinksTab.DrinksAdapter;

public class DrinksController {
	private static final int DRINKS_REQ_LIMIT = 100; // only return 100 drinks from the server to save bandwidth; server max limit is 250
	private DrinksTab drinksView;
	private AsyncTask<Params, List<Drink>, List<Drink>> currTask;
	public DrinksController(DrinksTab drinksView) {
		this.currTask = null;
		this.drinksView = drinksView;
	}
	
	public void requestDrinksAsync(String category, String name, String sort, int size) {
		Params p = new Params();
		p.category = category;
		p.name = name;
		p.sort = sort;
		p.size = size;
		RequestDrinksTask newTask = new RequestDrinksTask();
		if(currTask != null) {
			currTask.cancel(true);
		}
		currTask = newTask;
		currTask.execute(p);
	}
	
	private class Params { 
		private String category;
		private String name;
		private String sort;
		private int size;
		public Params() {
		}
		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder("limit="+DRINKS_REQ_LIMIT);
			if(category != null) {
				ret.append("&category=");
				ret.append(category);
			}
			
			if(name != null && name.length() > 0) {
				ret.append("&name=");
				try {
					ret.append(URLEncoder.encode(name, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					ret.append("null");
					e.printStackTrace();
				}
			}
			
			if(sort != null) {
				ret.append("&sort=");
				ret.append(sort);
			}
			
			if(size > 0) {
				ret.append("&num_ml=");
				ret.append(size);
			}
			return ret.toString();
		}
	}
	
	private class RequestDrinksTask extends AsyncTask<Params, List<Drink>, List<Drink>> {

		@Override
		protected void onProgressUpdate(List<Drink>... progress) {
			if(isCancelled()) {
				return;
			}
		}

		
		@Override
		protected void onPreExecute() {
			DrinksAdapter drinksAdapter = (DrinksAdapter)drinksView.getListView().getAdapter();
			if(isCancelled()) {
				return;
			}
			drinksAdapter.beforeDataLoaded();
		}
		@Override
		protected void onPostExecute(List<Drink> result) {

			// System.out.println("Execution done, got our drinks, count: " + result.size());
			DrinksAdapter drinksAdapter = (DrinksAdapter)drinksView.getListView().getAdapter();
			if(isCancelled()) {
				return;
			} else {
				drinksAdapter.afterDataLoaded(result);
			}
		}

		@Override
		protected List<Drink> doInBackground(Params... params) { 
			List<Drink> drinkList = new ArrayList<Drink>();
			HttpClient httpClient = new DefaultHttpClient();

			try {
				String reqUrl = "http://bakatz.com/scripts/get_vabc_data.php?type=drinks&"+params[0].toString();
				// System.out.println("Sending requrl: " + reqUrl);
				if(isCancelled()) {
					drinkList.clear();
					return drinkList;
				}
				HttpResponse response = httpClient
						.execute(new HttpGet(reqUrl));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					String responseString = out.toString();
					JSONArray ja = new JSONArray(responseString);

					for(int i = 0; i < ja.length(); i++) {
						if(isCancelled()) {
							drinkList.clear();
							return drinkList;
						}
						JSONObject jo = (JSONObject)ja.get(i);
						String name = jo.getString("name");
						double numML = jo.getDouble("num_ml");
						double abvPct = jo.getDouble("abv_pct");
						int quantity = jo.getInt("quantity");
						double price = jo.getDouble("price");
						String category = jo.getString("category");
						int containerType = jo.getInt("container_type");
						int age = jo.getInt("age");
						
						drinkList.add(new Drink(name, numML, abvPct, quantity, price, category, age, containerType));
					}
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Network error encoutered while retreiving data in drinks");
			}
			return drinkList;
			//return false;
		}
	}
}
