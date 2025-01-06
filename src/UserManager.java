import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class UserManager {
    private final Set<UUID> users = new HashSet<>();

    public UUID createUser() {
        UUID userId = UUID.randomUUID();
        users.add(userId);
        return userId;
    }

    public boolean userExists(UUID userId) {
        return users.contains(userId);
    }
}