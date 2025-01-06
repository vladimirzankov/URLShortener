import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

class Link {
    private final String shortUrl;
    private final String originalUrl;
    private final UUID userId;
    private int visitsLimit;
    private int visitsCount;
    private final LocalDateTime creationTime;
    private Duration lifetime;
    private boolean active;

    public Link(String shortUrl, String originalUrl, UUID userId, int visitsLimit, Duration lifetime) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.userId = userId;
        this.visitsLimit = visitsLimit;
        this.lifetime = lifetime;
        this.visitsCount = 0;
        this.creationTime = LocalDateTime.now();
        this.active = true;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getVisitsLimit() {
        return visitsLimit;

    }
    public void setVisitsLimit(int limit) {
        this.visitsLimit = limit;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(creationTime.plus(lifetime));
    }

    public boolean isLimitReached() {
        return visitsCount >= visitsLimit;
    }

    public void incrementVisitsCount() {
        visitsCount++;
    }

    public void setLifetime(Duration lifetime) {
        this.lifetime = lifetime;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}