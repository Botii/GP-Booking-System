package src;
import javax.swing.*;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
  

public class ViewBookings implements ActionListener{
    private JComboBox<String> monthComboBox;
    private JTextField yearTextField;
    private Statement statement;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton returnButton;
    private JPanel homePanel;
    private JPanel panel;
    private JPanel tablePanel;
    int pid;

    
    public ViewBookings(JFrame inputPanel, JPanel homePanel, int pid)  {
        this.pid = pid;
        this.homePanel = homePanel;
        inputPanel.setTitle("view booking");    
        inputPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panel to add all the components to
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        
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
        inputPanel.add(panel, BorderLayout.NORTH);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        inputPanel.add(tablePanel, BorderLayout.CENTER);
        inputPanel.pack();
        inputPanel.setVisible(true);
        inputPanel.setLocationRelativeTo(null);
        
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
        int month = 0;
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
        String yearText = yearTextField.getText();
        int year = 0;
        //checks for invalid input
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid year.");
            return;    
        }
       
        try {
            // Establish a database connection
            DBManager sqlconnect = new DBManager();
            sqlconnect.openConnection();
            // Create a PreparedStatement for executing the SQL query
            PreparedStatement preparedStatement = sqlconnect.getConnection().prepareStatement("SELECT Patient_ID, Date, Time FROM Appointments WHERE MONTH(Date) = ? AND YEAR(Date) = ?");
            // Set parameter values
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year)
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
        
            // Populate table model with data from ResultSet
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Patient_ID");
            model.addColumn("Date");
            model.addColumn("Time");

            while (resultSet.next()) {
                Object[] rowData = new Object[3];
                for (int i = 0; i < 3; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData);
            }
            table.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Unable to establish database connection.");
            ex.printStackTrace();
        }

    }
}



