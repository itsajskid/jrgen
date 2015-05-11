package com.github.jrgen.test.domain;


/**
 * Created by itsajskid on 2/27/15.
 */
public class NestedTypesTestBean {

    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

	@Override
	public String toString() {
		return "NestedTypesTestBean [person=" + person + "]";
	}
    
}
