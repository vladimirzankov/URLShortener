import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Config {
    private final int expirationSeconds;
    private final int maxVisits;
    private final String baseUrl;

    public Config() {
        Properties props = new Properties();
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) props.load(is);
        } catch (IOException e) {
            System.err.println("Warning: Could not load config.properties");
        }
        this.expirationSeconds = Integer.parseInt(props.getProperty("expiration_seconds", "86400"));
        this.maxVisits = Integer.parseInt(props.getProperty("max_visits", "5"));
        this.baseUrl = "http://clck.ru/";
    }

    public int getExpirationSeconds() {
        return expirationSeconds;
    }

    public int getMaxVisits() {
        return maxVisits;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}