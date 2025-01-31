//import static org.junit.Assert.assertEquals;
//import org.junit.Test;
import javax.swing.*;
import org.junit.Test;

import src.DBManager;
import src.Login_form;

import org.junit.Before;
import static org.junit.Assert.*;

    public class testCases {
    
        private DoctorChangeSystem doctorChangeSystem;
    // The setup method is annoted with @Before meaning it runs before each test
    // It iniatlises the doctorChnageSytem instance 
        @Before 
        public void setUp() {
            doctorChangeSystem = new DoctorChangeSystem();
        }
    //This test checks if the doctor lisin the combo box os not empty
        @Test
        public void testDoctorListNotEmpty() {
            // Retrieves the combo box from the content pane of the DoctorChangeSystem instance.
            JComboBox<String> comboBox = (JComboBox<String>)doctorChangeSystem.getContentPane().getComponent(0);
             // Asserts that the combo box should contain at least one item (doctor).
            assertTrue("Doctor list should not be empty", comboBox.getItemCount() > 0);
        }
    // This test verifies if the combo box contains a specific doctor, "Dr. Sedenia".
        @Test
        public void testDoctorListContainsSpecificDoctor() {
            JComboBox<String> comboBox = (JComboBox<String>)doctorChangeSystem.getContentPane().getComponent(0);  //Test for the 
            boolean containsDoctor = false;
                // Iterates over the items in the combo box to check if "Dr. Sedenia" is present.
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                if (comboBox.getItemAt(i).equals("Dr. Sedenia")) {
                    containsDoctor = true;
                    break;
                }
            }
                // Asserts that "Dr. Sedenia" is indeed in the list
            assertTrue("Doctor list should contain Dr. Sedenia", containsDoctor);
        }
     // This test checks if the "Change Doctor" button exists in the GUI.
        @Test
        public void testChangeDoctorButtonExists() {
            JButton changeDoctorButton = (JButton)doctorChangeSystem.getContentPane().getComponent(1);
            assertNotNull("Change doctor button should exist", changeDoctorButton);
        }
        
        @Test
        public void testValidLogin() {
            JPanel mainPanel = new JPanel();
            Login_form login = new Login_form(mainPanel);
            // Test data add this into your database
            String expectedUsername = "john";
            String expectedPassword = "password";
            // Flag to track if a match is found
            assertTrue(login.validateLogin(expectedUsername,expectedPassword));
        }
    
        

        //reverse of valid login
        @Test
        public void testInvalidLogin(){
            JPanel mainPanel = new JPanel();
            Login_form login = new Login_form(mainPanel);
            //test data
            String expectedUsername = "john";
            String expectedPassword = "password";
            // Flag to track if a match is found
            assertFalse(login.validateLogin(expectedUsername, expectedPassword));
        }
        // Additional tests can be added here to test other functionalities
    }
    













