package io.github.bluething.java.logging.slf4jlog4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("App start");
        LOGGER.error("Error mode");
        LOGGER.warn("Warn mode");
        LOGGER.info("Info mode");
        LOGGER.debug("Debugging mode");
        LOGGER.debug("Trace mode");

        int counter = 1;
        LOGGER.debug("The counter {}", ++counter);
        LOGGER.info("The counter {}", counter);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("The counter {}", ++counter);
        }
        LOGGER.info("The counter {}", counter);
    }

}
