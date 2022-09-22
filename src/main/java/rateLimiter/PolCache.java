package rateLimiter;

import java.util.HashMap;

public class PolCache {
    private static PolCache instance;
    private static HashMap<String, Policy> cache;

    private PolCache() {
        cache = new HashMap<String, Policy>();
    }

    public static PolCache getInstance() {
        if(instance == null){
            instance = new PolCache();
        }
        return instance;
    }

    public static HashMap<String, Policy> getCache() {
        return cache;
    }

    public static void setCache(HashMap<String, Policy> cache) {
        PolCache.cache = cache;
    }


}
