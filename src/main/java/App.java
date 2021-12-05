import server.Server;

/**
 * Starts the server and hosts the REST API wrapper.
 */
public class App {

    public static void main(String[] args) {
        Server app = new Server();
        app.startServer();
    }
}
