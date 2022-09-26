package rateLimiter;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rateLimiter.cache.CacheHandler;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TokenRateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRateManager.class);

    private Policy policy;

    public TokenRateManager(CacheHandler cacheHandler, ContainerRequestContext containerRequestContext, String token) {

        try {
            policy = (Policy) cacheHandler.getCache().get(token);

            TokenBucket tokenBucket;
            if (policy.getTokenBucket() == null) {
                policy.setTokenBucket(new TokenBucket(policy.getRate(), LocalDateTime.now()));
                cacheHandler.getCache().put(token, policy);
                LOGGER.debug("TokenBucket created for : "+ token);
            }
            if (policy.getTokenBucket().getBucket()!= policy.getRate() && policy.getTokenBucket().getTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) >   policy.getPerSec()) {
                policy.setTokenBucket(new TokenBucket(policy.getRate(), LocalDateTime.now()));
                cacheHandler.getCache().put(token, policy);
            }
                switch (policy.getTokenBucket().getBucket()) {
                case 0:
                    if (policy.getTokenBucket().getTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) < policy.getPerSec()) {
                        LOGGER.warn("rate limit exceeded for token : "+ token);
                        containerRequestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                                .entity(new JSONObject().put("error_code","429").put("error_message","Rate limit exceeded. Please wait before trying again.").toString()).type(MediaType.APPLICATION_JSON).build());
                    }
                    else {
                        tokenBucket = new TokenBucket(policy.getRate() -1, LocalDateTime.now());
                        policy.setTokenBucket(tokenBucket);
                        cacheHandler.getCache().put(token, policy);
                    }
                    break;
                default:
                    policy.getTokenBucket().setBucket(policy.getTokenBucket().getBucket() - 1);
                    cacheHandler.getCache().put(token, policy);
                    LOGGER.debug("token bucket : "+ ((Policy) cacheHandler.getCache().get(token)).getTokenBucket().getBucket());
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
