package server;

import omdbwrapper.Wrapper;
import com.mashape.unirest.http.Unirest;

import java.io.IOException;

import static spark.Spark.*;

/**
 * Handles the initialization and termination of the server hosting the REST API wrapper.
 */
public class Server {

    public void startServer() {
        Wrapper wrapper = new Wrapper();
        shutdownOnExit();
        System.out.println("Server is running.");
    }

    private void shutdownOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            try {
                Unirest.shutdown();
                stop();
                System.out.println("Unirest has shutdown.");
                System.out.println("Server shutdown successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unirest shutdown error.");
            }
        }));
    }
}
