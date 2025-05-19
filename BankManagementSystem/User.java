import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private double balance;
    private ArrayList<String> transactions;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public boolean checkPassword(String pwd) {
        return this.password.equals(pwd);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
        transactions.add("Deposited ₹" + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
            transactions.add("Withdrew ₹" + amount);
            return true;
        } else {
            transactions.add("Failed withdrawal attempt: ₹" + amount + " (Insufficient balance)");
            return false;
        }
    }

    public boolean transferTo(User recipient, double amount) {
        if (this.balance >= amount && !this.username.equals(recipient.getUsername())) {
            this.balance -= amount;
            recipient.balance += amount;

            this.transactions.add("Transferred ₹" + amount + " to " + recipient.getUsername());
            recipient.transactions.add("Received ₹" + amount + " from " + this.username);
            return true;
        } else {
            this.transactions.add("Failed transfer attempt: ₹" + amount + " to " + recipient.getUsername());
            return false;
        }
    }


    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public String getUsername() {
        return username;
    }
}