package org.tfoms.snils.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRIZYV_VIEW")
@NamedQueries({
        @NamedQuery(name = "prizyvByEnp",query = "from Prizyvnik p where p.enpVnutr in :enps")
})
public class Prizyvnik {
    @Id
    @Column(name = "PERSON_ADDRESSID")
    private String personAdd;

    @Column(name = "PERSON_SEX")
    private String sex;
    private Integer number_;

    @Column(name = "PRIZYV_INFILE")
    private String prizyvInfile;

    private String fam;
    private String im;
    private String ot;


    @Temporal(TemporalType.DATE)
    private Date dr;

    private String ser;
    private String num;

    @Column(name = "DATE_BEGIN")
    @Temporal(TemporalType.DATE)
    private Date dateBegin;

    private Integer period;

    @Column(name = "DATE_END")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;


    private Integer smo;

    @Column(name = "DATE_OTKREPL")
    @Temporal(TemporalType.DATE)
    private Date dateOtkrepl;

    @Column(name = "DATE_VOSSTANOVL")
    @Temporal(TemporalType.DATE)
    private Date dateVosstanovl;


    private String enp;
    private String serPolicy;
    private String numPolicy;


    @Column(name = "ENP_VNUTR")
    private String enpVnutr;

    @Override
    public String toString() {
        return "Prizyvnik{" +
                "personAdd='" + personAdd + '\'' +
                ", sex='" + sex + '\'' +
                ", number_=" + number_ +
                ", prizyvInfile='" + prizyvInfile + '\'' +
                ", fam='" + fam + '\'' +
                ", im='" + im + '\'' +
                ", ot='" + ot + '\'' +
                ", dr=" + dr +
                ", ser='" + ser + '\'' +
                ", num='" + num + '\'' +
                ", dateBegin=" + dateBegin +
                ", period=" + period +
                ", dateEnd=" + dateEnd +
                ", smo=" + smo +
                ", dateOtkrepl=" + dateOtkrepl +
                ", dateVosstanovl=" + dateVosstanovl +
                ", enp='" + enp + '\'' +
                ", serPolicy='" + serPolicy + '\'' +
                ", numPolicy='" + numPolicy + '\'' +
                ", enpVnutr='" + enpVnutr + '\'' +
                '}';
    }

    public Integer getNumber_() {
        return number_;
    }

    public void setNumber_(Integer number_) {
        this.number_ = number_;
    }

    public String getPersonAdd() {
        return personAdd;
    }

    public void setPersonAdd(String personAdd) {
        this.personAdd = personAdd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getNumber() {
        return number_;
    }

    public void setNumber(Integer number) {
        this.number_ = number;
    }

    public String getPrizyvInfile() {
        return prizyvInfile;
    }

    public void setPrizyvInfile(String prizyvInfile) {
        this.prizyvInfile = prizyvInfile;
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

    public String getSer() {
        return ser;
    }

    public void setSer(String ser) {
        this.ser = ser;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getSmo() {
        return smo;
    }

    public void setSmo(Integer smo) {
        this.smo = smo;
    }

    public Date getDateOtkrepl() {
        return dateOtkrepl;
    }

    public void setDateOtkrepl(Date dateOtkrepl) {
        this.dateOtkrepl = dateOtkrepl;
    }

    public Date getDateVosstanovl() {
        return dateVosstanovl;
    }

    public void setDateVosstanovl(Date dateVosstanovl) {
        this.dateVosstanovl = dateVosstanovl;
    }

    public String getEnp() {
        return enp;
    }

    public void setEnp(String enp) {
        this.enp = enp;
    }

    public String getSerPolicy() {
        return serPolicy;
    }

    public void setSerPolicy(String serPolicy) {
        this.serPolicy = serPolicy;
    }

    public String getNumPolicy() {
        return numPolicy;
    }

    public void setNumPolicy(String numPolicy) {
        this.numPolicy = numPolicy;
    }

    public String getEnpVnutr() {
        return enpVnutr;
    }

    public void setEnpVnutr(String enpVnutr) {
        this.enpVnutr = enpVnutr;
    }
}
