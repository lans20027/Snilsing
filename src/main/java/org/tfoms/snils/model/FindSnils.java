package org.tfoms.snils.model;


import org.hibernate.annotations.NotFound;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Z_PERSON_FIND_SNILS")
public class FindSnils {
    @Id
    private Long id;

    private String enp;

    private String fam;

    private String im;

    private String ot;

    private Date dr;

    private String serdoc;

    private String numdoc;

    private String gd;

    private Integer state;

    @OneToOne
    @JoinColumn(name = "id",referencedColumnName = "person_addressid")
    private Person person;

    @OneToOne
    @JoinColumn(name="id",referencedColumnName = "personadd_addressid")
    private Personadd personadd;

    @Override
    public String toString() {
        return "FindSnils{" +
                "id=" + id +
                ", enp='" + enp + '\'' +
                ", fam='" + fam + '\'' +
                ", im='" + im + '\'' +
                ", ot='" + ot + '\'' +
                ", dr=" + dr +
                ", serdoc='" + serdoc + '\'' +
                ", numdoc='" + numdoc + '\'' +
                ", gd='" + gd + '\'' +
                ", state=" + state +
                ", person=" + person +
                ", personadd=" + personadd +
                '}';
    }

    public Personadd getPersonadd() {
        return personadd;
    }

    public void setPersonadd(Personadd personadd) {
        this.personadd = personadd;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnp() {
        return enp;
    }

    public void setEnp(String enp) {
        this.enp = enp;
    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    public Date getDr() {
        return dr;
    }

    public void setDr(Date dr) {
        this.dr = dr;
    }

    public String getSerdoc() {
        return serdoc;
    }

    public void setSerdoc(String serdoc) {
        this.serdoc = serdoc;
    }

    public String getNumdoc() {
        return numdoc;
    }

    public void setNumdoc(String numdoc) {
        this.numdoc = numdoc;
    }

    public String getGd() {
        return gd;
    }

    public void setGd(String gd) {
        this.gd = gd;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
