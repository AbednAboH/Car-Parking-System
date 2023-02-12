package il.cshaifasweng.DataManipulationThroughDB;

import il.cshaifasweng.*;
import org.hibernate.SessionFactory;
import java.util.List;

public interface DAO<T> {
    SessionFactory factory =MySQL.getSessionFactory();
    T get(long id,Class<T> type);
    T getLastAdded(Class<T> Type);
    List<T> getAll(Class<T> type);
    void save(T t,Class<T> type);
    void update(T t);
    void delete(T t,Class<T> type);
    List<T> listQuery(Class<T> Type, String hql);
    T query(Class<T> Type, String hql);

//    T Query(String query,Class<T>);

}
