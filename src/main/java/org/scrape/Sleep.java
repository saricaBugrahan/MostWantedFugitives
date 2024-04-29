package org.scrape;

import java.util.logging.Logger;

public class Sleep {
    private static final Logger logger = Logger.getLogger(Sleep.class.getName());
    public static void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.warning("Sleep interrupted");
        }
    }
}
