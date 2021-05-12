import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
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
            for (String s : map.keySet()){
                //System.out.println("Key: " + s);
                //System.out.println("Value: " + map.get(s));
            }
           // System.out.println(map.keySet().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String crawlLinkURL(String link) {
        String result = "";
        try {
            Document page = Jsoup.connect(link).get();
            result = page.body().text();
        } catch (IOException e) {
             // could happen
        } finally {
            return result;
        }
    }


}
