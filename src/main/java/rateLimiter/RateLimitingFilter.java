package rateLimiter;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cache.CacheHandler;

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
            String uri = containerRequestContext.getUriInfo().getBaseUri().toString();
            LOGGER.info("Request from token : "+ token);

            cacheHandler.fillCache(token, uri);
            LOGGER.info(uri);
            if (cacheHandler.getApiRateCache().get(uri) != null){
                LOGGER.info("Existing policy for "+ uri);
                ApiRateManager apiRateManager = new ApiRateManager(cacheHandler, containerRequestContext, uri);
            }
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
