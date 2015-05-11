package com.github.jrgen.test.profile;

import java.util.LinkedHashSet;
import java.util.Set;

public class PersonalGroup implements Group {
	
	private String metLocation;
	private String groupName;
	private Set<UserProfile> members;
	
	public PersonalGroup() {
		this.members = new LinkedHashSet<UserProfile>();
	}
	
	public PersonalGroup(String metLocation, String groupName,
			Set<UserProfile> members) {
		super();
		this.metLocation = metLocation;
		this.groupName = groupName;
		this.members = members;
	}

	@Override
	public int groupSize() {
		return members.size();
	}

	@Override
	public String getGroupName() {
		return groupName;
	}

	@Override
	public Set<UserProfile> getMembers() {
		return members;
	}

	public String getMetLocation() {
		return metLocation;
	}

	public void setMetLocation(String metLocation) {
		this.metLocation = metLocation;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setMembers(Set<UserProfile> members) {
		this.members = members;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		result = prime * result
				+ ((metLocation == null) ? 0 : metLocation.hashCode());
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
		PersonalGroup other = (PersonalGroup) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		if (metLocation == null) {
			if (other.metLocation != null)
				return false;
		} else if (!metLocation.equals(other.metLocation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Personal [metLocation=" + metLocation + ", groupName="
				+ groupName + ", members=" + members + "]";
	}
	
	

}
