package Module2.exceptions;

public class UserDeletionException extends RuntimeException {
    public UserDeletionException(long id) {
        super("Can not delete User with id " + id);
    }
}
