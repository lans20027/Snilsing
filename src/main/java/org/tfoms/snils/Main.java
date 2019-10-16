package org.tfoms.snils;

import org.apache.xmlbeans.impl.regex.Match;
import org.tfoms.snils.dao.FindSnilsDAO;
import org.tfoms.snils.dao.SnilsDAO;
import org.tfoms.snils.model.*;
import org.tfoms.snils.xmls.XmlParser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.*;


class Thread1 extends Thread{
    private boolean toStop = false;
    Thread current;
    Thread superThread;

    Thread1(Thread s){
        super("Thread111");
        superThread = s;
    }

    @Override
    public void run(){
        current = Thread.currentThread();
        Runnable r = () -> {
          System.out.println(Thread.currentThread().getName() + " " + new Date() + "\n" + current.getName() + " is alive?" + current.isAlive() + "\n" + superThread.getName() + " is alive?" + superThread.isAlive() + "\n" );
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(r,0,1000,TimeUnit.MILLISECONDS);
        try {
            Thread.sleep(10_000);
            System.out.println("awake " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            service.shutdownNow();
            System.out.println("ok goodbye");
        }
    }
}


class DemoT extends Thread{
    String s;

    DemoT(String t){
        s = t;
    }


    void show(){
        synchronized (s) {
            for (int i = 0; i < 4; i++) {
                System.out.println(i);
                //System.out.print(s.charAt(i) + " ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
        }
    }

    @Override
    public void run() {
        show();
    }
}


public class Main {
    public static void main(String [] args) throws Exception{
        String text = null;
        String text2 = null;
        DemoT t1 = new DemoT(text);
        DemoT t2 = new DemoT(text2);

        t1.start();
        t2.start();




//        Thread.sleep(5000);
//        System.out.println("killing Hello from " + Thread.currentThread().getName());
//        t.stopMe();
//        t.interrupt();
//        System.out.println("killed Hello from " + Thread.currentThread().getName());

        /*
        ArrayList<String> enps = new ArrayList<>();
        enps.add("5451320881000868");

        List<TablePerson> result = FindSnilsDAO.findPersonByEnp1(enps);

        XmlParser parser = new XmlParser();

        parser.createDocument(result.get(0));
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("developerUnit");
        EntityManager em =managerFactory.createEntityManager();

        List<SnilsSaveResponse> list = em.createNamedQuery("findSnilsGood",SnilsSaveResponse.class).getResultList();

        System.out.println(list);*/

//        System.exit(0);
    }
}
