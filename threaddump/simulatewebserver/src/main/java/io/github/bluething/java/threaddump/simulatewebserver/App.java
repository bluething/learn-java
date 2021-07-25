package io.github.bluething.java.threaddump.simulatewebserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8080);
        LOGGER.info("Server started. Listen to port 8080");

        while (true) {
            new RequestProcessor(socket).handleClient();
        }
    }
}
