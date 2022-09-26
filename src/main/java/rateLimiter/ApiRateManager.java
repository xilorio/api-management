package rateLimiter;

import cache.CacheHandler;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ApiRateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRateManager.class);

    private Policy policy;

    public ApiRateManager(CacheHandler cacheHandler, ContainerRequestContext containerRequestContext, String uri) {

        try {
            policy = (Policy) cacheHandler.getApiRateCache().get(uri);

            TokenBucket tokenBucket;
            if (policy.getTokenBucket() == null) {
                policy.setTokenBucket(new TokenBucket(policy.getRate(), LocalDateTime.now()));
                cacheHandler.getCache().put(uri, policy);
            }
            if (policy.getTokenBucket().getBucket()!= policy.getRate() && policy.getTokenBucket().getTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) >   policy.getPerSec()) {
                policy.setTokenBucket(new TokenBucket(policy.getRate(), LocalDateTime.now()));
                cacheHandler.getApiRateCache().put(uri, policy);
            }
            switch (policy.getTokenBucket().getBucket()) {
                case 0:
                    if (policy.getTokenBucket().getTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) < policy.getPerSec()) {
                        containerRequestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                                .entity(new JSONObject().put("error_code","429").put("error_message","Rate limit exceeded. Please wait before trying again.").toString()).type(MediaType.APPLICATION_JSON).build());
                    }
                    else {
                        tokenBucket = new TokenBucket(policy.getRate() -1, LocalDateTime.now());
                        policy.setTokenBucket(tokenBucket);
                        cacheHandler.getApiRateCache().put(uri, policy);
                    }
                    break;
                default:
                    policy.getTokenBucket().setBucket(policy.getTokenBucket().getBucket() - 1);
                    cacheHandler.getApiRateCache().put(uri, policy);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
