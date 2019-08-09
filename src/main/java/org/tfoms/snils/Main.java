package org.tfoms.snils;

import org.tfoms.snils.dao.FindSnilsDAO;
import org.tfoms.snils.model.FindSnils;
import org.tfoms.snils.xmls.XmlParser;


public class Main {

    public static void main(String [] args){
        FindSnils result = FindSnilsDAO.getByIdFindSnils(4611876);
        System.out.println(result);

        XmlParser parser = new XmlParser();

        parser.createDocument(result);

    }
}
