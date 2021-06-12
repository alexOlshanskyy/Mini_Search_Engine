

import Utils.CORSFilter;
import com.google.gson.Gson;
import spark.Spark;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private static final Integer REINDEX_TIMER = 300000;

    /**
     * This is a simple server that has one endpoint /search.
     * This server will Reindex all the data every REINDEX_TIMER.
     * @param args not used
     */
    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Reindexing");
                Thread t = new Thread(new HelperThread());
                t.start();
            }
        }, 0, REINDEX_TIMER);

        SearchEngine sE = new SearchEngine();
        Gson g = new Gson();
        // Get the person's name from the query string if available.
        Spark.get("/search/:query", (req, res) -> {
            System.out.println("Got request: " + req.params(":query"));
            res.type("text/html");
            ArrayList<SearchEngine.SearchResult> result = sE.search(req.params(":query"));
            res.status(200);
            return g.toJson(result);
        });

    }

}
