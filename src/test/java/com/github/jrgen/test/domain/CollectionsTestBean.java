package com.github.jrgen.test.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsTestBean {
	
	private List<Integer> intList;
	private Map<String, Integer> strIntMap;
	private Set<Integer> intSet;
	@SuppressWarnings("rawtypes")
	private Set stringSet;
	private Set<? extends Number> numSet;
	private Set<List<Integer>> setListInt;
	private int primitiveInt;
	
	public List<Integer> getIntList() {
		return intList;
	}
	
	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}
	
	public Map<String, Integer> getStrIntMap() {
		return strIntMap;
	}
	
	public void setStrIntMap(Map<String, Integer> strIntMap) {
		this.strIntMap = strIntMap;
	}
	
	public Set<Integer> getIntSet() {
		return intSet;
	}
	
	public void setIntSet(Set<Integer> intSet) {
		this.intSet = intSet;
	}

	public Set<? extends Number> getNumSet() {
		return numSet;
	}

	public void setNumSet(Set<? extends Number> numSet) {
		this.numSet = numSet;
	}

	public int getPrimitiveInt() {
		return primitiveInt;
	}

	public void setPrimitiveInt(int primitiveInt) {
		this.primitiveInt = primitiveInt;
	}

	@SuppressWarnings("rawtypes")
	public Set getStringSet() {
		return stringSet;
	}

	public void setStringSet(@SuppressWarnings("rawtypes") Set stringSet) {
		this.stringSet = stringSet;
	}

	public Set<List<Integer>> getSetListInt() {
		return setListInt;
	}

	public void setSetListInt(Set<List<Integer>> setListInt) {
		this.setListInt = setListInt;
	}

	@Override
	public String toString() {
		return "CollectionsTestBean [intList=" + intList + ", strIntMap="
				+ strIntMap + ", intSet=" + intSet + ", stringSet=" + stringSet
				+ ", numSet=" + numSet + ", setListInt=" + setListInt
				+ ", primitiveInt=" + primitiveInt + "]";
	}
	
}
