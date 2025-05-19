import javax.swing.*;
import java.awt.*;

public class MainApp {
    static BankSystem bank = new BankSystem();
    static JDBCManager jdbc = new JDBCManager();

    public static void main(String[] args) {
        jdbc.connect();
        SwingUtilities.invokeLater(MainApp::showLoginScreen);
    }

    static void showLoginScreen() {
        JFrame frame = new JFrame("Bank Management System - Login/Register");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 1, 10, 5));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        frame.add(new JLabel("Username:"));
        frame.add(userField);
        frame.add(new JLabel("Password:"));
        frame.add(passField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        frame.add(buttonPanel);

        loginBtn.addActionListener(e -> {
            User user = bank.login(userField.getText(), new String(passField.getPassword()));
            if (user != null) {
                frame.dispose();
                showDashboard(user);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!");
            }
        });

        registerBtn.addActionListener(e -> {
            boolean success = bank.register(userField.getText(), new String(passField.getPassword()));
            JOptionPane.showMessageDialog(frame, success ? "Registered successfully!" : "Username already exists!");
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void showDashboard(User user) {
        JFrame frame = new JFrame("Welcome, " + user.getUsername());
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JLabel balanceLabel = new JLabel("Current Balance: ₹" + user.getBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(balanceLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField amountField = new JTextField();
        String[] actions = { "Deposit", "Withdraw", "Transfer" };
        JComboBox<String> actionBox = new JComboBox<>(actions);
        JTextField recipientField = new JTextField();
        JButton submitBtn = new JButton("Submit Transaction");
        JButton historyBtn = new JButton("Transaction History");

        JButton logoutBtn = new JButton("Logout");

        panel.add(new JLabel("Amount (₹):"));
        panel.add(amountField);
        panel.add(new JLabel("Action:"));
        panel.add(actionBox);
        panel.add(new JLabel("Recipient (for Transfer):"));
        panel.add(recipientField);

        JPanel southPanel = new JPanel();
        southPanel.add(submitBtn);
        southPanel.add(historyBtn);
        southPanel.add(logoutBtn);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        submitBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String action = (String) actionBox.getSelectedItem();

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Amount must be positive.");
                    return;
                }

                switch (action) {
                    case "Deposit":
                        user.deposit(amount);
                        jdbc.logAction(user.getUsername() + " deposited ₹" + amount);
                        break;
                    case "Withdraw":
                        if (!user.withdraw(amount)) {
                            JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                        } else {
                            jdbc.logAction(user.getUsername() + " withdrew ₹" + amount);
                        }
                        break;
                    case "Transfer":
                        String recipient = recipientField.getText();
                        if (!bank.transfer(user, recipient, amount)) {
                            JOptionPane.showMessageDialog(frame, "Transfer failed (user may not exist or low balance).");
                        } else {
                            jdbc.logAction(user.getUsername() + " transferred ₹" + amount + " to " + recipient);
                        }
                        break;
                }


                balanceLabel.setText("Current Balance: ₹" + user.getBalance());
                amountField.setText("");
                recipientField.setText("");

            }

            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
            }
        });


        historyBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (String s : user.getTransactions()) {
                sb.append(s).append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString(), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
        });


        logoutBtn.addActionListener(e -> {
            frame.dispose();    // Close dashboard window
            showLoginScreen();  // Show login/register screen again
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}