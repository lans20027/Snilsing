package org.tfoms.snils.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Personadd {

    @Id
    private String personadd_addressid;

    private String born;

    @Temporal(TemporalType.DATE)
    private Date datepassport;

    private String dok_vi;

    @Override
    public String toString() {
        return "Personadd{" +
                "personadd_addressid=" + personadd_addressid +
                ", born='" + born + '\'' +
                ", datepassport=" + datepassport +
                ", dok_vi='" + dok_vi + '\'' +
                '}';
    }

    public String getPersonadd_addressid() {
        return personadd_addressid;
    }

    public void setPersonadd_addressid(String personadd_addressid) {
        this.personadd_addressid = personadd_addressid;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public Date getDatepassport() {
        return datepassport;
    }

    public void setDatepassport(Date datepassport) {
        this.datepassport = datepassport;
    }

    public String getDok_vi() {
        return dok_vi;
    }

    public void setDok_vi(String dok_vi) {
        this.dok_vi = dok_vi;
    }
}
