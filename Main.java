import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    final private Font mainFont = new Font("Courier New", Font.BOLD, 16);
    JTextField tfCustomerName, tfEmail, tfAddress, tfPhoneNumber, tfSocialInsuranceNumber, tfNationality, tfAge, tfGender, tfAccountStatus, tfPIN;

    public void initialize() {
        //Insert customer w/ the following information
        JLabel lblCustomerName = new JLabel("Customer Name:");
        lblCustomerName.setFont(mainFont);
        tfCustomerName = new JTextField();
        tfCustomerName.setFont(mainFont);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(mainFont);
        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(mainFont);
        tfAddress = new JTextField();
        tfAddress.setFont(mainFont);

        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        lblPhoneNumber.setFont(mainFont);
        tfPhoneNumber = new JTextField();
        tfPhoneNumber.setFont(mainFont);

        JLabel lblSocialInsuranceNumber = new JLabel("Social Insurance Number:");
        lblSocialInsuranceNumber.setFont(mainFont);
        tfSocialInsuranceNumber = new JTextField();
        tfSocialInsuranceNumber.setFont(mainFont);

        JLabel lblNationality = new JLabel("Nationality:");
        lblNationality.setFont(mainFont);
        tfNationality = new JTextField();
        tfNationality.setFont(mainFont);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setFont(mainFont);
        tfAge = new JTextField();
        tfAge.setFont(mainFont);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setFont(mainFont);
        tfGender = new JTextField();
        tfGender.setFont(mainFont);

        JLabel lblAccountStatus = new JLabel("Account Status:");
        lblAccountStatus.setFont(mainFont);
        tfAccountStatus = new JTextField();
        tfAccountStatus.setFont(mainFont);

        JLabel lblPIN = new JLabel("PIN:");
        lblPIN.setFont(mainFont);
        tfPIN = new JTextField();
        tfPIN.setFont(mainFont);

        //JPanel that holds all labels and textfields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(10, 2, 5, 5)); // 10 rows, 2 columns
        formPanel.add(lblCustomerName);
        formPanel.add(tfCustomerName);
        formPanel.add(lblEmail);
        formPanel.add(tfEmail);
        formPanel.add(lblAddress);
        formPanel.add(tfAddress);
        formPanel.add(lblPhoneNumber);
        formPanel.add(tfPhoneNumber);
        formPanel.add(lblSocialInsuranceNumber);
        formPanel.add(tfSocialInsuranceNumber);
        formPanel.add(lblNationality);
        formPanel.add(tfNationality);
        formPanel.add(lblAge);
        formPanel.add(tfAge);
        formPanel.add(lblGender);
        formPanel.add(tfGender);
        formPanel.add(lblAccountStatus);
        formPanel.add(tfAccountStatus);
        formPanel.add(lblPIN);
        formPanel.add(tfPIN);

       
        JButton btnCreate = new JButton("Create");
        btnCreate.setFont(mainFont);

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(mainFont);

        JButton btnDrop = new JButton("Drop");
        btnDrop.setFont(mainFont);

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDrop);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        add(mainPanel);
        setTitle("Retail Banking System Database");
        setSize(600, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        Main frame = new Main();
        frame.initialize();
    }
}
