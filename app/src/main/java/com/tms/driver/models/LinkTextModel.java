package com.tms.driver.models;

/**
 * Generic model class to store values with provided data type
 * 
 * @author siddharth.brahmi
 * 
 * @param <T>
 */
public class LinkTextModel<T> {

	private T t;

	public void setT(T t) {
		this.t = t;
	}

	public T getT() {
		return t;
	}
}
