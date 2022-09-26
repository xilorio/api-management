package rateLimiter;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rateLimiter.cache.CacheHandler;

import java.io.IOException;
import java.sql.SQLException;

public class RateLimitingFilter implements ContainerRequestFilter{
    private static Logger LOGGER = LoggerFactory.getLogger(RateLimitingFilter.class);
    private CacheHandler cacheHandler;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        try {
            cacheHandler = new CacheHandler();
            LOGGER.debug("cache initialized");

            String token = containerRequestContext.getHeaderString("X-Authorization");
            LOGGER.info("Request from token : "+ token);

            cacheHandler.fillCache(token);
            if (cacheHandler.getCache().get(token) != null){
                LOGGER.info("Existing policy for "+ token);
                TokenRateManager rateManager = new TokenRateManager(cacheHandler, containerRequestContext, token);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }



}
