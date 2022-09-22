package rateLimiter;

import java.time.LocalDateTime;
import java.util.Date;

public class TokenBucket {
    private int bucket;
    private LocalDateTime time;

    public TokenBucket(int bucket, LocalDateTime time) {
        this.bucket = bucket;
        this.time = time;
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
