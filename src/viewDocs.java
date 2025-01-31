package src;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewDocs implements ActionListener {

    // connection details required
    String url = "jdbc:mysql://localhost:3306/doctorsList";
    String username = "root";
    String password = "123456789";

    // frame
    JFrame frame;

    // table
    JTable table;

    // label
    JLabel label;

    //back button
    JButton backButton;

    int pid;

    viewDocs(JFrame inputPanel, int pid) {
        this.pid = pid; 

        //database driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //closing the window 
		inputPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputPanel.setLocationRelativeTo(null);
        // set the title of the window
        inputPanel.setTitle("View doctors");

        // create the new label
        label = new JLabel("All Doctors", SwingConstants.CENTER);

        //create the go back button
        backButton = new JButton("Return to home");
        //add action listener to it
        backButton.addActionListener(this);


        // the data, this will be taken from the database
        String[][] data = getDetails();

        // column names
        String[] columnName = { "Doctor Name", "Department", "Gender", "Email Address" };

        // create the table
        table = new JTable(data, columnName);

        // set the bounds
        table.setBounds(40, 50, 300, 400);
        // set new font
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        // set the opaque
        table.getTableHeader().setOpaque(false);
        // set the background colour
        table.getTableHeader().setBackground(new Color(32, 136, 203));
        // set the foreground colour
        table.getTableHeader().setForeground(new Color(255, 255, 255));
        // set the height of the row
        table.setRowHeight(35);

        // add the table to the scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // set the layout and add all the elements to the frame
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(label, BorderLayout.NORTH);
        inputPanel.add(scrollPane, BorderLayout.CENTER);
        inputPanel.add(backButton, BorderLayout.SOUTH);


        // set the size of the frame
        inputPanel.setSize(600, 250);

		
        // set the visibility
        inputPanel.setVisible(true);

    }
    
    // get the Details from the database
    public String[][] getDetails() {
        try {
            // create the connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // create the statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            // Query to get all the rows in the table
            String query = "SELECT DoctorName, Department, Gender, EmailAddress FROM doctors";

            // execute the query
            ResultSet resultSet = statement.executeQuery(query);

            // get the number of rows/results
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            // create the 2D array
            String[][] data = new String[rowCount][4];

            // add all the data into the array
            int row = 0;
            while (resultSet.next()) {
                data[row][0] = resultSet.getString("DoctorName");
                data[row][1] = resultSet.getString("Department");
                data[row][2] = resultSet.getString("Gender");
                data[row][3] = resultSet.getString("EmailAddress");
                row++;
            }

            // close the connection
            connection.close();

            // return the data
            return data;
        } catch (SQLException e) { // catch any errors
            e.printStackTrace();
            return null;
        }
    }


    //back button
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {

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
    }
}
