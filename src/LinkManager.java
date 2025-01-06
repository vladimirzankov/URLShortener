import java.awt.*;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class LinkManager {
    private final Map<String, Link> links = new HashMap<>();
    private final Config config;

    public LinkManager(Config config) {
        this.config = config;
    }

    public Link createLink(UUID userId, String originalUrl, int visitsLimit, int expirationSeconds) {
        int actualExpiration = Math.min(expirationSeconds, config.getExpirationSeconds());
        int actualVisits = Math.min(visitsLimit, config.getMaxVisits());

        String path = generatePath();
        String shortUrl = config.getBaseUrl() + path;

        Link link = new Link(shortUrl, originalUrl, userId, actualVisits,
                Duration.ofSeconds(actualExpiration));
        links.put(shortUrl, link);
        return link;
    }

    private String generatePath() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void openLink(String shortUrl) {
        Link link = links.get(shortUrl);
        if (link == null) {
            System.out.println("Link not found");
            return;
        }

        if (!link.isActive() || link.isExpired() || link.isLimitReached()) {
            System.out.println("Link is not available (expired or visit limit reached)");
            link.deactivate();
            links.remove(shortUrl);
            return;
        }

        link.incrementVisitsCount();
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(link.getOriginalUrl()));
            } else {
                System.out.println("Open manually: " + link.getOriginalUrl());
            }
        } catch (Exception e) {
            System.out.println("Error opening link: " + e.getMessage());
        }

        if (link.isLimitReached()) {
            link.deactivate();
            links.remove(shortUrl);
        }
    }

    public void editLimit(UUID userId, String shortUrl, int newLimit) {
        Link link = links.get(shortUrl);
        if (link == null || !link.getUserId().equals(userId)) {
            System.out.println("Link not found or access denied");
            return;
        }
        link.setVisitsLimit(Math.max(newLimit, config.getMaxVisits()));
        System.out.println("Visit limit updated");
    }

    public void updateExpiration(UUID userId, String shortUrl, int newExpirationSeconds) {
        Link link = links.get(shortUrl);
        if (link == null) {
            System.out.println("Link not found");
            return;
        }
        if (!link.getUserId().equals(userId)) {
            System.out.println("Access denied");
            return;
        }
        int actualExpiration = Math.min(newExpirationSeconds, config.getExpirationSeconds());
        link.setLifetime(Duration.ofSeconds(actualExpiration));
        System.out.println("Expiration time updated");
    }

    public void deleteLink(UUID userId, String shortUrl) {
        Link link = links.get(shortUrl);
        if (link == null || !link.getUserId().equals(userId)) {
            System.out.println("Link not found or access denied");
            return;
        }
        links.remove(shortUrl);
        System.out.println("Link deleted");
    }

    public void removeExpiredLinks() {
        links.values().removeIf(link -> {
            if (link.isExpired()) {
                System.out.println("Removed expired: " + link.getShortUrl());
                return true;
            }
            return false;
        });
        System.out.println("Cleanup completed");
    }
}