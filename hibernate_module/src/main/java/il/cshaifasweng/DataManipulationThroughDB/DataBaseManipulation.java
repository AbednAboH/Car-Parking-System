package il.cshaifasweng.DataManipulationThroughDB;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

public class DataBaseManipulation<T> implements DAO<T>{
    static Session session;
    public static void intiate(){
        session=factory.openSession();
    }
    public static Session getSession(){
        return session;
    }
    @Override
    public T get(int id,Class<T> type) {
       session.beginTransaction();
       T object=session.get(type,id);
        session.getTransaction().commit();
        return object;
    }
    @Override
    public T getLastAdded(Class<T> Type) {
        session.beginTransaction();
        String hql ="FROM "+Type.getName()+" e ORDER BY e.id DESC";
        TypedQuery<T> query = session.createQuery(hql, Type).setMaxResults(1);
        return query.getSingleResult();
    }
    @Override
    public List<T> listQuery(Class<T> Type, String hql) {
        session.beginTransaction();
        TypedQuery<T> query = session.createQuery(hql, Type);
        List<T> entities=query.getResultList();
        session.getTransaction().commit();
        return entities;
    }

    @Override
    public T query(Class<T> Type, String hql) {
        session.beginTransaction();
        TypedQuery<T> query = session.createQuery(hql, Type).setMaxResults(1);
        T entities=(T)query.getSingleResult();
        session.getTransaction().commit();
        return entities;
    }

    @Override
    public List<T> getAll(Class<T> type) {
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        query.from(type);
        List<T> entities=session.createQuery(query).getResultList();
        session.getTransaction().commit();
        return entities;
    }

    @Override
    public void save(T t,Class<T> type) {
        session.beginTransaction();
        session.save(t);
        session.flush();
        session.getTransaction().commit();

    }

    @Override
    public void update(T t) {
        session.beginTransaction();
        session.update(t);
        session.flush();
        session.getTransaction().commit();

    }
    @Override
    public void delete(T t,Class<T> type) {
        session.beginTransaction();
        session.delete(t);
        session.flush();
        session.getTransaction().commit();

    }

    public void deleteById(int id,Class<T> type){
        T object= get(id,type);
        delete(object,type);
    }

    public <T> List<T> executeQuery(Class<T> Type, String hql, Map<String,Object> params){
        session.beginTransaction();
        Query query = session.createQuery(hql, Type).setMaxResults(1); // Can be changed to more than one.
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        session.getTransaction().commit();
        return query.list();
    }

}
