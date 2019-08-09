package org.tfoms.snils.dao;

import org.hibernate.Session;
import org.tfoms.snils.hibernateDB.HibernateUtil;
import org.tfoms.snils.model.FindSnils;
import org.tfoms.snils.model.FindSnilsBig;

import java.util.ArrayList;
import java.util.List;



public class FindSnilsDAO {
    public static List<FindSnils> getAllFindSnils(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from FindSnils t where t.state = 0",FindSnils.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static List<FindSnils> getAllFindSnils(int maximum){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from FindSnils t where t.state = 0 and rownum <=" + maximum,FindSnils.class).list();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static FindSnils getByIdFindSnils(int id){
        FindSnils result = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            result = session.createQuery("from FindSnils t where  t.id=" + id,FindSnils.class).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
