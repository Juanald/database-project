import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
public class JdbcOracleConnectionTemplate {
private static Connection conn1 = null;

    public static void main(String[] args) {
            try {
                Class.forName("oracle.jdbc.OracleDriver"); 
                // Create a login with credentials
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(300, 200);
                loginFrame.setLayout(new FlowLayout());

                JTextField usernameField = new JTextField(20);
                JTextField passwordField = new JPasswordField(20);
                JButton loginButton = new JButton("Login");

                loginFrame.add(new JLabel("Enter username:"));
                loginFrame.add(new JLabel("Enter password:"));
                loginFrame.add(usernameField);
                loginFrame.add(passwordField);
                loginFrame.add(loginButton);
                loginFrame.setVisible(true);
                // We have to get the values
                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        final String dbURL1 = "jdbc:oracle:thin:" + username + "/" + password + "@oracle12c.scs.ryerson.ca:1521:orcl12c";
                        try {
                            conn1 = DriverManager.getConnection(dbURL1);
                            if (conn1 != null) {
                                System.out.println("Connected with connection #1");
                            }
                            GUI gui = new GUI(conn1);
                            loginFrame.dispose();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            System.out.println("Failed to connect to the database");
                            System.exit(0);
                        }
                    }
                });
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
    }
}

class GUI {
    private Connection conn1;

