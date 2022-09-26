package Database;

import rateLimiter.Policy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PolicyRepository {
    private DataSource ds;

    public PolicyRepository() throws SQLException, ClassNotFoundException {
        this.ds = PolicyDataSource.getMysqlDataSource();
    }


    public Policy getByToken(String token) throws SQLException {
        Connection connection = ds.getConnection();
        String query = "select * from policy p, token t where p.title = t.policy and t.token = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1,token);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Policy pol =  new Policy(rs.getString("title"),rs.getInt("rate"),rs.getInt("perSec"));
            rs.close();
            ps.close();
            connection.close();
            return pol;
        }
        return null;
    }
    public Policy getByUri(String uri) throws SQLException {
        Connection connection = ds.getConnection();
        String query = "select * from policy p, api a where p.title = a.policy and a.uri = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, uri);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Policy pol =  new Policy(rs.getString("title"),rs.getInt("rate"),rs.getInt("perSec"));
            rs.close();
            ps.close();
            connection.close();
            return pol;
        }
        return null;
    }
    public boolean isTokenExpired(String token) throws SQLException {
        Connection connection = ds.getConnection();
        String query = "select expired from token t where t.token = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1,token);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            if(rs.getInt("expired") == 1){
                rs.close();
                ps.close();
                connection.close();
                return true;
            }
            rs.close();
            ps.close();
            connection.close();
            return false;
        }
        return false;
    }


}
