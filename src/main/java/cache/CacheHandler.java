package cache;

import Database.PolicyRepository;
import rateLimiter.Policy;

import java.sql.SQLException;
import java.util.HashMap;

public class CacheHandler {
    private HashMap<String , Policy> rateCache;
    private HashMap<String , Boolean> expiryCache;

    private PolicyRepository policyRepository;

    public CacheHandler() throws SQLException, ClassNotFoundException {
        PolicyCache policyCache = PolicyCache.getInstance();
        rateCache = PolicyCache.getCache();
        expiryCache = PolicyCache.getExpiryCache();
        policyRepository = new PolicyRepository();
    }

    public void fillCache(String token) throws SQLException {
        Policy policy;
        Boolean isExpired;
        if(expiryCache.get(token) == null){
            isExpired = policyRepository.isTokenExpired(token);
            expiryCache.put(token,isExpired);
        }
        if(rateCache.get(token) == null){
            policy = policyRepository.getByToken(token);
            if(policy != null) {
                rateCache.put(token, policy);
            }
        }
    }

    public HashMap getCache() {
        return rateCache;
    }

    public void setCache(HashMap cache) {
        this.rateCache = cache;
    }

    public HashMap<String, Boolean> getExpiryCache() {
        return expiryCache;
    }

    public void setExpiryCache(HashMap<String, Boolean> expiryCache) {
        this.expiryCache = expiryCache;
    }
}