    public GUI(Connection conn1) {
        this.conn1 = conn1;
        // Create the main frame
        JFrame frame = new JFrame("Database Operations");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // On close, close the connection
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (conn1 != null && !conn1.isClosed()) {
                        conn1.close();
                        System.out.println("Closed connection");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();    
        buttonPanel.setLayout(new GridLayout(4, 2, 10, 10));
        JTextArea output = new JTextArea();
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons
        JButton dropTablesButton = new JButton("Drop Tables");
        JButton createTablesButton = new JButton("Create Tables");
        JButton populateTablesButton = new JButton("Populate Tables");
        JButton queryTablesButton = new JButton("Query Tables");
        JButton selectButton = new JButton("Read Tables");
        JButton deleteButton = new JButton("Delete Record");
        JButton updateButton = new JButton("Update Record");

        
        // Add action listeners to buttons
        dropTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Drop Tables button clicked.");
                output.setText("");
                String[] queries = {
                    "DROP TABLE customers CASCADE CONSTRAINTS",
                    "DROP TABLE loan CASCADE CONSTRAINTS",
                    "DROP TABLE monthly_statements CASCADE CONSTRAINTS",
                    "DROP TABLE transactions CASCADE CONSTRAINTS",
                    "DROP TABLE Bank CASCADE CONSTRAINTS",
                    "DROP TABLE Employee CASCADE CONSTRAINTS",
                    "DROP TABLE chequing_accounts CASCADE CONSTRAINTS",
                    "DROP TABLE savings_accounts CASCADE CONSTRAINTS",
                    "DROP TABLE credit_card_accounts CASCADE CONSTRAINTS",
                    "DROP VIEW high_ccas",
                    "DROP VIEW CustomerValidTransactions",
                    "DROP VIEW Above_A_Thousand"
                };
                try (Statement stmt = conn1.createStatement()) {
                    for (String query : queries) {
                        stmt.executeUpdate(query);
                        System.out.println("Executed: " + query); // Optionally log each query
                    }
                System.out.println("Tables dropped");
                output.setText("Tables dropped");
                } catch (SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    System.out.println(ex.getMessage());
                    output.setText(ex.getMessage());
                }
            }
        });

        createTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Tables button clicked.");
                output.setText("");
                String[] queries =  {
                    "CREATE TABLE customers (customer_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, customer_name varchar2(255) NOT NULL, email varchar2(255) NOT NULL, address varchar2(255) NOT NULL, phone_number varchar2(255) NOT NULL, social_insurance_number varchar2(255) NOT NULL UNIQUE, nationality varchar2(255), age NUMBER NOT NULL, gender varchar2(255), account_status varchar2(255) NOT NULL, PIN NUMBER NOT NULL)",
                    "CREATE TABLE Bank (Bank_Name VARCHAR(100) NOT NULL, Branch_Location VARCHAR(100) NOT NULL, Transit_No CHAR(5) PRIMARY KEY NOT NULL, Website VARCHAR(100) NOT NULL, Email VARCHAR(100) NOT NULL, Contact_Number VARCHAR(15) NOT NULL)",
                    "CREATE TABLE Employee (Employee_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, Employee_Name VARCHAR(100) NOT NULL, Email VARCHAR(100) NOT NULL UNIQUE, Address VARCHAR(255) NOT NULL, Phone_Number VARCHAR(15) NOT NULL, Age INT NOT NULL, Gender VARCHAR (20) NOT NULL, Nationality VARCHAR(50) NOT NULL)",
                    "CREATE TABLE chequing_accounts (customer_id INT, account_id INT GENERATED ALWAYS AS IDENTITY, balance DECIMAL(25, 2) DEFAULT 0, account_type VARCHAR2(255), PRIMARY KEY (customer_id, account_id), FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",
                    "CREATE TABLE savings_accounts (customer_id INT, account_id INT GENERATED ALWAYS AS IDENTITY UNIQUE, balance DECIMAL(25, 2) DEFAULT 0, account_type VARCHAR2(255), interest_rate DECIMAL(10, 2), PRIMARY KEY (customer_id, account_id), FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",
                    "CREATE TABLE credit_card_accounts (customer_id INT, card_number INT UNIQUE NOT NULL, balance DECIMAL(25, 2) DEFAULT 0, credit_limit INT DEFAULT 2500, minimum_payment DECIMAL(25, 2) DEFAULT 10, expiration_date DATE NOT NULL, payment_due_date DATE NOT NULL, PRIMARY KEY (customer_id, card_number), FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",
                    "CREATE TABLE loan (customer_id NUMBER, loan_id NUMBER GENERATED ALWAYS AS IDENTITY UNIQUE, principal DECIMAL(25, 2), interest DECIMAL(5, 2), payment_amount DECIMAL(25, 2), status VARCHAR(10) DEFAULT 'inactive' CHECK (status IN ('active', 'inactive', 'closed')), PRIMARY KEY (customer_id, loan_id), FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",
                    "CREATE TABLE transactions (customer_id NUMBER, transaction_id NUMBER GENERATED ALWAYS AS IDENTITY UNIQUE, transaction_date DATE, transaction_time TIMESTAMP, account_from NUMBER, account_to NUMBER, transaction_amount DECIMAL(25, 2), PRIMARY KEY (customer_id, transaction_id), FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",
                    "CREATE TABLE monthly_statements (customer_id NUMBER, balance_sheet_id NUMBER GENERATED ALWAYS AS IDENTITY UNIQUE, transaction_id NUMBER, account_id NUMBER, account_type VARCHAR2(255), statement_period VARCHAR2(20), start_date DATE, end_date DATE, opening_balance DECIMAL(25, 2), closing_balance DECIMAL(25, 2), PRIMARY KEY (balance_sheet_id, account_id, customer_id), FOREIGN KEY (transaction_id, customer_id) REFERENCES transactions(transaction_id, customer_id))"
                };

                try (Statement stmt = conn1.createStatement()) {
                    for (String query : queries) {
                        stmt.executeUpdate(query);
                        System.out.println("Executed: " + query); // Optionally log each query
                    }
                System.out.println("Tables created");
                output.setText("Tables created");
                } catch(SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    System.out.println(ex.getMessage());
                    output.setText(ex.getMessage());

                }
            }
        });

        populateTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Populate Tables button clicked.");
                output.setText("");
                String[] insertQueries = {
                    "INSERT INTO customers (customer_name, email, address, phone_number, social_insurance_number, nationality, age, gender, account_status, PIN) VALUES ('Prudence Egdal', 'pegdal0@shareasale.com', '19737 Kinsman Street', '527-170-8707', '138-93-0308', 'China', 65, 'Genderfluid', 'Good standing', 5)",
                    "INSERT INTO customers (customer_name, email, address, phone_number, social_insurance_number, nationality, age, gender, account_status, PIN) VALUES ('Didi Prover', 'dprover1@exblog.jp', '9 Clarendon Lane', '730-933-0605', '441-08-0840', 'China', 9, 'Female', 'Good standing', 5)",
                    "INSERT INTO customers (customer_name, email, address, phone_number, social_insurance_number, nationality, age, gender, account_status, PIN) VALUES ('Morry Feifer', 'mfeifer2@nasa.gov', '0 Bunting Crossing', '949-639-8327', '258-22-4751', 'Peru', 3, 'Male', 'Bad Standing', 4)",
                    "INSERT INTO customers (customer_name, email, address, phone_number, social_insurance_number, nationality, age, gender, account_status, PIN) VALUES ('Flint Grzegorecki', 'fgrzegorecki3@printfriendly.com', '1665 Farmco Parkway', '285-474-0468', '197-92-8267', 'Indonesia', 63, 'Male', 'Bad Standing', 4)",
                    "INSERT INTO customers (customer_name, email, address, phone_number, social_insurance_number, nationality, age, gender, account_status, PIN) VALUES ('Valeria Gulliman', 'vgulliman4@trellian.com', '946 Pepper Wood Trail', '605-660-1832', '758-88-9683', 'Thailand', 28, 'Female', 'Bad Standing', 4)",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('Toronto Metropolitan Bank', '100 King St W, Toronto, ON','00001','www.tmubank.ca','support@tmubank.ca','416-555-1000')",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('Toronto Metropolitan Bank', '200 Bay St, Toronto, ON','00002','www.tmubank.ca', 'support@tmubank.ca', '416-555-2000')",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('Toronto Metropolitan Bank', '300 Queen St W, Toronto, ON', '00003', 'www.tmubank.ca', 'support@tmubank.ca', '416-555-3000')",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('Toronto Metropolitan Bank', '400 Front St W, Toronto, ON', '00004', 'www.tmubank.ca', 'support@tmubank.ca', '416-555-4000')",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('Toronto Metropolitan Bank', '500 Bloor St W, Toronto, ON', '00005', 'www.tmubank.ca', 'support@tmubank.ca', '416-555-5000')",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('New York Metropolitan Bank', '100 Market St, New York, NY, USA', '00006', 'www.tmubank.ca', 'support@tmubank.ca', '212-555-6000')",
                    "INSERT INTO Bank (Bank_Name, Branch_Location, Transit_No, Website, Email, Contact_Number) VALUES ('London Metropolitan Bank', '200 Oxford St, London, UK', '00007', 'www.tmubank.ca', 'support@tmubank.ca', '+44-20-555-7000')",
                    "INSERT INTO Employee ( Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Kale Smooth', 'kale.smooth@tmuBank.com', '123 Cherry St, Toronto, ON', '123-456-7890', 30, 'Male', 'Canadian')",
                    "INSERT INTO Employee ( Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Rose Mary', 'rose.mary@tmuBank.com', '456 Derry Rd,  Mississauaga, CA', '987-654-3210', 28, 'Female', 'Canadian')",
                    "INSERT INTO Employee (Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Robert Johnson', 'robert.johnson@tmuBank.com', '789 WoodPine Rd,  Oakville, London', '555-123-4567', 35, 'Male', 'American')",
                    "INSERT INTO Employee ( Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Jolly Maez', 'jolly.maez@tmuBank.com', '321 Holiday Blvd,  North York, ON', '444-987-6543', 26, 'Female', 'Mexican')",
                    "INSERT INTO Employee (Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Casey Jordan', 'casey.jordan@tmuBank.com', '909 Maple Leaf Rd, Toronto, ON', '444-987-6544', 25, 'Non-Binary', 'Canadian')",
                    "INSERT INTO Employee (Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Avery Taylor', 'avery.taylor@tmuBank.com', '121 King St, New York, NY,', '187-654-3214', 31, 'Genderfluid', 'Canadian')",
                    "INSERT INTO Employee (Employee_Name, Email, Address, Phone_Number, Age, Gender, Nationality) VALUES ('Robin Knight', 'robin.knight@tmuBank.com', '99 Oxford Ave, London, UK', '787-654-3445', 28, 'Agender', 'Canadian')",
                    "INSERT INTO chequing_accounts (customer_id, balance, account_type) VALUES(1, 0, 'Student Account')",
                    "INSERT INTO chequing_accounts (customer_id, balance, account_type) VALUES(2, 0, 'RBC Banking')",
                    "INSERT INTO chequing_accounts (customer_id, balance, account_type) VALUES(3, 2000, 'RBC Banking')",
                    "INSERT INTO chequing_accounts (customer_id, balance, account_type) VALUES(4, 50000, 'RBC Banking')",
                    "INSERT INTO chequing_accounts (customer_id, balance, account_type) VALUES(5, 100000, 'RBC Banking Supreme')",
                    "INSERT INTO savings_accounts (customer_id, balance, account_type, interest_rate) VALUES(1, 0, 'Student Account', 0.25)",
                    "INSERT INTO savings_accounts (customer_id, balance, account_type, interest_rate) VALUES(3, 1000, 'Student Account', 0.25)",
                    "INSERT INTO savings_accounts (customer_id, balance, account_type, interest_rate) VALUES(4, 5.92, 'Student Account', 0.25)",
                    "INSERT INTO savings_accounts (customer_id, balance, account_type, interest_rate) VALUES(5, 6, 'Student Account', 0.25)",
                    "INSERT INTO savings_accounts (customer_id, balance, account_type, interest_rate) VALUES(2, 0, 'Economy Account', 0.75)",
                    "INSERT INTO credit_card_accounts (customer_id, card_number, balance, credit_limit, minimum_payment, expiration_date, payment_due_date) VALUES (1, 123456789, 0, 2500, 10, TO_DATE('17/12/2015', 'DD/MM/YYYY'), TO_DATE('17/12/2020', 'DD/MM/YYYY'))",
                    "INSERT INTO credit_card_accounts (customer_id, card_number, balance, credit_limit, minimum_payment, expiration_date, payment_due_date) VALUES (2, 987654321, 0, 2500, 10, TO_DATE('17/12/2017', 'DD/MM/YYYY'), TO_DATE('17/12/2023', 'DD/MM/YYYY'))",
                    "INSERT INTO credit_card_accounts (customer_id, card_number, balance, credit_limit, minimum_payment, expiration_date, payment_due_date) VALUES (3, 287654321, 1000, 10000, 10, TO_DATE('17/12/2027', 'DD/MM/YYYY'), TO_DATE('20/12/2023', 'DD/MM/YYYY'))",
                    "INSERT INTO loan (customer_id, principal, interest, payment_amount, status) VALUES (1, 50000.00, 5.25, 1200.00, 'active')",
                    "INSERT INTO loan (customer_id, principal, interest, payment_amount, status) VALUES (2, 10000.00, 6.00, 500.00, 'active')",
                    "INSERT INTO loan (customer_id, principal, interest, payment_amount, status) VALUES (3, 100000.00, 4.75, 2400.00, 'closed')",
                    "INSERT INTO loan (customer_id, principal, interest, payment_amount, status) VALUES (4, 70000.00, 5.50, 1100.00, 'closed')",
                    "INSERT INTO loan (customer_id, principal, interest, payment_amount, status) VALUES (5, 150000.00, 5.25, 3500.00, 'active')"
                };
                try (Statement stmt = conn1.createStatement()) {
                    for (String query : insertQueries) {
                        stmt.executeUpdate(query);
                        System.out.println("Executed: " + query); // Optionally log each query
                    }
                    System.out.println("Tables populated");
                    output.setText("Tables populated");
                } catch (SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    System.out.println(ex.getMessage());
                    output.setText(ex.getMessage());

                }
            }
        });

        queryTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Query Tables button clicked.");
                output.setText("");
                String[] selectQueries = {
                    "SELECT account_status, COUNT(*) AS Customer_Count FROM customers GROUP BY account_status ORDER BY Customer_Count DESC",
                    "SELECT DISTINCT nationality FROM customers ORDER BY nationality",
                    "SELECT Bank_Name, Branch_Location FROM Bank GROUP BY Bank_Name, Branch_Location ORDER BY Bank_Name ASC",
                    "SELECT Gender, COUNT(*) AS Employee_Count FROM Employee GROUP BY Gender ORDER BY Employee_Count DESC",
                    "SELECT customers.customer_name,customers.customer_id, chequing_accounts.account_id, chequing_accounts.balance, transactions.transaction_id, transactions.transaction_amount FROM customers JOIN chequing_accounts ON customers.customer_id = chequing_accounts.customer_id JOIN transactions ON customers.customer_id = transactions.customer_id",
                    "CREATE VIEW CustomerValidTransactions AS SELECT customers.customer_name,customers.customer_id, chequing_accounts.account_id, chequing_accounts.balance, transactions.transaction_id, transactions.transaction_amount FROM customers JOIN chequing_accounts ON customers.customer_id = chequing_accounts.customer_id JOIN transactions ON customers.customer_id = transactions.customer_id",
                    "SELECT * FROM CustomerValidTransactions WHERE balance < transaction_amount",
                    "SELECT customer_id, balance FROM chequing_accounts WHERE balance > 25000 ORDER BY balance DESC",
                    "SELECT account_type, SUM(balance) AS total_balance FROM chequing_accounts WHERE balance > 25000 GROUP BY account_type ORDER BY total_balance DESC",
                    "SELECT customer_id, balance, account_type FROM savings_accounts WHERE account_type = 'Student Account' ORDER BY balance DESC",
                    "SELECT DISTINCT balance FROM savings_accounts ORDER BY balance",
                    "SELECT c.customer_id, c.customer_name, chequing_accounts.balance AS chequing_balance, cca.balance AS credit_card_balance, sa.balance AS savings_balance FROM customers c JOIN chequing_accounts ON c.customer_id = chequing_accounts.customer_id JOIN credit_card_accounts cca ON c.customer_id = cca.customer_id JOIN savings_accounts sa ON c.customer_id = sa.customer_id",
                    "CREATE VIEW high_ccas AS SELECT c.customer_id, c.customer_name, cca.balance FROM customers c JOIN credit_card_accounts cca ON c.customer_id = cca.customer_id WHERE cca.balance > 2500",
                    "SELECT * FROM high_ccas",
                    "SELECT principal as amount FROM loan GROUP BY principal ORDER BY amount ASC",
                    "SELECT transaction_date, transaction_time FROM transactions GROUP BY transaction_date, transaction_time ORDER BY transaction_date",
                    "SELECT transactions.transaction_date, transactions.transaction_amount, customers.customer_name, monthly_statements.account_type, monthly_statements.opening_balance, monthly_statements.closing_balance FROM transactions JOIN customers on transactions.customer_id = customers.customer_id JOIN monthly_statements on transactions.transaction_id = monthly_statements.transaction_id",
                    "UPDATE monthly_statements SET closing_balance = 3500.00 WHERE customer_id = 1",
                    "UPDATE monthly_statements SET closing_balance = 7000 WHERE customer_id = 2",
                    "CREATE VIEW Above_A_Thousand AS SELECT transactions.transaction_amount, transactions.transaction_date, customers.customer_name FROM transactions JOIN customers on transactions.customer_id = customers.customer_id WHERE transactions.transaction_amount > 1000",
                    "SELECT * FROM above_a_thousand",
                    "SELECT c.customer_id, c.customer_name FROM customers c WHERE EXISTS (SELECT 1 FROM savings_accounts sa WHERE sa.customer_id = c.customer_id AND sa.interest_rate > (SELECT AVG(interest_rate) FROM savings_accounts))",
                    "SELECT c.customer_id, c.customer_name, AVG(cca.balance) AS Avg_CCA_Balance FROM credit_card_accounts cca JOIN customers c ON c.customer_id = cca.customer_id GROUP BY c.customer_id, c.customer_name HAVING AVG(cca.balance) < (SELECT AVG(balance) FROM credit_card_accounts)",
                    "SELECT c.customer_id, c.customer_name, t.transaction_amount AS amount, 'Transaction below 2000' AS source FROM customers c JOIN transactions t ON c.customer_id = t.customer_id WHERE c.nationality != 'China' AND t.transaction_amount < 2000 UNION SELECT c.customer_id, c.customer_name, ms.closing_balance AS amount, 'Account with sufficient funds' AS source FROM customers c JOIN monthly_statements ms ON c.customer_id = ms.customer_id WHERE c.nationality != 'China' AND ms.closing_balance > 5000",
                    "SELECT c.customer_id, c.customer_name, ca.balance AS chequing_balance, s.balance AS savings_balance, l.principal FROM customers c JOIN chequing_accounts ca ON c.customer_id = ca.customer_id JOIN savings_accounts s ON c.customer_id = s.customer_id JOIN loan l ON c.customer_id = l.customer_id WHERE (s.balance + ca.balance) < 3000 AND l.status = 'active'",
                    "SELECT loan_id, principal, status, COUNT(customer_id) AS Active_loans FROM loan GROUP BY loan_id, principal, status ORDER BY principal ASC",
                    "SELECT customer_id, customer_name, gender FROM Customers MINUS SELECT customer_id, customer_name, gender FROM Customers WHERE Gender = 'Male'",
                    "SELECT c.customer_id, c.age, c.customer_name FROM Customers c JOIN chequing_accounts a1 ON c.customer_id = a1.customer_id JOIN savings_accounts a2 ON c.customer_id = a2.customer_id WHERE NOT EXISTS (SELECT 1 FROM loan a3 WHERE a3.customer_id = c.customer_id) ORDER BY c.age DESC"
                };
							
                try (Statement stmt = conn1.createStatement()) {
                    for (String query : selectQueries) {
                        ResultSet rs = stmt.executeQuery(query);
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(metaData.getColumnName(i) + "\t");
                        }
                        System.out.println();

                        while(rs.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(rs.getObject(i) + "\t");
                            }
                            System.out.println();
                        }
                    }
                    System.out.println("Tables queried");
                    output.setText("Tables queried");
                } catch (SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    output.setText(ex.getMessage());

                }
            }
        });
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Read Tables button clicked.");
                output.setText("");
                
                // Opens up a modal to query
                Modal modal = new Modal(conn1);
                String userInput = modal.createModal(frame);
                try (Statement stmt = conn1.createStatement()) {
                    ResultSet rs = stmt.executeQuery(userInput);
                    Modal.parseResponse(rs, output);
                } catch (SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    output.setText(ex.getMessage());

                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete button clicked.");
                output.setText("");
                // Opens up a modal to query
                Modal modal = new Modal(conn1);
                String userInput = modal.createModal(frame);
                try (Statement stmt = conn1.createStatement()) {
                    stmt.executeUpdate(userInput);
                    System.out.println("Record deleted");
                    output.setText("Record deleted");
                } catch (SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    output.setText(ex.getMessage());

                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update button clicked.");
                output.setText("");

                Modal modal = new Modal(conn1);
                String userInput = modal.createModal(frame);
                try (Statement stmt = conn1.createStatement()) {
                    stmt.executeUpdate(userInput);
                    System.out.println("Record updated");
                    output.setText("Record updated");
                } catch (SQLException ex) {
                    System.out.println(ex.getErrorCode());
                    output.setText(ex.getMessage());

                }
            }
        });


        // Set layout and add buttons to the frame
        buttonPanel.add(dropTablesButton);
        buttonPanel.add(createTablesButton);
        buttonPanel.add(populateTablesButton);
        buttonPanel.add(queryTablesButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Make the frame visible
        frame.setVisible(true);
    }
}

