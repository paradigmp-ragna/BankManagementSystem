import java.util.HashMap;

public class BankSystem {
    private HashMap<String, User> users;

    public BankSystem() {
        users = new HashMap<>();
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password));
        return true;
    }

    public User login(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.checkPassword(password)) return user;
        }
        return null;
    }

    public boolean transfer(User sender, String recipientUsername, double amount) {
        User recipient = users.get(recipientUsername);
        if (recipient == null || sender.getUsername().equals(recipientUsername)) {
            return false;
        }
        return sender.transferTo(recipient, amount);  // will return true only if successful
    }
}