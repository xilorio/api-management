package stopList;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import rateLimiter.Policy;
import rateLimiter.TokenBucket;
import rateLimiter.cache.CacheHandler;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TokenExpiryManager {
    private Policy policy;
    private CacheHandler cacheHandler;
    public TokenExpiryManager(ContainerRequestContext containerRequestContext) {
    }
}
