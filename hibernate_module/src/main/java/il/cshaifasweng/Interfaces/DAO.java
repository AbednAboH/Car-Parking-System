package il.cshaifasweng.Interfaces;

import il.cshaifasweng.*;
import org.hibernate.SessionFactory;
import java.util.List;

public interface DAO<T> {
    SessionFactory factory =MySQL.getSessionFactory();
    T get(long id,Class<T> type);
    List<T> getAll(Class<T> type);
    void save(T t,Class<T> type);
    void update(T t);
    void delete(T t,Class<T> type);
}
