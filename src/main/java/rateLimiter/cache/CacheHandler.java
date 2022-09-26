package rateLimiter.cache;

import rateLimiter.Database.PolicyRepository;
import rateLimiter.Policy;

import java.sql.SQLException;
import java.util.HashMap;

public class CacheHandler {
    private HashMap<String , Policy> cache;
    private PolicyRepository policyRepository;

    public CacheHandler() throws SQLException, ClassNotFoundException {
        PolicyCache policyCache = PolicyCache.getInstance();
        cache = PolicyCache.getCache();
        policyRepository = new PolicyRepository();
    }

    public void fillCache(String token) throws SQLException {
        Policy policy;
        if(cache.get(token) == null){
            policy = policyRepository.getByToken(token);
            if(policy != null) {
                cache.put(token, policy);
            }
        }
    }

    public HashMap getCache() {
        return cache;
    }

    public void setCache(HashMap cache) {
        this.cache = cache;
    }
}
