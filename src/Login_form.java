
package src;
import javax.swing.*;   
import java.awt.event.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  
import java.sql.Statement;

class Login_form extends JFrame implements ActionListener {  
    //initialize the buttons and labels
    JFrame frame;
    JButton login_button;  
    JButton forgot_password_button; // Declaring the class-level variable here
    JPanel mainPanel;
    JLabel patient_username_label, patient_password_label;  
    JTextField patient_username_TextField, patient_password_TextField;
    Statement statement;
    int loggedInPatientID;

    //use the dbmanager openconnection method
    // Database connection details
    //String url = "jdbc:mysql://localhost:3306/your_database_name";
    //String username = "your_username";
    //String password = "your_password";

    Login_form(JFrame frame, JPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.frame = frame;
        frame.setTitle("Patient Login Form"); //set the title of the frame

        //set the layout to a grid layout
        mainPanel.setLayout(null);

        //create the button with login text inside
        login_button = new JButton("login");
        setLocationRelativeTo(null);

        //create the forgot password button (using the class-level variable)
        forgot_password_button = new JButton("Forgot password");

        //create the labels and text fields
        patient_username_label = new JLabel("Enter patient username");
        patient_password_label = new JLabel("Enter patient password");
        patient_username_TextField = new JTextField();
        patient_password_TextField = new JPasswordField(); // Use JPasswordField for password input

        // Set the bounds for each component
        patient_username_label.setBounds(20, 20, 150, 30);
        patient_password_label.setBounds(20, 60, 150, 30);
        patient_username_TextField.setBounds(180, 20, 200, 30);
        patient_password_TextField.setBounds(180, 60, 200, 30);
        login_button.setBounds(150, 100, 100, 30);
        forgot_password_button.setBounds(260, 100, 130, 30);
       
        //add an action listener to the buttons
        login_button.addActionListener(this); 
        forgot_password_button.addActionListener(this);

        // Add components to the main panel
        mainPanel.add(patient_username_label);
        mainPanel.add(patient_password_label);
        mainPanel.add(patient_username_TextField);
        mainPanel.add(patient_password_TextField);
        mainPanel.add(login_button);
        mainPanel.add(forgot_password_button);

        //add form panel into the main frame
        frame.add(mainPanel);
        frame.setSize(450,300); 
        frame.setVisible(true);
    }
    
    // Action listener for the buttons
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login_button) {
            String username = patient_username_TextField.getText();
            String password = new String(((JPasswordField)patient_password_TextField).getPassword());
            String hashedPassword = hashPassword(password); // Hash the provided password
            if (validateLogin(username, hashedPassword)) {
                // Redirect to the next screen or perform other actions upon successful login
                System.out.println("Login successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Patient Login Error", JOptionPane.ERROR_MESSAGE);
             }
        } else if (ae.getSource() == forgot_password_button) {
            // Forgot password button action
            open_email();
        }
    }

     // Validate the login credentials
     private boolean validateLogin(String username, String password) {
        try {
            // Establish database connection
            DBManager sqlconnect = new DBManager();
            sqlconnect.openConnection();

            // Create a PreparedStatement for executing the SQL query
            PreparedStatement preparedStatement = sqlconnect.getConnection().prepareStatement("SELECT * FROM patients WHERE Username = ? AND Password = ?");
            
            // Set parameter values
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if the result set has any rows
            if (resultSet.next()) {
                loggedInPatientID = resultSet.getInt("Patient_ID");
                
                // If login is successful, open the home page and set login panel to invisible
                home_page homepage = new home_page(frame,this,loggedInPatientID);
                mainPanel.setVisible(false);
                sqlconnect.closeConnection();
                return true;
            } else {
                // Return false if credentials are invalid
                sqlconnect.closeConnection();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hash the password using SHA-256 algorithm
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Open the email app for patients to contact support team
    private void open_email() {
        try {
            // Opens the mail app for patients to email the support team about password issues.
            String url = "mailto:support@example.com";
            Runtime.getRuntime().exec("cmd /c start " + url);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Error message incase 
            JOptionPane.showMessageDialog(this, "Failed to open Windows Mail app.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
