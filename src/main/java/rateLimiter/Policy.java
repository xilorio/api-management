package rateLimiter;

public class Policy {

    private String title;
    private int rate;
    private int perSec;

    private TokenBucket tokenBucket;



    public Policy(String title, int rate, int perSec) {
        this.title = title;
        this.rate = rate;
        this.perSec = perSec;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getPerSec() {
        return perSec;
    }

    public void setPerSec(int perSec) {
        this.perSec = perSec;
    }

    public TokenBucket getTokenBucket() {
        return tokenBucket;
    }

    public void setTokenBucket(TokenBucket tokenBucket) {
        this.tokenBucket = tokenBucket;
    }
}
