package org.tfoms.snils;

import org.tfoms.snils.dao.FindSnilsDAO;
import org.tfoms.snils.dao.SnilsDAO;
import org.tfoms.snils.model.*;
import org.tfoms.snils.xmls.XmlParser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.*;

public class Main {
    public static void main(String [] args) throws Exception{


/*        ArrayList<String> enps = new ArrayList<>();
        enps.add("5478960884000016");

        List<Person> result = FindSnilsDAO.findPersonByEnp(enps);

        TablePerson person = new TablePerson(result.get(0));
        System.out.println(result.get(0));

        XmlParser parser = new XmlParser();

        parser.createDocument(person);
        */
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("developerUnit");
        EntityManager em =managerFactory.createEntityManager();

        List<SnilsSaveResponse> list = em.createNamedQuery("findSnilsGood",SnilsSaveResponse.class).getResultList();

        System.out.println(list);
        System.exit(0);
    }
}
