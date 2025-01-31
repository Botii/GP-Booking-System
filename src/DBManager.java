package src;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	public static void main(String[] args) {
		DBManager db = new DBManager();		
		db.testConnection();
	}
	
	public void testConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CHANGE USER AND PASSWORD TO THE NAME OF YOUR LOCAL DATABASE
            connection = DriverManager.getConnection("jdbc:mysql://localhost/doctorsList?user=root&password=123456789");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from patient");
			while (resultSet.next())
				System.out.println(resultSet.getString("root") + " - " + resultSet.getString("password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public boolean openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // CHANGE USER AND PASSWORD TO THE NAME OF YOUR LOCAL DATABASE
            connection = DriverManager.getConnection("jdbc:mysql://localhost/doctorsList?user=root&password=123456789");
            return true; // Connection successful
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false; // Connection failed
        }
    }
    //closed connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	//returns every entry in database for testing
	public List<String> getAllEntries() {
    List<String> allDataList = new ArrayList<>();
    try {
        // Open the database connection
        openConnection();

        // Fetch data from the database
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM patients");

        while (resultSet.next()) {
            String username = resultSet.getString("Username");
            String password = resultSet.getString("Password");
            String entry = username + " - " + password;
            allDataList.add(entry);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return allDataList;
	}
    public Connection getConnection(){
        return connection;
    }
}