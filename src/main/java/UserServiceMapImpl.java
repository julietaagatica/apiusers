import java.util.HashMap;
import java.util.Map;

public class UserServiceMapImpl implements IUserService {

    private Map<String,User> userMap;

    public UserServiceMapImpl() {
        this.userMap = new HashMap<String, User>();
    }

    public User getUser(String username) {
        return userMap.get(username);
    }

    public int addUser(User user) throws ApiException {
        User userNew = userMap.put(user.getUsername(),user);
        if (userNew != null) {
            return -1;
        }
        return userMap.size();
    }

    public User updateUser(String username,User user) throws ApiException {
        userMap.remove(username);
        return userMap.put(user.getUsername(), user);
    }

    public User deleteUser(User user) throws ApiException {
        userMap.remove(user.getUsername());
        return null;
    }

    public void setUsersTest(){
        User user1 = new User(1,"user1","pass1",null);
        User user2 = new User(2,"user2","pass2",null);
        try {
            this.addUser(user1);
            this.addUser(user2);
        } catch (ApiException e) {
            System.out.println("Error al insertar usuarios. ERROR: "+e.getMessage());
        }
    }

}