class Modal {
    private Connection conn1;
    public Modal(Connection conn1) {
        this.conn1 = conn1;
    }

    public String createModal(JFrame parentFrame) {
        // Create the modal dialog
        JDialog modalDialog = new JDialog(parentFrame, "Modal Dialog", true);
        modalDialog.setSize(300, 200);
        modalDialog.setLocationRelativeTo(parentFrame);        
        modalDialog.setLayout(new FlowLayout());
        modalDialog.add(new JLabel("Enter query:"));
        JTextField textField = new JTextField(20);
        JButton closeButton = new JButton("Submit");
        
        String[] userInput = new String[1];
        // On close, we send the result up to the parent frame
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInput[0] = textField.getText();
                // System.out.println("User entered: " + textField.getText());
                modalDialog.dispose(); // Close after clicked
            }
        });

        modalDialog.add(textField);
        modalDialog.add(closeButton);
        
        // Show the modal dialog
        modalDialog.setVisible(true);
        return userInput[0];
    }

    public static void parseResponse(ResultSet rs, JTextArea output) throws SQLException {
        String stringOutput = "";

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Append column headers
            for (int i = 1; i <= columnCount; i++) {
                stringOutput += (metaData.getColumnName(i)) + ("\t");
            }
            stringOutput += ("\n");

            // Append rows of data
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    stringOutput += value + "\t";
                }
                stringOutput += ("\n");
            }
            output.setText(stringOutput.toString());
        } catch (SQLException ex) {
            output.setText(ex.getMessage());
        }
    }
}