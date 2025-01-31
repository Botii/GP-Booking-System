package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorsDetails extends JFrame {

    private JTable table;
    private JButton goHomeButton;

    public DoctorsDetails() {
        setTitle("Doctor Information");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create go home button
        goHomeButton = new JButton("Go Home");
        goHomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action to perform when go home button is clicked
                // For now, just closing the current window
                dispose();
            }
        });
        add(goHomeButton, BorderLayout.SOUTH);

        // Fetch data from database and display in table
        fetchDoctorData();

        setVisible(true);
    }

    private void fetchDoctorData() {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctorsList", "root", "123456789");

            // Create statement
            Statement statement = connection.createStatement();

            // Execute query
            String query = "SELECT Doctor_ID, DoctorName, Department, Gender, EmailAddress FROM Doctors";
            ResultSet resultSet = statement.executeQuery(query);

            // Populate data into table
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Doctor ID");
            model.addColumn("Name");
            model.addColumn("Department");
            model.addColumn("Gender");
            model.addColumn("Email");

            while (resultSet.next()) {
                String doctorID = resultSet.getString("Doctor_ID");
                String name = resultSet.getString("DoctorName");
                String department = resultSet.getString("Department");
                String gender = resultSet.getString("Gender");
                String email = resultSet.getString("EmailAddress");

                model.addRow(new Object[]{doctorID, name, department, gender, email});
            }

            table.setModel(model);

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch data from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
