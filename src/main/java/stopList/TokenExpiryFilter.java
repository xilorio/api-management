package stopList;

import cache.CacheHandler;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rateLimiter.RateLimitingFilter;
import rateLimiter.TokenRateManager;

import java.io.IOException;
import java.sql.SQLException;

@Priority(1)
public class TokenExpiryFilter implements ContainerRequestFilter {
    private static Logger LOGGER = LoggerFactory.getLogger(RateLimitingFilter.class);

    private  CacheHandler cacheHandler;

    @Override

    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        try {
            cacheHandler = new CacheHandler();
            LOGGER.debug("cache initialized");

            String token = containerRequestContext.getHeaderString("X-Authorization");
            String uri = containerRequestContext.getUriInfo().getBaseUri().toString();

            LOGGER.info("Request from token : "+ token);
            cacheHandler.fillCache(token,uri);
            if (cacheHandler.getExpiryCache().get(token)){
                LOGGER.info("token expired "+ token);
                containerRequestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                        .entity(new JSONObject().put("error_code","401").put("error_message","Unauthorized access").toString()).type(MediaType.APPLICATION_JSON).build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
