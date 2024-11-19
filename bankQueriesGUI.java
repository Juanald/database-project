import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class bankQueriesGUI extends JFrame{

    public bankQueriesGUI(){
        setTitle("Retail Bank Query System");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dropdown for Query Type
        JPanel queryTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        queryTypePanel.add(new JLabel("Query Type:"));
        String[] queryTypes = {"Customer Information", "Account Details", "Transaction History", "Bank Information", "Employee Information", "Loans"};
        JComboBox<String> queryTypeDropdown = new JComboBox<>(queryTypes);
        queryTypePanel.add(queryTypeDropdown);

        // Input Fields for Search
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 5));
        inputPanel.add(new JLabel("Customer ID:"));
        JTextField customerIdField = new JTextField();
        inputPanel.add(customerIdField);

        inputPanel.add(new JLabel("Account ID:"));
        JTextField accountIdField = new JTextField();
        inputPanel.add(accountIdField);

        inputPanel.add(new JLabel("Transaction Date:"));
        JTextField transactionDateField = new JTextField();
        inputPanel.add(transactionDateField);

        inputPanel.add(new JLabel("Amount Range:"));
        JTextField amountRangeField = new JTextField();
        inputPanel.add(amountRangeField);

        inputPanel.add(new JLabel("Transit#:"));
        JTextField transitNo = new JTextField();
        inputPanel.add(transitNo);

        inputPanel.add(new JLabel("Employee ID:"));
        JTextField employeeIdField = new JTextField();
        inputPanel.add(employeeIdField);

        // Radio Buttons for Account Type
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(new JLabel("Account Type:"));
        JRadioButton savingsButton = new JRadioButton("Savings");
        JRadioButton chequingButton = new JRadioButton("Chequing");
        JRadioButton creditButton = new JRadioButton("Credit");

        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(savingsButton);
        accountTypeGroup.add(chequingButton);
        accountTypeGroup.add(creditButton);

        radioPanel.add(savingsButton);
        radioPanel.add(chequingButton);
        radioPanel.add(creditButton);

        // Search Button
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        

        // Results Table
        String[] columns = {"Result"};
        JTable resultsTable = new JTable(new Object[][]{}, columns);
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Results"));

        // Add components to the main panel
        mainPanel.add(queryTypePanel);
        mainPanel.add(inputPanel);
        mainPanel.add(radioPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(tableScrollPane);

        // Add main panel to frame
        add(mainPanel);

        // Search Button Action
        searchButton.addActionListener(e -> {
            String selectedQuery = (String) queryTypeDropdown.getSelectedItem();
            String customerId = customerIdField.getText();
            String accountId = accountIdField.getText();
            String transactionDate = transactionDateField.getText();
            String amountRange = amountRangeField.getText();

            String selectedAccountType = null;
            if (savingsButton.isSelected()) {
                selectedAccountType = "Savings";
            } else if (chequingButton.isSelected()) {
                selectedAccountType = "Checking";
            } else if (creditButton.isSelected()) {
                selectedAccountType = "Credit";
            }

            // Display selected options (Replace this with query logic)
            JOptionPane.showMessageDialog(this, String.format(
                "Query Type: %s\nCustomer ID: %s\nAccount ID: %s\nTransaction Date: %s\nAmount Range: %s\nAccount Type: %s",
                selectedQuery, customerId, accountId, transactionDate, amountRange, selectedAccountType
            ));
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            bankQueriesGUI form = new bankQueriesGUI();
            form.setVisible(true);
        });
    }
    
}