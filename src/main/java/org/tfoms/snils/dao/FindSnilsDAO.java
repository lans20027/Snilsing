package org.tfoms.snils.dao;

import org.hibernate.Session;
import org.tfoms.snils.hibernateDB.HibernateUtil;
import org.tfoms.snils.model.Person;
import org.tfoms.snils.model.TablePerson;

import java.util.ArrayList;
import java.util.List;



public class FindSnilsDAO {
    public static List<TablePerson> findPersonByEnp1(List<String> enps){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            List<Person> personList = session.createQuery("from Person p where p.enp in :enps",Person.class).setParameter("enps",enps).list();

            ArrayList<TablePerson> tablePeople = new ArrayList<>(personList.size());
            for(Person p : personList){
                TablePerson newPerson = new TablePerson(p);
                tablePeople.add(newPerson);
            }
            return tablePeople;
        }
    }








}
