package Module2.dao;

import java.util.List;

public interface DaoInterface<T> {
    List<T> getAll();

    T get(long id);

    T create(T entity);

    T update(T entity);

    T delete(long id);
}
