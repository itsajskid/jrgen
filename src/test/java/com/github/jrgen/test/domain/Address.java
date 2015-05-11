package com.github.jrgen.test.domain;


/**
 * Created by 529236 on 3/3/2015.
 */
public class Address {

    private String street;
    private String otherStreet;
    private String city;
    private String state;
    private String zipCode;

    public Address() {
        super();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOtherStreet() {
        return otherStreet;
    }

    public void setOtherStreet(String otherStreet) {
        this.otherStreet = otherStreet;
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
//        if (state != null && !state.matches("^[a-zA-Z]{2}$")) {
//            throw new IllegalArgumentException("State must be two alphabetical characters in length.");
//        }

        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
//        if (zipCode != null && !zipCode.matches("^\\d{5}$|^\\d{5}-\\d{4}$")) {
//            throw new IllegalArgumentException("Zip code must be in 5 or 9 digit format.");
//        }

        this.zipCode = zipCode;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((otherStreet == null) ? 0 : otherStreet.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		Address other = (Address) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (otherStreet == null) {
			if (other.otherStreet != null)
				return false;
		} else if (!otherStreet.equals(other.otherStreet))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", otherStreet=" + otherStreet
				+ ", city=" + city + ", state=" + state + ", zipCode="
				+ zipCode + "]";
	}

	public static class AddressBuilder {
		
		private String street;
		private String otherStreet;
		private String state;
		private String city;
		private String zipCode;
		
		public AddressBuilder street(String street) {
			this.street = street;
			return this;
		}
		
		public AddressBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		public AddressBuilder otherStreet (String otherStreet) {
			this.otherStreet = otherStreet;
			return this;
		}
		
		public AddressBuilder state (String state) {
			this.state = state;
			return this;
		}
		
		public AddressBuilder zipCode (String zipCode) {
			this.zipCode = zipCode;
			return this;
		}
		
		public Address build() {
			Address address = new Address();
			
			address.setCity(city);
			address.setOtherStreet(otherStreet);
			address.setState(state);
			address.setZipCode(zipCode);
			address.setStreet(street);
			
			return address;
		}
	}
}
