# BankManagementSystem
# Bank Management System (Java Swing)

This is a simple Bank Management System built using **Java Swing** without a database. It supports:

- User Registration & Login  
- Deposit, Withdraw, and Transfer Money  
- Transaction History  
- Logout  
- Basic transaction logging (via JDBC)

## How to Run

1. Open the project in any Java IDE (Eclipse, IntelliJ, NetBeans).
2. Run `MainApp.java`.
3. Register a new user or log in.
4. Perform banking operations using the GUI.

## Notes

- Data is stored **in-memory**, so it will reset when the app is closed.
- All balances and transactions exist only during runtime.

## Files

- `MainApp.java` – App entry point with GUI.
- `BankSystem.java` – Manages users and transactions.
- `User.java` – Holds user info and balance.
- `JDBCManager.java` – Logs actions (optional).

## Developed For

Java Mini Project – 2025  
[Your Name], [College Name]

