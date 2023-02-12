package il.cshaifasweng.DataManipulationThroughDB;
import il.cshaifasweng.MySQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

public class DataBaseManipulation<T> implements DAO<T>{
    static Session session;

    // TODO: 12/02/2023 Remove, this is for testing purposes only , as adding manually would take a lot of time . 
    static {
        try {
            session = MySQL.getSession();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public DataBaseManipulation() {
        try {
            session = MySQL.getSession();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void intiate(Session session){
        DataBaseManipulation.session=session;
    }
    public static Session getSession(){
        return session;
    }
    @Override
    public T get(long id,Class<T> type) {

       T object=session.get(type,id);

        return object;
    }
    @Override
    public T getLastAdded(Class<T> Type) {

        String hql ="FROM "+Type.getName()+" e ORDER BY e.id DESC";
        TypedQuery<T> query = session.createQuery(hql, Type).setMaxResults(1);
        return query.getSingleResult();
    }
    @Override
    public List<T> listQuery(Class<T> Type, String hql) {

        TypedQuery<T> query = session.createQuery(hql, Type);
        List<T> entities=query.getResultList();

        return entities;
    }

    @Override
    public T query(Class<T> Type, String hql) {

        TypedQuery<T> query = session.createQuery(hql, Type).setMaxResults(1);
        T entities=(T)query.getSingleResult();

        return entities;
    }

    @Override
    public List<T> getAll(Class<T> type) {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        query.from(type);
        List<T> entities=session.createQuery(query).getResultList();

        return entities;
    }

    @Override
    public void save(T t,Class<T> type) {

        session.save(t);
        session.flush();


    }

    @Override
    public void update(T t) {

        session.update(t);
        session.flush();


    }
    @Override
    public void delete(T t,Class<T> type) {

        session.delete(t);
        session.flush();


    }
    public void deleteById(long id,Class<T> type){
        T object= get(id,type);
        delete(object,type);
    }

    public <T> List<T> executeQuery(Class<T> Type, String hql, Map<String,Object> params){
        return executeQuery(Type,hql,params,session);
    }
    public <T> List<T> executeQuery(Class<T> Type, String hql, Map<String,Object> params,Session session){
            Query query = session.createQuery(hql, Type).setMaxResults(1); // Can be changed to more than one.
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            return query.list();
        }
 public <T> List<T> executeListQuery(Class<T> Type, String hql, Map<String,Object> params,Session session){
            Query query = session.createQuery(hql, Type); // Can be changed to more than one.
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            return query.list();
        }

}
