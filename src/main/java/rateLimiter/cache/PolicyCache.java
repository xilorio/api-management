package rateLimiter.cache;

import rateLimiter.Policy;

import java.util.HashMap;

public class PolicyCache {
    private static PolicyCache instance;
    private static HashMap<String, Policy> cache;

    private PolicyCache() {
        cache = new HashMap<String, Policy>();
    }

    public static PolicyCache getInstance() {
        if(instance == null){
            instance = new PolicyCache();
        }
        return instance;
    }

    public static HashMap<String, Policy> getCache() {
        return cache;
    }

    public static void setCache(HashMap<String, Policy> cache) {
        PolicyCache.cache = cache;
    }


}
