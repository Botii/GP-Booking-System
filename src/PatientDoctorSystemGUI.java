package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Main class representing the system
public class PatientDoctorSystemGUI extends JFrame {
    private Connection connection;
    private JComboBox<String> doctorComboBox;
    int pid;

    // Constructor
    public PatientDoctorSystemGUI(int pid) {
        this.pid = pid;
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctorsList", "root", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up the GUI components
        setTitle("Patient Doctor System");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create panel for patient registration
        JPanel patientPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        patientPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        patientPanel.add(new JLabel("Patient Name:"));
        JTextField patientNameField = new JTextField();
        patientPanel.add(patientNameField);
        patientPanel.add(new JLabel("Patient Email:"));
        JTextField patientEmailField = new JTextField();
        patientPanel.add(patientEmailField);
        patientPanel.add(new JLabel("Choose Doctor:"));
        doctorComboBox = new JComboBox<>();
        populateDoctorComboBox();
        patientPanel.add(doctorComboBox);

        // Create button to register patient
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patientName = patientNameField.getText();
                String patientEmail = patientEmailField.getText();
                Doctor chosenDoctor = (Doctor) doctorComboBox.getSelectedItem();
                registerPatient(patientName, patientEmail, chosenDoctor);
            }
        });
        patientPanel.add(registerButton);


        // Create a back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JFrame) SwingUtilities.getWindowAncestor(backButton)).dispose();

                //create a new frame
                JFrame homePageFrame = new JFrame();

                //create an instance of the home page
                home_page homePage = new home_page(homePageFrame, home_page.loginForm, pid);

                //set all the required stuff and make it visible
                homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                homePageFrame.setSize(500, 500);
                homePageFrame.setLocationRelativeTo(null);
                homePageFrame.setVisible(true);
            }
        });
        patientPanel.add(backButton);

        add(patientPanel, BorderLayout.CENTER);
    }

    // Method to populate doctor combo box with data from database
    private void populateDoctorComboBox() {
        List<Doctor> doctors = getAllDoctorsFromDatabase();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Doctor doctor : doctors) {
            doctorComboBox.addItem(doctor.getName()); // Add only doctor names
        }

    }

    // Method to register a new patient
    private void registerPatient(String patientName, String patientEmail, Doctor chosenDoctor) {
        // Create new patient object
        Patient patient = new Patient(patientName, patientEmail);
        // Insert patient into database
        insertPatientIntoDatabase(patient);

        // Send confirmation message to patient
        sendConfirmationToPatient(patient);
        // Send confirmation message to chosen doctor
        sendConfirmationToDoctor(patient, chosenDoctor);
    }

    // Method to send confirmation message to patient
    private void sendConfirmationToPatient(Patient patient) {
        // Logic to send confirmation message to patient via email or other means
        JOptionPane.showMessageDialog(this, "Confirmation message sent to patient: " + patient.getName());
    }

    // Method to send confirmation message to doctor
    private void sendConfirmationToDoctor(Patient patient, Doctor doctor) {
        // Logic to send confirmation message to doctor via email or other means
        JOptionPane.showMessageDialog(this, "Confirmation message sent to doctor: " + doctor.getName() + " regarding patient: " + patient.getName());
    }

    // Method to insert a patient into the database
    private void insertPatientIntoDatabase(Patient patient) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO patients (name, email) VALUES (?, ?)");
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch all doctors from database
    private List<Doctor> getAllDoctorsFromDatabase() {
        List<Doctor> doctors = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM doctors");
            while (resultSet.next()) {
                int id = resultSet.getInt("Doctor_ID");
                String name = resultSet.getString("DoctorName");
                String specialization = resultSet.getString("Department");
                doctors.add(new Doctor(id, name, specialization));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Define a class to represent a Patient
    static class Patient {
        private String name;
        private String email;

        // Constructor
        public Patient(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    // Define a class to represent a Doctor
    static class Doctor {
        private int id;
        private String name;
        private String specialization;

        // Constructor
        public Doctor(int id, String name, String specialization) {
            this.id = id;
            this.name = name;
            this.specialization = specialization;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSpecialization() {
            return specialization;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientDoctorSystemGUI(1).setVisible(true);
        });
    }
}
