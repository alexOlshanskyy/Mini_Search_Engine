package Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Spark;

import java.util.HashMap;

public class CORSFilter {

    private final HashMap<String, String> corsHeaders = new HashMap<>();

    /**
     * All the headers needed for requests
     */
    public CORSFilter() {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers",
                "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    /**
     * Applying the headers
     */
    public void apply() {
        Filter filter = (request, response) -> corsHeaders.forEach(response::header);
        Spark.afterAfter(filter);
        Logger logger = LoggerFactory.getLogger("Backend Server");
        logger.info("Listening on: http://localhost:" + Spark.port());
    }
}