package com.github.jrgen.test.profile;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProfessionalGroup implements Group {
	
	private String companyName;
	private String groupName;
	private Set<UserProfile> members;
	
	public ProfessionalGroup() {
		this.members = new LinkedHashSet<UserProfile>();
	}

	public ProfessionalGroup(String companyName, String groupName,
			Set<UserProfile> members) {
		super();
		this.companyName = companyName;
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

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setMembers(Set<UserProfile> members) {
		this.members = members;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((members == null) ? 0 : members.hashCode());
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
		ProfessionalGroup other = (ProfessionalGroup) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
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
		return true;
	}

	@Override
	public String toString() {
		return "ProfessionalGroup [companyName=" + companyName + ", groupName="
				+ groupName + ", members=" + members + "]";
	}
	
	
}
