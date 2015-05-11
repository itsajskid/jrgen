package com.github.jrgen.test.profile;

import java.util.Set;

public interface Group {

	public int groupSize();
	public String getGroupName();
	public Set<UserProfile> getMembers();
	
}
