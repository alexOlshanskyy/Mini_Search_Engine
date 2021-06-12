import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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

    /**
     * Crawls the url using Jsoup library. This crawler goes to a depth of 1, so it will go to all the urls it can
     * find on the given url, but it will stop there and not go to depth 2.
     * @param url the url to crawl
     * @return returns a map of all urls (original + all the sub urls it can find at depth 1) to all the text date that
     * was found on the html page.
     */
    public static HashMap<String, String> crawlURL(String url) {
        HashSet<String> linksStrings = new HashSet<>();
        HashMap<String, String> map = new HashMap<>();
        try {
            Document page = Jsoup.connect(URL_PREFIX + url).get();
            Elements links = page.select("a[href]");
            for (Element e : links) {
                String path = e.attr("href");
                // only do internal links
                if (!path.equals("") && path.charAt(0) == '/' && path.length() > 1) {
                    linksStrings.add(URL_PREFIX + url + path);
                }
            }
            for (String s : linksStrings) {
                map.put(s, crawlLinkURL(s));
            }
            map.put(URL_PREFIX + url, page.body().text());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * This is a helper to crawls the internal links of the base url.
     * @param link is the link to crawl
     * @return all the text that was found in the given link
     */
    private static String crawlLinkURL(String link) {
        String result = "";
        try {
            Document page = Jsoup.connect(link).get();
            result = page.body().text();
        } catch (IOException e) {
            // could happen if the url is outdated
            // ignore
        } catch (NullPointerException e) {
            // always return
            return result;
        }
        return result;
    }


}
