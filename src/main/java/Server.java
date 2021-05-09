

import com.google.gson.Gson;
import spark.Spark;

public class Server {

    public static void main(String[] args) {
        //CORSFilter corsFilter = new CORSFilter();
        //corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        Gson g = new Gson();
        // Get the person's name from the query string if available.
        Spark.get("/getPath/:startShortName/:endShortName", (req, res) -> {
            res.type("text/html");
            return g.toJson(null);
        });

    }

}
