package cache;

import rateLimiter.Policy;

import java.util.HashMap;

public class PolicyCache {
    private static PolicyCache instance;
    private static HashMap<String, Policy> rateCache;
    private static HashMap<String , Boolean> expiryCache;

    private PolicyCache() {
        expiryCache = new HashMap<String , Boolean>();
        rateCache = new HashMap<String, Policy>();
    }

    public static PolicyCache getInstance() {
        if(instance == null){
            instance = new PolicyCache();
        }
        return instance;
    }

    public static HashMap<String, Policy> getCache() {
        return rateCache;
    }

    public static void setCache(HashMap<String, Policy> cache) {
        PolicyCache.rateCache = cache;
    }

    public static HashMap<String , Boolean> getExpiryCache() {
        return expiryCache;
    }

    public static void setExpiryCache(HashMap<String , Boolean> expiryCache) {
        PolicyCache.expiryCache = expiryCache;
    }
}
