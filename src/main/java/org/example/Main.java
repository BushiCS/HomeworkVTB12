package org.example;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.concurrent.*;

public class Main {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(8);

    public static void main(String[] args) {
        // CREATE TABLE
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Item.class)
                .buildSessionFactory();

        // INSERT INTO
        for (int i = 1; i <= 40; i++) {
            Session session = factory.getCurrentSession();
            session.beginTransaction();
            session.persist(new Item(0));
            session.getTransaction().commit();
        }

        // UPDATE
        for (int i = 1; i <= 8; i++) {
            new Thread(() -> {
                Session session = null;
                try {
                    cyclicBarrier.await();
                    for (int j = 1; j <= 200; j++) {
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        int b = 40;
                        int a = 1;
                        int id = (int) (a + Math.random() * b);
//                        Item item = session.get(Item.class, id);
//                        session.lock(item, LockModeType.PESSIMISTIC_WRITE);


                        Item item = session
                                .createQuery("from Item where id = :id", Item.class)
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                .setParameter("id", id)
                                .getSingleResult();
                        item.setVal(item.getVal() + 1);
                        Thread.sleep(5);
                        try {
                            session.getTransaction().commit();
                            System.out.println(Thread.currentThread().getName() + " committed");
                        } catch (OptimisticLockException e) {
                            session.getTransaction().rollback();
                            System.out.println(Thread.currentThread().getName() + " rollback");
                        }
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println(e.getMessage());
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }
            }).start();

        }

    }
}