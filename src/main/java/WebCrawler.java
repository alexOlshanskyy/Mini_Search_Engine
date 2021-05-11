import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
    private final static String URL_PREFIX = "https://www.";

    public static void main(String[] args)
    {
        crawlURL("cia.gov");
    }


    public static HashMap<String, String> crawlURL(String url) {
        try {
            Document page = Jsoup.connect(URL_PREFIX + url).get();
            Elements links = page.select("a[href]");
            for (Element e : links) {
                String path = e.attr("href");
                // only do internal links
                if (path.charAt(0) == '/' && path.length() > 1) {
                    System.out.println(URL_PREFIX + url + path);
                }
            }
            String html = page.body().toString();
            System.out.println(page.body().text());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


}
