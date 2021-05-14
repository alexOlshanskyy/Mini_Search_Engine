

import com.google.gson.Gson;
import spark.Spark;

public class Server {


    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().
        Indexer.index();
        SearchEngine sE = new SearchEngine();
        String res1 = sE.search("Up-to-date official transcript submitted as an electronic PDF");
        System.out.println(res1);
        Gson g = new Gson();
        // Get the person's name from the query string if available.
        Spark.get("/getPath/:query", (req, res) -> {
            res.type("text/html");
            System.out.println(req.params(":query"));
            res.status(200);
            return g.toJson(null);
        });

    }

}
