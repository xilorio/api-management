package cache;

import Database.PolicyRepository;
import rateLimiter.Policy;

import java.sql.SQLException;
import java.util.HashMap;

public class CacheHandler {
    private HashMap<String , Policy> rateCache;
    private HashMap<String , Boolean> expiryCache;
    private HashMap<String , Policy> apiRateCache;

    private PolicyRepository policyRepository;

    public CacheHandler() throws SQLException, ClassNotFoundException {
        PolicyCache policyCache = PolicyCache.getInstance();
        rateCache = PolicyCache.getCache();
        expiryCache = PolicyCache.getExpiryCache();
        apiRateCache = PolicyCache.getApiRateCache();
        policyRepository = new PolicyRepository();
    }

    public void fillCache(String token,String uri) throws SQLException {
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
        if(apiRateCache.get(uri) == null){
            policy = policyRepository.getByUri(uri);
            if(policy != null) {
                apiRateCache.put(uri, policy);
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

    public HashMap<String, Policy> getApiRateCache() {
        return apiRateCache;
    }

    public void setApiRateCache(HashMap<String, Policy> apiRateCache) {
        this.apiRateCache = apiRateCache;
    }
}
