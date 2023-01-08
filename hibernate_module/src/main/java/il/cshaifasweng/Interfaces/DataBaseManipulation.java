package il.cshaifasweng.Interfaces;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class DataBaseManipulation<T> implements DAO<T>{

    @Override
    public T get(long id,Class<T> type) {
       Session session=factory.openSession();
       session.beginTransaction();
       T object=session.get(type,id);
       session.close();
       return object;
    }

    @Override
    public List<T> getAll(Class<T> type) {
        Session session=factory.openSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        query.from(type);
        List<T> entities=session.createQuery(query).getResultList();
        session.close();
        return entities;
    }

    @Override
    public void save(T t,Class<T> type) {
        Session session=factory.openSession();
        session.beginTransaction();
        session.save(t);
        session.flush();
        session.close();

    }

    @Override
    public void update(T t) {
        Session session=factory.openSession();
        session.beginTransaction();
        session.update(t);
        session.flush();
        session.close();
    }

    @Override
    public void delete(T t,Class<T> type) {
        Session session=factory.openSession();
        session.beginTransaction();
        session.delete(t);
        session.flush();
        session.close();

    }

}
