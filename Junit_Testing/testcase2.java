package src;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class testcase2 {

    @Test
    public void testAppointmentsButtonAction() {
        JComboBox<String> combobox = new JComboBox<>();
        combobox.addItem("Dr. Sedenia");

        AppointmentViewer appointmentViewer = new AppointmentViewer(combobox);
        JButton viewAppointmentsButton = appointmentViewer.getViewAppointmentsButton();

        // Simulate selecting a doctor from the combo box
        combobox.setSelectedIndex(0);

        // Create a mock action event for the button click
        ActionEvent mockEvent = new ActionEvent(viewAppointmentsButton, ActionEvent.ACTION_PERFORMED, "");

        // Simulate button click
        viewAppointmentsButton.getActionListeners()[0].actionPerformed(mockEvent);

        // Get the expected message
        ArrayList<String> mockAppointments = appointmentViewer.getMockAppointmentsForDoctor("Dr. Sedenia");
        String expectedMessage = "Appointments for Dr. Sedenia:\n" + String.join("\n", mockAppointments);

        // Get the actual message displayed in the dialog
        // Assuming this method is implemented elsewhere
        String actualMessage = JOptionPaneTest.getMessageInModalDialog();

        // Check if the actual message matches the expected message
        assertEquals(expectedMessage, actualMessage);
    }

    //private class ViewDocsTest {
    

    @Test
    public void testGetDetails() {
        try {
            // Mocked data
            String[][] expectedData = {
                    {"Dr John Smith", "Cardiology", "M", "john@example.com"},
                    {"Dr Emily Johnson", "Pediatrics", "F", "jane@example.com"},
                    {"Dr Michael Lee", "Orthopedics", "M", "michael@example.com"},
                    {"Dr Sarah Brown", "Oncology", "F", "sarah@example.com"},
                    {"Dr David Kim", "Neurology", "M", "david@example.com"}
            };

            // Mocking database connection
            Connection connectionMock = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctorsList", "root", "123456789");
            Statement statementMock = connectionMock.createStatement();
            ResultSet resultSetMock = statementMock.executeQuery("SELECT DoctorName, Department, Gender, EmailAddress FROM doctors");

            // Creating an instance of the viewDocs class
            viewDocs view = new viewDocs();

            // Calling the getDetails() method to retrieve data
            String[][] actualData = view.getDetails();

            // Checking if data is not null
            assertNotNull(actualData);

            // Checking if data has rows
            assertTrue(actualData.length > 0);

            // Comparing actual data with expected data
            assertArrayEquals(expectedData, actualData);

        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    @Test
    public void ViewBookingsTest() {
        // Create a test JFrame
        JFrame testFrame = new JFrame();

        // Create an instance of ViewBookings
        ViewBookings viewBookings = new ViewBookings(testFrame);

        // Manually set values for JComboBox and JTextField
        JComboBox<String> monthComboBox = (JComboBox<String>) getField(viewBookings, "monthComboBox");
        monthComboBox.setSelectedItem("March");

        JTextField yearTextField = (JTextField) getField(viewBookings, "yearTextField");
        yearTextField.setText("2024");

        // Manually invoke the submitButtonClicked method
        viewBookings.submitButtonClicked(); 

        // Retrieve the statement and connection objects for verification
        Statement statement = (Statement) getField(viewBookings, "statement");

        // Ensure that statement is not null, indicating successful database connection
        assertEquals(true, statement != null);
    }

    // Utility method to access private fields using reflection
    private Object getField(Object object, String fieldName) {
        try {
            java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


        public void testShowPrescriptions() {
            try {
                // Establish database connection
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctorsList", "root", "123456789");
                PreparedStatement statement = connection.prepareStatement("SELECT visit_details FROM Patient WHERE firstName = 'Emma'");
                ResultSet resultSet = statement.executeQuery();

                // Display past visit details
                StringBuilder prescriptions = new StringBuilder("Past visit Details and Prescriptions for Emma:\n");
                while (resultSet.next()) {
                    prescriptions.append(resultSet.getString("Visit_Details")).append("\n");
                }
                // Show the prescriptions and past to the user
                System.out.println(prescriptions.toString());

                // Close database resources
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching prescriptions: " + e.getMessage());
            }
        }
    }


