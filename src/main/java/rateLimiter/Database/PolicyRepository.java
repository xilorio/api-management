package rateLimiter.Database;

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
            connection.close();
            return pol;
        }
        return null;
    }


}
