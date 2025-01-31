package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ViewPrescription extends JFrame {

    public void fetchAndDisplayPrescriptions(int patientId, JFrame frame) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctorsList", "root", "123456789");
            PreparedStatement statement = connection.prepareStatement("SELECT description FROM visit_details WHERE patient_id = ?");
            statement.setInt(1, patientId); // Set the parameterized query with patientId
            ResultSet resultSet = statement.executeQuery();

            // Display past visit details and prescriptions
            StringBuilder message = new StringBuilder("Past visit Details and Prescriptions for Patient ID: " + patientId + ":\n");
            while (resultSet.next()) {
                //String visitDetails = resultSet.getString("visit_details");
                String prescriptions = resultSet.getString("description");
                message.append("Visit Details: ").append(prescriptions).append("\n");
            }
            // Show the message to the user
            JOptionPane.showMessageDialog(frame, message.toString(), "Past Visit Details and Prescriptions", JOptionPane.INFORMATION_MESSAGE);

            // Close database resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching visit details and prescriptions", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
