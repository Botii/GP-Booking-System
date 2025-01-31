package src;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
public class AppointmentViewer {
    // GUI components
    private JButton viewAppointmentsButton;
    private JComboBox<String> doctorListComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    // Database credentials (should ideally be stored in a more secure way)
    private final String DB_URL = "jdbc:mysql://localhost:3306/doctorsList";
    private final String USER = "root";
    private final String PASS = "123456789";

    public AppointmentViewer(JComboBox<String> doctorListComboBox, JComboBox<Integer> monthComboBox, JComboBox<Integer> yearComboBox) {
        this.doctorListComboBox = doctorListComboBox;
        this.monthComboBox = monthComboBox;
        this.yearComboBox = yearComboBox;
        viewAppointmentsButton = new JButton("View Appointments");
        setUpButtonListener();
    }

    public JButton getViewAppointmentsButton() {
        return viewAppointmentsButton;
    }

    private void setUpButtonListener() {
        viewAppointmentsButton.addActionListener(e -> {
            String selectedDoctor = (String) doctorListComboBox.getSelectedItem();
            int selectedMonth = (int) monthComboBox.getSelectedItem();
            int selectedYear = (int) yearComboBox.getSelectedItem();
            displayAppointmentsForDoctor(selectedDoctor, selectedMonth, selectedYear);
        });
    }

    private void displayAppointmentsForDoctor(String doctorName, int desiredMonth, int desiredYear) {
        ArrayList<String> appointments = getAppointmentsForDoctor(doctorName, desiredMonth, desiredYear);
        String message = "Appointments for " + doctorName + ":\n" + String.join("\n", appointments);
        JOptionPane.showMessageDialog(null, message);
    }

    private ArrayList<String> getAppointmentsForDoctor(String doctorName, int desiredMonth, int desiredYear) {
        ArrayList<String> appointments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT Patient_ID, Date FROM Appointments WHERE MONTH(Date) = ? AND YEAR(Date) = ?";
            stmt = conn.prepareStatement(sql); 
            stmt.setInt(1, desiredMonth);
            stmt.setInt(2, desiredYear);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String patientID = rs.getString("Patient_ID");
                String date = rs.getString("Date");
                appointments.add("Patient_ID: " + patientID + ", Date: " + date);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return appointments;
    }
}
