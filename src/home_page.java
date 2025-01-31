package src;
import javax.swing.*;
import java.sql.*;
import java.awt.Window;
import java.awt.event.*;  

public class home_page extends JFrame implements ActionListener {
    JFrame frame;
    JButton change_doctor_button;  
    JButton logout_button;
    JButton view_prescriptions;
    JButton view_all_doctors;
    JButton new_booking;
    JButton view_booking;
    JButton reschedule_booking;


    JButton patientDoctor;
    JPanel mainPanel; 
    JButton docDetails; 

    int pid;


    // Reference to the login form
    static Login_form loginForm;
    
    public home_page(JFrame frame, Login_form loginForm, int pid) {

        this.pid = pid;
        
        // Set the title of the home page frame
        frame.setTitle("Patient Home Page"); 
        
        //Homepage panel
        mainPanel = new JPanel();
        mainPanel.setVisible(true);

        //add homepage panel to overall Frame
        frame.add(mainPanel);
        this.loginForm = loginForm;
        this.frame = frame;
    
        // Create the booking button
        change_doctor_button = new JButton("Change Doctor");
    
        // Create the logout button
        logout_button = new JButton("logout");
    
        // Create the view prescriptions button which allows the logged in user to view their prescriptions and visit details
        view_prescriptions = new JButton("View Prescriptions");
    
        // Create the view all doctors button which allows the logged in user to view all the doctors.
        view_all_doctors = new JButton("View All Doctors");
    
        // Create the view booking button which allows the logged in user to book a new appointment
        new_booking = new JButton("New Appointment");

        //create the view booking button which allwo the logged in user to view there current appoointments
        view_booking = new JButton("View Appointment");

        //create the view booking button which allwo the logged in user to rescedule their booking.
        reschedule_booking = new JButton("Reschedule Appointment");

        patientDoctor = new JButton("Register as a patient");

    
        docDetails = new JButton("view doctors details");

        // Set the bounds for each component
        change_doctor_button.setBounds(0, 10, 200, 30);
        logout_button.setBounds(0, 50, 200, 30);
        view_prescriptions.setBounds(0, 90, 200, 30);
        view_all_doctors.setBounds(0, 130, 200, 30);
        new_booking.setBounds(0, 170, 200, 30);
        view_booking.setBounds(0,170,200,30);
        reschedule_booking.setBounds(0,210,200,30);


        //add a action listener to the buttons
        change_doctor_button.addActionListener(this); 
        logout_button.addActionListener(this);
        view_prescriptions.addActionListener(this);
        view_all_doctors.addActionListener(this);
        new_booking.addActionListener(this);
        view_booking.addActionListener(this);
        reschedule_booking.addActionListener(this);
        patientDoctor.addActionListener(this);


        // Add components to the main panel
        mainPanel.add(change_doctor_button);
        mainPanel.add(view_prescriptions);
        mainPanel.add(view_all_doctors);
        mainPanel.add(new_booking);
        mainPanel.add(view_booking);
        mainPanel.add(logout_button);
        mainPanel.add(reschedule_booking);
        mainPanel.add(docDetails);
        mainPanel.add(patientDoctor);
    }

    public static void main(String[] args) {

        // Create a new JFrame
        JFrame frame = new JFrame();
        // Set JFrame properties
        home_page homepage = new home_page(frame, loginForm,250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500); // Set size as needed
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
    String url = "jdbc:mysql://localhost:3306/doctorsList";
    String username = "root";
    String password = "123456789";

    // Get the current timestamp
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    // Prepare the SQL statement for inserting log
    String insertLogQuery = "INSERT INTO login_logs (pid, functionality, timestamp) VALUES (?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(url, username, password);
         PreparedStatement insertLogStatement = connection.prepareStatement(insertLogQuery)) {

        // Set parameters for the prepared statement
        insertLogStatement.setInt(1, pid);

        // Handle button clicks
        if (ae.getSource() == change_doctor_button) {
            insertLogStatement.setString(2, "Change Doctor");
            // Handle booking button action
            // Open the DoctorChangeSystem
            frame.dispose();
            // Open the DoctorChangeSystem passing the reference to home_page frame
            DoctorChangeSystem doctorChangeSystem = new DoctorChangeSystem(this, pid);
            doctorChangeSystem.setVisible(true);

        } else if (ae.getSource() == logout_button) {
            insertLogStatement.setString(2, "Logout");
            // Handle logout button action
            // Dispose of the current home page window
            frame.dispose();

        } else if (ae.getSource() == view_prescriptions) {
            insertLogStatement.setString(2, "View Prescriptions");
            // Open ViewPrescription passing the reference to home_page frame
            ViewPrescription viewPrescription = new ViewPrescription();
            viewPrescription.fetchAndDisplayPrescriptions(pid, frame);


        } else if (ae.getSource() == view_all_doctors) {
            insertLogStatement.setString(2, "View All Doctors");
             // Create and show instance of viewDocs
             mainPanel.setVisible(false);
             viewDocs viewdocs = new viewDocs(frame, pid);

        } else if (ae.getSource() == new_booking) {
            insertLogStatement.setString(2, "New Appointmnet");
            //add when new booking is created
            mainPanel.setVisible(false);
            NewBooking newBooking = new NewBooking(frame,mainPanel,pid);
            


        } else if (ae.getSource() == view_booking) {
            insertLogStatement.setString(2, "View Appointment");
            //sets homepage panel invis and sets viewbooking page to visable
            mainPanel.setVisible(false);
            ViewBookings bookings = new ViewBookings(frame,mainPanel, pid);

        } else if (ae.getSource() == reschedule_booking) {
            insertLogStatement.setString(2, "Reschedule Appointment");
            // Close the current JFrame
            frame.dispose();

            // Create and show the ChangeAppointment GUI
            ChangeAppointment changeAppointment = new ChangeAppointment(pid);

            changeAppointment.setVisible(true);

        } else if (ae.getSource() == patientDoctor) {
            insertLogStatement.setString(2, "Register as a patient");
            // Close the current JFrame
            frame.dispose();

            // Create and show the ChangeAppointment GUI
            PatientDoctorSystemGUI patientDoctor = new PatientDoctorSystemGUI(pid);

            patientDoctor.setVisible(true);

        }else if(ae.getSource() == docDetails){
            insertLogStatement.setString(2, "view doctors details");
            frame.dispose();
            DoctorsDetails doctorDetails = new DoctorsDetails();
            doctorDetails.setVisible(true);

        }

        // Set the timestamp parameter
        insertLogStatement.setTimestamp(3, timestamp);

        // Execute the insert statement
        insertLogStatement.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    }
}