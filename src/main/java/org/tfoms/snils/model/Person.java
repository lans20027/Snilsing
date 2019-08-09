package org.tfoms.snils.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    private Long person_addressid;

    @Column(name = "person_sex")
    private String sex;


    public Long getPerson_addressid() {
        return person_addressid;
    }

    public void setPerson_addressid(Long person_addressid) {
        this.person_addressid = person_addressid;
    }

    public String getSex() {
        return sex.equals("0") ? "Male" : "Female";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + person_addressid +
                ", sex='" + sex + '\'' +
                '}';
    }
}
