package com.github.jrgen.test.domain;


public final class ImmutableName {

	private final String firstName;
	private final String lastName;
	private final String middleName;
	private final Salutation salutation;
	
	public enum Salutation {
		Mr, Mrs, Ms, Dr;
	}

	public ImmutableName(String firstName, String lastName, String middleName,
		Salutation salutation) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result
				+ ((salutation == null) ? 0 : salutation.hashCode());
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
		ImmutableName other = (ImmutableName) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (salutation != other.salutation)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImmutableName [firstName=" + firstName + ", lastName="
				+ lastName + ", middleName=" + middleName + ", salutation="
				+ salutation + "]";
	}
}
