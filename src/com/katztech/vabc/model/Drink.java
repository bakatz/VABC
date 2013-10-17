package com.katztech.vabc.model;

import java.text.DecimalFormat;

public class Drink implements Comparable<Drink> {
	private String displayName;
	private double numML;
	private int quantity;
	private double abvPct;
	private double price;
	private String category;
	private int containerType;
	private int age;

	@Override
	public String toString() {
		return displayName + " - " + new DecimalFormat("'$'0.00").format(price);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(abvPct);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + age;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + containerType;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		temp = Double.doubleToLongBits(numML);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + quantity;
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
		Drink other = (Drink) obj;
		if (Double.doubleToLongBits(abvPct) != Double
				.doubleToLongBits(other.abvPct))
			return false;
		if (age != other.age)
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (containerType != other.containerType)
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (Double.doubleToLongBits(numML) != Double
				.doubleToLongBits(other.numML))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	public Drink(String displayName) {
		this.displayName = displayName;
	}

	public Drink(String displayName, double numML, double abvPct, int quantity,
			double price, String category, int age, int containerType) {
		this.setDisplayName(displayName);
		this.setAbvPct(abvPct);
		this.setPrice(price);
		this.setNumML(numML);
		this.setCategory(category);
		this.setQuantity(quantity);
		this.setAge(age);
	}

	public static double computeABVFromProof(double proof) {
		return (4.0 / 7.0) * proof;
	}

	public static double ozToML(double oz) {
		return 29.5735 * oz; // 1 oz is 29.5735 ml
	}

	public static double LToML(double L) {
		return 1000.0 * L;
	}

	public static double QTToML(double qt) {
		return 946.353 * qt;
	}

	public double computeQuality() {
		return (quantity * numML * (abvPct / 100.0)) / price;
	}

	public double getAbvPct() {
		return abvPct;
	}

	public void setAbvPct(double abvPct) {
		this.abvPct = abvPct;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public double getNumML() {
		return numML;
	}

	public void setNumML(double numML) {
		this.numML = numML;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int compareTo(Drink other) {
		if (computeQuality() < other.computeQuality()) {
			return 1;
		} else if (computeQuality() > other.computeQuality()) {
			return -1;
		} else {
			return 0;
		}
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getContainerType() {
		return containerType;
	}

	public void setContainerType(int containerType) {
		this.containerType = containerType;
	}
}
