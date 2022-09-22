package rateLimiter;

import java.sql.SQLException;
import java.util.HashMap;

public class CacheHandler {
    private HashMap<String ,Policy> cache;
    private PolDAO polDAO;

    public CacheHandler() throws SQLException, ClassNotFoundException {
        PolCache polCache = PolCache.getInstance();
        cache = PolCache.getCache();
        polDAO = new PolDAO();
    }

    public Boolean fillCache(String token) throws SQLException {
        Policy policy;
        if(cache.get(token) == null){
            policy = polDAO.getByToken(token);
            if(policy != null) {
                cache.put(token, policy);
                return true;
            }
        }
        return false;
    }

    public HashMap getCache() {
        return cache;
    }

    public void setCache(HashMap cache) {
        this.cache = cache;
    }
}
