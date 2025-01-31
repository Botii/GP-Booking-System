package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ChangeAppointment extends JFrame implements ActionListener {
    private JTextField patientIdField;
    private JTextField newDateField;
    private JTextField newTimeField;
    private JButton changeButton;
    private JButton homeButton;
    int pid;

    public ChangeAppointment(int pid) {
        this.pid = pid;
        // connection details required
        String url = "jdbc:mysql://localhost:3306/database";
        String username = "root";
        String password = "123";
        setTitle("Change Appointment");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with a grid layout
        JPanel panel = new JPanel(new GridLayout(4, 2));

        // Create the labels and text fields
        JLabel patientIdLabel = new JLabel("Appointment ID:");
        patientIdField = new JTextField();
        JLabel newDateLabel = new JLabel("New Date (YYYY-MM-DD):");
        newDateField = new JTextField();
        JLabel newTimeLabel = new JLabel("New Time (HH:MM):");
        newTimeField = new JTextField();

        // Create the buttons
        changeButton = new JButton("Change Appointment");
        changeButton.addActionListener(this);
        homeButton = new JButton("Home");
        homeButton.addActionListener(this);

        panel.add(patientIdLabel);
        panel.add(patientIdField);
        panel.add(newDateLabel);
        panel.add(newDateField);
        panel.add(newTimeLabel);
        panel.add(newTimeField);
        panel.add(changeButton);
        panel.add(homeButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeButton) {
            int patientId = Integer.parseInt(patientIdField.getText());
            String newDate = newDateField.getText();
            String newTime = newTimeField.getText();

            // Parse the new date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate newDateLocal = LocalDate.parse(newDate, formatter);

                // Check if the new date is before today's date
                if (newDateLocal.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "New date cannot be before today's date.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter date in 'YYYY-MM-DD' format.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call the method to change the appointment
            changeAppointment(patientId, newDate, newTime);
        } else if (e.getSource() == homeButton) {
            // Close the current window
            ((JFrame) SwingUtilities.getWindowAncestor(homeButton)).dispose();

            // Create a new frame 
            JFrame homePageFrame = new JFrame();

            // Create an instance of the home page 
            home_page homePage = new home_page(homePageFrame, home_page.loginForm, pid);

            // Set all the required stuff and make it visible
            homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            homePageFrame.setSize(500, 500); 
            homePageFrame.setLocationRelativeTo(null); 
            homePageFrame.setVisible(true);
        }
    }

    private void changeAppointment(int appointmentId, String newDate, String newTime) {
        // SQL query to update the appointment
        String sql = "UPDATE Appointments SET Date = ?, Time = ? WHERE Appointment_ID = ?";
        
        String url = "jdbc:mysql://localhost:3306/doctorsList";
        String username = "root";
        String password = "123456789";
        
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameters for the prepared statement
            preparedStatement.setString(1, newDate);
            preparedStatement.setString(2, newTime);
            preparedStatement.setInt(3, appointmentId);
    
            // Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update appointment. Appointment ID not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

  
}
