public class App {
    public static void main(String[] args) {
        AppContext context = new AppContext();
        ConsoleUI ui = new ConsoleUI(context);
        ui.start();
    }
}