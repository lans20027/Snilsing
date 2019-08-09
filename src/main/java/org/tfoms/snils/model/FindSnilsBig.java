package org.tfoms.snils.model;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;

public class FindSnilsBig extends FindSnils {

    @Column(name = "person_sex")
    @OneToOne
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "FindSnilsBig{" +
                "person=" + person +
                '}';
    }
}
