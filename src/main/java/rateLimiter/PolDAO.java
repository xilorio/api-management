package rateLimiter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PolDAO {
    private PolDatabase db;

    public PolDAO() throws SQLException, ClassNotFoundException {
        this.db = PolDatabase.getInstance();

    }

    public Policy getByToken(String token) throws SQLException {
        Connection connection = db.getConnection();
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
