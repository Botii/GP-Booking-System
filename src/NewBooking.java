package src;
import javax.swing.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NewBooking implements ActionListener {
    private JTextField dayTextField;
    private JComboBox<String> monthComboBox;
    private JTextField yearTextField;
    private JComboBox<String> timeComboBox;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton returnButton;
    private JPanel homePanel;
    private JPanel panel;
    private JPanel tablePanel;
    int pid;

    
    public NewBooking(JFrame mainFrame, JPanel homePanel, int pid){
        this.pid = pid;
        this.homePanel = homePanel;
        mainFrame.setTitle("new booking");    
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // Create panel to add all the components to
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        

        
        // Create a label and combobox for the months
        JLabel timeLabel = new JLabel("Time:");
        panel.add(timeLabel);
        String[] times = {"9am","10am","11am","12pm","1pm","2pm","3pm","4pm","5pm"};
        timeComboBox = new JComboBox<>(times);
        panel.add(timeComboBox);
        
        //create a text field for the day
        JLabel dayLabel = new JLabel("day:");
        panel.add(dayLabel);
        dayTextField = new JTextField(4);
        panel.add(dayTextField);
        
        // Create a label and combobox for the months
        JLabel monthLabel = new JLabel("Month:");
        panel.add(monthLabel);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        panel.add(monthComboBox);

        // Create label and text input for year
        JLabel yearLabel = new JLabel("Year:");
        panel.add(yearLabel);
        yearTextField = new JTextField(4);
        panel.add(yearTextField);

        // Create a submit button for the information
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                submitButtonClicked();
            }
        });
        panel.add(submitButton);

        //return home button

        returnButton = new JButton("return to home");
        returnButton.addActionListener(this);
        panel.add(returnButton);

        // Create table and scroll pane
        table = new JTable();
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        // Add everything to main frame
        mainFrame.add(panel, BorderLayout.NORTH);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(tablePanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == returnButton){
            panel.setVisible(false);
            tablePanel.setVisible(false);
            homePanel.setVisible(true);
        }
    }

    private void submitButtonClicked(){
        String monthText = (String) monthComboBox.getSelectedItem();
        String timeText = (String) timeComboBox.getSelectedItem();
        int month = 0;
        int day = 0;
        int time = 0;
        //convert string month to int month
        switch (monthText.toLowerCase()) {
            case "january":
                month = 1;
                break;
            case "february":
                month = 2;
                break;
            case "march":
                month = 3;
                break;
            case "april":
                month = 4;
                break;
            case "may":
                month = 5;
                break;
            case "june":
                month = 6;
                break;
            case "july":
                month = 7;
                break;
            case "august":
                month = 8;
                break;
            case "september":
                month = 9;
                break;
            case "october":
                month = 10;
                break;
            case "november":
                month = 11;
                break;
            case "december":
                month = 12;
                break;
            default:
                throw new IllegalArgumentException();
            }

            switch (timeText.toLowerCase()) {
                case "9am":
                    time = 9;
                    break;
                case "10am":
                    time = 10;
                    break;
                case "11am":
                    time = 11;
                    break;
                case "12am":
                    time = 12;
                    break;
                case "1pm":
                    time = 1;
                    break;
                case "2pm":
                    time = 2;
                    break;
                case "3pm":
                    time = 3;
                    break;
                case "4pm":
                    time = 4;
                    break;
                case "5pm":
                    time = 5;
                    break;

                default:
                    throw new IllegalArgumentException();
                }    
        String yearText = yearTextField.getText();
        String dayText = dayTextField.getText();
        int year = 0;
        //checks for invalid input
        try {
            year = Integer.parseInt(yearText);
            day = Integer.parseInt(dayText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid year or day.");
            return;    
        }
        try {
            // Establish a database connection
            DBManager sqlconnect = new DBManager();
            sqlconnect.openConnection();

            //get date value
            String strdate = String.format("%d-%02d-%02d", year, month, day);
            Date date = Date.valueOf(strdate);
            
            //get time value
            String strtime = String.format("%02d:00:00", time);
            Time sqltime = Time.valueOf(strtime);

            //check timeslot has not been booked yet
            PreparedStatement checkAppointment = sqlconnect.getConnection().prepareStatement("SELECT * FROM Appointments WHERE date = ? && time = ?");

            checkAppointment.setDate(1, date);
            checkAppointment.setTime(2, sqltime);
            ResultSet resultSet = checkAppointment.executeQuery();
            System.out.println("here");
            // If there is already a booking for this timeslot, break and return
            if (resultSet.next()) {
            JOptionPane.showMessageDialog(panel, "This timeslot is already booked.", "Booking Conflict", JOptionPane.ERROR_MESSAGE);
            return;
            }
            System.out.println(pid);
            PreparedStatement getDoctor = sqlconnect.getConnection().prepareStatement("SELECT Doctor_ID FROM Appointments WHERE Patient_Id = ?");
            getDoctor.setInt(1, pid);
            ResultSet getDoc = getDoctor.executeQuery();
            int did = 0;
            if(getDoc.next()){
                did = getDoc.getInt("Doctor_Id");
            }
            // Create a PreparedStatement for executing the SQL query
            PreparedStatement preparedStatement = sqlconnect.getConnection().prepareStatement("INSERT INTO appointments(Patient_Id,Doctor_Id,date,time) VALUES (?,?,?,?)");

            // Set parameter values
            preparedStatement.setInt(1, pid);
            preparedStatement.setInt(2, did);
            preparedStatement.setDate(3, date);
            preparedStatement.setTime(4, sqltime);

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("test");
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(panel, "Your appointment has been booked.", "Your Booking", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Insertion failed");
            }
            preparedStatement.close();
            sqlconnect.closeConnection();
        
            

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
