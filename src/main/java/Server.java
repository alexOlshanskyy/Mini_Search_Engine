

import Utils.CORSFilter;
import com.google.gson.Gson;
import spark.Spark;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private static final Integer REINDEX_TIMER = 30000;


    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your database code here
                System.out.println("Reindexing");
                Thread t = new Thread(new HelperThread());
                t.start();
            }
        }, 0, REINDEX_TIMER);

        //Indexer.index();
        SearchEngine sE = new SearchEngine();
        //String res1 = sE.search("Up-to-dAte oFficiAl transcript submitted as an electronic PDF");
        //System.out.println(res1);
        Gson g = new Gson();
        // Get the person's name from the query string if available.
        Spark.get("/search/:query", (req, res) -> {
            System.out.println("Got request: " + req.params(":query"));
            res.type("text/html");
            if (Indexer.isReady()) {
                ArrayList<SearchEngine.SearchResult> result = sE.search(req.params(":query"));
                res.status(200);
                return g.toJson(result);
            } else {
                res.status(200);
                return g.toJson(null);
            }


        });

    }

}
