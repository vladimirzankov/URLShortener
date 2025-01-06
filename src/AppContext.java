class AppContext {
    private final LinkManager linkManager;
    private final UserManager userManager;

    public AppContext() {
        Config config = new Config();
        this.linkManager = new LinkManager(config);
        this.userManager = new UserManager();
    }

    public LinkManager getLinkManager() { return linkManager; }
    public UserManager getUserManager() { return userManager; }
}