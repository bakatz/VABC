package com.katztech.vabc.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.katztech.vabc.R;
import com.katztech.vabc.controller.DrinksController;
import com.katztech.vabc.model.Drink;

public class DrinksTab extends SherlockListFragment {

	private ListView listView;
	private Spinner sortTypeSpinner;
	private Spinner drinkTypeSpinner;
	private Spinner drinkSizeSpinner;
	private EditText searchInputEditText;
	private DrinksController drinksController;
	public static final Drink LOADING_DRINK = new Drink("Loading . . .");
	private Map<String, String> sortTypeMap;
	private Map<String, Integer> drinkSizeMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_drinks, container, false);
		this.listView = (ListView) v.findViewById(android.R.id.list);
		this.sortTypeSpinner = (Spinner) v
				.findViewById(R.id.drink_sort_type_spinner);
		ArrayAdapter<CharSequence> drinkSortTypeAdapter = ArrayAdapter
				.createFromResource(v.getContext(),
						R.array.drink_sort_types_array,
						android.R.layout.simple_spinner_item);
		this.drinkTypeSpinner = (Spinner) v
				.findViewById(R.id.drink_type_spinner);
		ArrayAdapter<CharSequence> drinkTypeAdapter = ArrayAdapter
				.createFromResource(v.getContext(), R.array.drink_types_array,
						android.R.layout.simple_spinner_item);
		this.drinkSizeSpinner = (Spinner) v
				.findViewById(R.id.drink_size_spinner);
		ArrayAdapter<CharSequence> drinkSizeAdapter = ArrayAdapter
				.createFromResource(v.getContext(), R.array.drink_sizes_array,
						android.R.layout.simple_spinner_item);

		this.searchInputEditText = (EditText) v.findViewById(R.id.search_input);

		final SpinnerSelectedListener listen = new SpinnerSelectedListener();
		sortTypeSpinner.setAdapter(drinkSortTypeAdapter);
		sortTypeSpinner.post(new Runnable() {
			public void run() {
				sortTypeSpinner.setOnItemSelectedListener(listen);
			}
		});

		drinkTypeSpinner.setAdapter(drinkTypeAdapter);
		drinkTypeSpinner.post(new Runnable() {
			public void run() {
				drinkTypeSpinner.setOnItemSelectedListener(listen);
			}
		});

		drinkSizeSpinner.setAdapter(drinkSizeAdapter);
		drinkSizeSpinner.post(new Runnable() {
			public void run() {
				drinkSizeSpinner.setOnItemSelectedListener(listen);
			}
		});

		searchInputEditText
				.addTextChangedListener(new EditTextChangedListener());

		this.listView.setAdapter(new DrinksAdapter(listView.getContext()));
		if (drinksController == null) {
			drinksController = new DrinksController(this);
		}

		sortTypeMap = new HashMap<String, String>();
		sortTypeMap.put("Best Value", "value_score");
		sortTypeMap.put("Price", "price");
		sortTypeMap.put("Size", "num_ml");
		sortTypeMap.put("ABV", "abv_pct");
		sortTypeMap.put("Category Name", "category");
		sortTypeMap.put("Name", "name");
		sortTypeMap.put("Age", "age");

		drinkSizeMap = new HashMap<String, Integer>();
		drinkSizeMap.put("(All Sizes)", -1);
		drinkSizeMap.put("Handle (1.75L)", 1750);
		drinkSizeMap.put("1L", 1000);
		drinkSizeMap.put("Fifth (750mL)", 750);
		drinkSizeMap.put("375mL", 375);
		drinkSizeMap.put("50mL", 50);

		String sortType = sortTypeSpinner.getSelectedItem().toString();
		String category = drinkTypeSpinner.getSelectedItem().toString();
		String drinkSize = drinkSizeSpinner.getSelectedItem().toString();
		String searchText = searchInputEditText.getText().toString();

		// System.out.println("[Triggered by main] Got selected: " + sortType
		// 		+ " " + category + " " + drinkSize + " " + searchText);
		updateFilters(sortType, category, drinkSize, searchText);
		return v;
	}

	public ListView getListView() {
		return listView;
	}

	private void updateFilters(String sortType, String category,
			String drinkSize, String searchText) {

		String sortTypeValue = sortTypeMap.get(sortType);
		String categoryValue = category.toLowerCase(Locale.US);
		if (categoryValue.equals("(all types)")) {
			categoryValue = null;
		}
		int drinkSizeValue = drinkSizeMap.get(drinkSize);
		if (searchText == null || searchText.length() == 0) {
			searchText = null;
		}

		drinksController.requestDrinksAsync(categoryValue, searchText,
				sortTypeValue, drinkSizeValue);
	}

	private class SpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int idx, long arg3) {
			String sortType = sortTypeSpinner.getSelectedItem().toString();
			String category = drinkTypeSpinner.getSelectedItem().toString();
			String drinkSize = drinkSizeSpinner.getSelectedItem().toString();
			String searchText = searchInputEditText.getText().toString();

			System.out.println("[Triggered by spinner] Got selected: "
					+ sortType + " " + category + " " + drinkSize + " "
					+ searchText);
			updateFilters(sortType, category, drinkSize, searchText);

			// DrinksTab.this.drinksController.requestDrinksAsync(100, category,
			// searchText, sortType);
			// TODO Auto-generated method stub

		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}

	}

	private class EditTextChangedListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			String sortType = sortTypeSpinner.getSelectedItem().toString();
			String category = drinkTypeSpinner.getSelectedItem().toString();
			String drinkSize = drinkSizeSpinner.getSelectedItem().toString();
			String searchText = searchInputEditText.getText().toString();

			System.out.println("[Triggered by edit text] Got selected: "
					+ sortType + " " + category + " " + drinkSize + " "
					+ searchText);
			updateFilters(sortType, category, drinkSize, searchText);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	}

	public class DrinksAdapter extends ArrayAdapter<Drink> {

		public DrinksAdapter(Context con) {
			super(con, R.layout.row, R.id.label, new ArrayList<Drink>());
			add(LOADING_DRINK);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);

			// TODO: set icon based on what type of drink it is
			icon.setImageResource(R.drawable.ic_launcher);
			TextView size = (TextView) row.findViewById(R.id.size);
			if (this.getItem(position) != LOADING_DRINK) {
				Drink d = this.getItem(position);
				String age = d.getAge() <= 0 ? "" : "aged "
						+ Integer.toString(d.getAge()) + " years,";
				String catCapped = d.getCategory().substring(0, 1)
						.toUpperCase(Locale.US)
						+ d.getCategory().substring(1);
				size.setText(catCapped + ", " + age + " " + d.getNumML()
						+ " mL, " + d.getAbvPct() + "% ABV " + " at "
						+ new DecimalFormat("$0.00").format(d.getPrice()));

			}
			return (row);
		}

		public void beforeDataLoaded() {
			clear();
			add(LOADING_DRINK);
		}

		public void afterDataLoaded(List<Drink> result) {
			clear();
			for (Drink d : result) {
				add(d);
			}
			notifyDataSetChanged();
		}
	}
}