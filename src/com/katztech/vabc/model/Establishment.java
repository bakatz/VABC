package com.katztech.vabc.model;
import java.util.ArrayList;
import java.util.List;


public class Establishment {
	private String name;
	private int type;
	private int tagBinary;
	private String address;
	private String city;
	private String state;
	private int zip;
	private String phoneNum;
	private String hours;
	private List<Drink> drinkList;
	private double lat;
	private double lon;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static final int STORE_ESTABLISHMENT = 0;
	public static final int BAR_ESTABLISHMENT = 1;
	public static final int LIQUOR_STORE_ESTABLISHMENT = 2;
	
	public static final int ROMANTIC_TAG = 1;
	public static final int LATENIGHT_TAG = 2;
	public static final int LIVEMUSIC_TAG = 3;
	public static final int OUTDOOR_TAG = 4;
	public static final int SPORTS_TAG = 5;
	public static final int EASYTOTALK_TAG = 6;
	public static final int SEATING_TAG = 7;
	public static final int BARGAMES_TAG = 8;
	public static final int COLLEGE_TAG = 9;
	
	public Establishment(String name, int type, int id) {
		this.setName(name);
		this.setType(type);
		drinkList = new ArrayList<Drink>();
		this.lat = -1.0;
		this.lon = -1.0;
		this.id = id;
	}

	@Override
	public String toString() {
		return "Establishment [name=" + name + ", type=" + type
				+ ", drinkList=" + drinkList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((drinkList == null) ? 0 : drinkList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Establishment other = (Establishment) obj;
		if (drinkList == null) {
			if (other.drinkList != null)
				return false;
		} else if (!drinkList.equals(other.drinkList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Drink> getDrinkList() {
		return drinkList;
	}

	public void setDrinkList(List<Drink> drinkList) {
		this.drinkList = drinkList;
	}

	public int getTagBinary() {
		return tagBinary;
	}
	
	public boolean containsTag(int tag) {
		return (this.tagBinary | (1 << (tag - 1))) == this.tagBinary;
	}

	public void addTag(int tag) {
		this.tagBinary |= 1 << (tag - 1);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public void setTags(int tagBinary) {
		this.tagBinary = tagBinary;
	}
}
