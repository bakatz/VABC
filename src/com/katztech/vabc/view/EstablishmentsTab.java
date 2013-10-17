package com.katztech.vabc.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.katztech.vabc.R;
import com.katztech.vabc.controller.LocationsController;
import com.katztech.vabc.model.Establishment;

//import android.widget.TextView;

public class EstablishmentsTab extends SherlockFragment {

	private SupportMapFragment fragment;
	private GoogleMap map;
	private List<Establishment> estList;
	private LocationsController locController;

	public GoogleMap getMap() {
		return map;
	}

	public List<Establishment> getEstList() {
		return estList;
	}

	public synchronized void setEstList(List<Establishment> estList) {
		this.estList = estList;
		for(int i = 0; i < estList.size(); i++) {
			Establishment currEst = estList.get(i);
			map.addMarker(new MarkerOptions()
	        .position(new LatLng(currEst.getLat(), currEst.getLon()))
	        .title(currEst.getName() + " " + i)
	        .snippet("Tap to view details about " + currEst.getAddress()));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_locations, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm
				.findFragmentById(R.id.map_placeholder);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map_placeholder, fragment)
					.commit();
		}

		if (estList == null) {
			estList = new ArrayList<Establishment>();
		}

		if (locController == null) {
			locController = new LocationsController(this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (map == null) {
			map = fragment.getMap();

			map.setMyLocationEnabled(true);
			map.setOnMyLocationChangeListener(new LocationChangeListener());
			map.setOnInfoWindowClickListener(new LocationClickListener());
			// map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
		}

		if (estList.isEmpty()) {
			locController.requestLocationsAsync();
			// new GetLocationsTask().execute();
		}
	}

	public class LocationChangeListener implements OnMyLocationChangeListener {
		private boolean hasMovedToLocation;

		public LocationChangeListener() {
			hasMovedToLocation = false;
		}

		@Override
		public void onMyLocationChange(Location location) {
			if (location != null && !hasMovedToLocation
					&& location.getLongitude() < 0) { // why does my gps report
														// first loc as indian
														// ocean?!
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						location.getLatitude(), location.getLongitude()), 14.0f));
				hasMovedToLocation = true; // only move to our current location
											// once
			}
		}

	}

	public class LocationClickListener implements OnInfoWindowClickListener {

		@Override
		public void onInfoWindowClick(Marker marker) {
			Intent locationDetailIntent = new Intent(EstablishmentsTab.this.getActivity(), EstablishmentDetailActivity.class);
			int idx = Integer.parseInt(marker.getTitle().split(" ")[2]);
			Establishment estFound = estList.get(idx);
			locationDetailIntent.putExtra("location", estFound);
			startActivity(locationDetailIntent);
		}

	}
}
