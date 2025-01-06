import java.util.Scanner;
import java.util.UUID;

class ConsoleUI {
    private final AppContext context;
    private final Scanner scanner;
    private UUID currentUserId;;

    public ConsoleUI(AppContext context) {
        this.context = context;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Type 'help' for commands\n");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toLowerCase();

            try {
                running = processCommand(command, parts);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private boolean processCommand(String command, String[] args) {
        switch (command) {
            case "help":
                showCommands();
                return true;
            case "signup":
                createNewAccount();
                return true;
            case "signin":
                signIn(args);
                return true;
            case "create":
                createNewLink(args);
                return true;
            case "open":
                visitLink(args);
                return true;
            case "limit":
                updateVisitLimit(args);
                return true;
            case "expire":
                updateExpiration(args);
                return true;
            case "delete":
                removeLink(args);
                return true;
            case "clean":
                deleteExpiredLinks();
                return true;
            case "user":
                showCurrentUser();
                return true;
            case "quit":
                return false;
            default:
                System.out.println("Unknown command");
                return true;
        }
    }

    private void showCommands() {
        System.out.println("\nCommands:");
        System.out.println("  signup           Create account");
        System.out.println("  signin <id>      Sign in");
        System.out.println("  user             Show current user");
        System.out.println("  create <url> <visits> <secs>  Create link");
        System.out.println("  open <url>       Open link");
        System.out.println("  limit <url> <n>  Update visit limit");
        System.out.println("  delete <url>     Delete link");
        System.out.println("  clean            Remove expired");
        System.out.println("  quit             Exit");
    }

    private void createNewAccount() {
        UUID newId = context.getUserManager().createUser();
        System.out.println("Created: " + newId);
    }

    private void signIn(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: signin <id>");
            return;
        }
        try {
            UUID userId = UUID.fromString(args[1]);
            if (context.getUserManager().userExists(userId)) {
                currentUserId = userId;
                System.out.println("Signed in");
            } else {
                System.out.println("User not found");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID");
        }
    }

    private void createNewLink(String[] args) {
        if (currentUserId == null) {
            System.out.println("Sign in first");
            return;
        }
        if (args.length < 4) {
            System.out.println("Usage: create <url> <visits> <secs>");
            return;
        }
        try {
            String url = args[1];
            int visits = Integer.parseInt(args[2]);
            int seconds = Integer.parseInt(args[3]);

            Link link = context.getLinkManager().createLink(currentUserId, url, visits, seconds);
            System.out.println("Created: " + link.getShortUrl());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void visitLink(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: open <url>");
            return;
        }
        context.getLinkManager().openLink(args[1]);
    }

    private void updateVisitLimit(String[] args) {
        if (currentUserId == null) {
            System.out.println("Sign in first");
            return;
        }
        if (args.length < 3) {
            System.out.println("Usage: limit <url> <visits>");
            return;
        }
        try {
            context.getLinkManager().editLimit(currentUserId, args[1], Integer.parseInt(args[2]));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void updateExpiration(String[] args) {
        if (currentUserId == null) {
            System.out.println("Sign in first");
            return;
        }
        if (args.length < 3) {
            System.out.println("Usage: expire <url> <seconds>");
            return;
        }
        try {
            String url = args[1];
            int seconds = Integer.parseInt(args[2]);
            context.getLinkManager().updateExpiration(currentUserId, url, seconds);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void removeLink(String[] args) {
        if (currentUserId == null) {
            System.out.println("Sign in first");
            return;
        }
        if (args.length < 2) {
            System.out.println("Usage: delete <url>");
            return;
        }
        context.getLinkManager().deleteLink(currentUserId, args[1]);
    }

    private void deleteExpiredLinks() {
        context.getLinkManager().removeExpiredLinks();
    }

    private void showCurrentUser() {
        System.out.println(currentUserId == null ? "Not signed in" : "User: " + currentUserId);
    }
}