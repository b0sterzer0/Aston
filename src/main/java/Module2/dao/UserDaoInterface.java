package Module2.dao;

import Module2.models.User;

import java.util.List;

public interface UserDaoInterface {
    List<User> getAll();

    User get(long id);

    User create(User entity);

    User update(User entity);

    User delete(long id);
}
