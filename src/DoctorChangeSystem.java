package src;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class DoctorChangeSystem extends JFrame {

    private home_page homePage;
    private JComboBox<String> doctorListComboBox;
    private JButton changeDoctorButton;
    private JButton backButton;
    private JPanel panel;
    private ArrayList<String> doctorList = new ArrayList<>();
    int pid;

    public DoctorChangeSystem(home_page homePage, int pid) {
        this.homePage = homePage;
        this.pid = pid;

        setTitle("Doctor Change System");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());

        // Populate the doctor list
        populateDoctorList();

        // Create combo box with doctor list
        doctorListComboBox = new JComboBox<>(doctorList.toArray(new String[0]));

        // Create button for changing the doctor
        changeDoctorButton = new JButton("Change Doctor");

        // Create button to go back
        backButton = new JButton("Return to home");

        // Create top panel with FlowLayout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(changeDoctorButton);
        topPanel.add(backButton);

        // Add components to the panel
        panel.add(doctorListComboBox, BorderLayout.NORTH);
        panel.add(topPanel, BorderLayout.CENTER);

        // Add action listener to the change doctor button
        changeDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDoctor = (String) doctorListComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(null, "Doctor changed to " + selectedDoctor);
            }
        });

        // Action listener for the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JFrame) SwingUtilities.getWindowAncestor(backButton)).dispose();
                JFrame homePageFrame = new JFrame();
                home_page homePage = new home_page(homePageFrame, home_page.loginForm, pid);
                homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                homePageFrame.setSize(500, 500);
                homePageFrame.setLocationRelativeTo(null);
                homePageFrame.setVisible(true);
            }
        });

        // Add the panel to the frame
        add(panel);


    }

    // Method to populate doctor list from database
    private void populateDoctorList() {
        try {

                // connection details required
            String url = "jdbc:mysql://localhost:3306/doctorsList";
            String username = "root";
            String password = "123456789";

            // create the connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // create the statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            // Execute query to retrieve doctors
            ResultSet rs = statement.executeQuery("SELECT * FROM Doctors");

            // Clear existing list
            doctorList.clear();

            // Populate doctor list
            while (rs.next()) {
                String doctorName = rs.getString("DoctorName");
                doctorList.add(doctorName);
            }

            // Close resources
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
         } 
    }
}
