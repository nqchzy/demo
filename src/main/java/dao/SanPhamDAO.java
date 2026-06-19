package dao;

import hibernate.HibernateUtil;
import model.SanPham;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SanPhamDAO {

    public List<SanPham> getAll() {

        try(Session session =
                    HibernateUtil.getSessionFactory()
                            .openSession()) {

            return session
                    .createQuery("from SanPham",
                            SanPham.class)
                    .list();
        }
    }

    public boolean themSanPham(SanPham sp) {

        Transaction tx = null;

        try(Session session =
                    HibernateUtil.getSessionFactory()
                            .openSession()) {

            tx = session.beginTransaction();

            session.persist(sp);

            tx.commit();

            return true;

        } catch (Exception e) {

            if(tx != null)
                tx.rollback();

            return false;
        }
    }

    public boolean capNhatSanPham(SanPham sp) {

        Transaction tx = null;

        try(Session session =
                    HibernateUtil.getSessionFactory()
                            .openSession()) {

            tx = session.beginTransaction();

            session.merge(sp);

            tx.commit();

            return true;

        } catch (Exception e) {

            if(tx != null)
                tx.rollback();

            return false;
        }
    }

    public boolean xoaSanPham(String maSP) {

        Transaction tx = null;

        try(Session session =
                    HibernateUtil.getSessionFactory()
                            .openSession()) {

            tx = session.beginTransaction();

            SanPham sp =
                    session.get(
                            SanPham.class,
                            maSP
                    );

            if(sp != null)
                session.remove(sp);

            tx.commit();

            return true;

        } catch (Exception e) {

            if(tx != null)
                tx.rollback();

            return false;
        }
    }

    public SanPham timSanPhamTheoMa(String maSP) {

        try(Session session =
                    HibernateUtil.getSessionFactory()
                            .openSession()) {

            return session.get(
                    SanPham.class,
                    maSP
            );
        }
    }
}