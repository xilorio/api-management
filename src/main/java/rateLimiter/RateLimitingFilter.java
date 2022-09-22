package rateLimiter;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RateLimitingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private Policy policy;
    private CacheHandler cacheHandler;

    public RateLimitingFilter() throws SQLException, ClassNotFoundException {

    }


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        try {
            cacheHandler = new CacheHandler();
            System.out.println(cacheHandler + "my caaaaache");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String token = containerRequestContext.getHeaderString("X-Authorization");
        containerRequestContext.getUriInfo().getBaseUri();
        System.out.println(token);
        try {
            if (cacheHandler.fillCache(token)) {
                System.out.println("cache success " + cacheHandler.getCache().get(token));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        try {
            policy = (Policy) cacheHandler.getCache().get(token);
            System.out.println("policy success " + policy.getTokenBucket());

            TokenBucket tokenBucket;
            if (policy.getTokenBucket() == null) {
                policy.setTokenBucket(new TokenBucket(policy.getRate(), LocalDateTime.now()));
                cacheHandler.getCache().put(token, policy);
                System.out.println("bucket success " + ((Policy) cacheHandler.getCache().get(token)).getTokenBucket().getTime());
            }
            switch (policy.getTokenBucket().getBucket()) {
                case 0:
                    if (policy.getTokenBucket().getTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) < policy.getPerSec()) {
                        System.out.println("rate exceeded" + policy.getTokenBucket().getTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) );
                        containerRequestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS).build());
                    }
                    else {
                        tokenBucket = new TokenBucket(policy.getRate() -1, LocalDateTime.now());
                        policy.setTokenBucket(tokenBucket);
                        cacheHandler.getCache().put(token, policy);
                    }
                    break;
                default:
                    policy.getTokenBucket().setBucket(policy.getTokenBucket().getBucket() - 1);
                    System.out.println("policy success " + policy.getTokenBucket().getBucket());
                    cacheHandler.getCache().put(token, policy);
                    break;

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {

    }


}
