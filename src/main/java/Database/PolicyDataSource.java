package Database;

import java.io.FileInputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class  PolicyDataSource {

    private static MysqlDataSource ds;


    private PolicyDataSource() {

    }
    public static MysqlDataSource getMysqlDataSource(){
        Properties props = new Properties();
        String fileName = "C:/Users/ADAM/project/demo1/src/main/resources/datasource.properties";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
            System.out.println(props.getProperty("mysql.url"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ds = new MysqlDataSource();
        ds.setURL(props.getProperty("mysql.url"));
        ds.setUser(props.getProperty("mysql.username"));
        ds.setPassword(props.getProperty("mysql.password"));
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}