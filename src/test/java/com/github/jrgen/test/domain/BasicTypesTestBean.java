package com.github.jrgen.test.domain;

public class BasicTypesTestBean {

	private Integer integerProperty;
	private String stringProperty;
	private Character characterProperty;
	private Boolean booleanProperty;
	private Float floatProperty;
	private Short shortProperty;
	private Long longProperty;
	private Double doublProperty;
	
	public BasicTypesTestBean() {
		super();
	}

	public Integer getIntegerProperty() {
		return integerProperty;
	}

	public void setIntegerProperty(Integer integerProperty) {
		this.integerProperty = integerProperty;
	}

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public Character getCharacterProperty() {
		return characterProperty;
	}

	public void setCharacterProperty(Character characterProperty) {
		this.characterProperty = characterProperty;
	}

	public Boolean getBooleanProperty() {
		return booleanProperty;
	}

	public void setBooleanProperty(Boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public Float getFloatProperty() {
		return floatProperty;
	}

	public void setFloatProperty(Float floatProperty) {
		this.floatProperty = floatProperty;
	}

	public Short getShortProperty() {
		return shortProperty;
	}

	public void setShortProperty(Short shortProperty) {
		this.shortProperty = shortProperty;
	}

	public Long getLongProperty() {
		return longProperty;
	}

	public void setLongProperty(Long longProperty) {
		this.longProperty = longProperty;
	}

	public Double getDoublProperty() {
		return doublProperty;
	}

	public void setDoublProperty(Double doublProperty) {
		this.doublProperty = doublProperty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((booleanProperty == null) ? 0 : booleanProperty.hashCode());
		result = prime
				* result
				+ ((characterProperty == null) ? 0 : characterProperty
						.hashCode());
		result = prime * result
				+ ((doublProperty == null) ? 0 : doublProperty.hashCode());
		result = prime * result
				+ ((floatProperty == null) ? 0 : floatProperty.hashCode());
		result = prime * result
				+ ((integerProperty == null) ? 0 : integerProperty.hashCode());
		result = prime * result
				+ ((longProperty == null) ? 0 : longProperty.hashCode());
		result = prime * result
				+ ((shortProperty == null) ? 0 : shortProperty.hashCode());
		result = prime * result
				+ ((stringProperty == null) ? 0 : stringProperty.hashCode());
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
		BasicTypesTestBean other = (BasicTypesTestBean) obj;
		if (booleanProperty == null) {
			if (other.booleanProperty != null)
				return false;
		} else if (!booleanProperty.equals(other.booleanProperty))
			return false;
		if (characterProperty == null) {
			if (other.characterProperty != null)
				return false;
		} else if (!characterProperty.equals(other.characterProperty))
			return false;
		if (doublProperty == null) {
			if (other.doublProperty != null)
				return false;
		} else if (!doublProperty.equals(other.doublProperty))
			return false;
		if (floatProperty == null) {
			if (other.floatProperty != null)
				return false;
		} else if (!floatProperty.equals(other.floatProperty))
			return false;
		if (integerProperty == null) {
			if (other.integerProperty != null)
				return false;
		} else if (!integerProperty.equals(other.integerProperty))
			return false;
		if (longProperty == null) {
			if (other.longProperty != null)
				return false;
		} else if (!longProperty.equals(other.longProperty))
			return false;
		if (shortProperty == null) {
			if (other.shortProperty != null)
				return false;
		} else if (!shortProperty.equals(other.shortProperty))
			return false;
		if (stringProperty == null) {
			if (other.stringProperty != null)
				return false;
		} else if (!stringProperty.equals(other.stringProperty))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BasicTypesTestBean [integerProperty=" + integerProperty
				+ ", stringProperty=" + stringProperty + ", characterProperty="
				+ characterProperty + ", booleanProperty=" + booleanProperty
				+ ", floatProperty=" + floatProperty + ", shortProperty="
				+ shortProperty + ", longProperty=" + longProperty
				+ ", doublProperty=" + doublProperty + "]";
	}
	
	
}
