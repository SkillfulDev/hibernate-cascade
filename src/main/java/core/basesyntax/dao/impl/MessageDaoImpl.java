package core.basesyntax.dao.impl;

import core.basesyntax.dao.MessageDao;
import core.basesyntax.model.Message;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class MessageDaoImpl extends AbstractDao implements MessageDao {
    public MessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Message create(Message entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                throw new RuntimeException("Can`t save message " + entity + " to DB");
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }

    @Override
    public Message get(Long id) {
        Message message = null;
        try (Session session = factory.openSession()) {
            message = session.get(Message.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can`t find message with id = " + id + " in Db");
        }
        return message;
    }

    @Override
    public List<Message> getAll() {
        List<Message> list = new ArrayList<>();
        try (Session session = factory.openSession()) {
            Query<Message> query = session.createQuery("FROM Message", Message.class);
            list = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can`t find any messages in Db");
        }
        return list;
    }

    @Override
    public void remove(Message entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can`t remove message " + entity + " from DB");
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
