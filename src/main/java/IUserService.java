import java.util.Collection;

public interface IUserService {
    public User getUser(String username);
    public int addUser(User user) throws ApiException;
    public User updateUser(String username, User user) throws ApiException;
    public User deleteUser(User user) throws ApiException;

    public void setUsersTest();
}


