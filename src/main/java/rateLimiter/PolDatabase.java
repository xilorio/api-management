package rateLimiter;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PolDatabase {

    private static PolDatabase instance;
    private static Connection connection;

    private  PolDatabase () throws ClassNotFoundException, SQLException {
        String url = String.format("jdbc:mysql://localhost:3306/management");
        String name = "root";
        String pw = "";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url,name,pw);
        System.out.println("data success");
    }

    public static PolDatabase getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null)
        {
            instance = new PolDatabase();
        }
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }
}
