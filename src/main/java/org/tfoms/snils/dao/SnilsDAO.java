package org.tfoms.snils.dao;

import org.tfoms.snils.model.Person;
import org.tfoms.snils.model.Prizyvnik;
import org.tfoms.snils.model.SnilsSaveResponse;
import org.tfoms.snils.model.TablePerson;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SnilsDAO {
    static EntityManager em = Persistence.createEntityManagerFactory("developerUnit").createEntityManager();

    public static List<TablePerson> findSnilsGood(){
        List<SnilsSaveResponse> snilses = em.createNamedQuery("findSnilsGood",SnilsSaveResponse.class).getResultList();
        ArrayList<TablePerson> data = new ArrayList<>();

        for(SnilsSaveResponse s : snilses){
            data.add(new TablePerson(s));
        }

        return data;
    }


    public static TablePerson findPerson(String surname,String firstname,String lastname,Date birthday,String trueSer,String trueNum){
        Person person = em.createNamedQuery("personByFIOD",Person.class)
                .setParameter("surname",surname)
                .setParameter("firstname",firstname)
                .setParameter("lastname",lastname)
                .setParameter("birthday",birthday).getSingleResult();

        TablePerson tablePerson = new TablePerson(person);
        tablePerson.setPersonSerdoc(trueSer);
        tablePerson.setPersonNumdoc(trueNum);
        tablePerson.setPersonadd(person.getPersonadd());
        return tablePerson;
    }


    public static void insertPerson(TablePerson person){
        if(person.getSnils().trim().equals("нет данных") || person.getSnils().trim().equals("-")) return;
        SnilsSaveResponse snilsPerson = new SnilsSaveResponse(person);
        snilsPerson.setDateInsert(new Date());
        System.out.println("ssaving:" + snilsPerson);


        em.getTransaction().begin();
        List<SnilsSaveResponse> list = em.createNamedQuery("findSnilsById",SnilsSaveResponse.class)
                .setParameter("birthday",person.getPersonBirthday())
                .setParameter("firstname",person.getPersonFirstname())
                .setParameter("surname",person.getPersonSurname())
                .setParameter("lastname",person.getPersonLastname()).getResultList();

        if(list.isEmpty()){
            em.persist(snilsPerson);
        }else{
            if(list.size() == 1){
                em.remove(list.get(0));
                em.persist(snilsPerson);
            }
        }
        em.getTransaction().commit();
    }
}
