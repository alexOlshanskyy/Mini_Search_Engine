import Utils.FileParser;
import Utils.StringCleaner;

import java.util.*;

public class Indexer {

    public static IndexData indexData = new IndexData(null, null, null);
    private static boolean isReady = false;

    public static void main(String[] args)
    {
        index();
    }

    /**
     *
     * @return returns whether the index data is ready to be used
     */
    public static boolean isReady() {
        return isReady;
    }

    /**
     *
     * @return returns IndexData to be used for searches
     */
    public static IndexData getIndexData() {
        return indexData;
    }


    /**
     * This method indexes all the data from the urls and puts it in the IndexData object
     */
    public static void index() {
        HashMap<String, HashSet<String>> wordToListOfURLsLocal = new HashMap<>();
        HashMap<String, HashMap<String,Integer>> urlToWordCountLocal = new HashMap<>();
        HashMap<String, String> urlToPlainLocal = new HashMap<>();
        ArrayList<String> domains = FileParser.getDomains();
        // loop trough all domains
        for (String domain : domains) {
            System.out.println(domain);
            HashMap<String, String> map = WebCrawler.crawlURL(domain);
            // loop trough all urls
            for (String url: map.keySet()) {
                urlToPlainLocal.put(url, StringCleaner.cleanUpText(map.get(url)).toLowerCase());
                HashMap<String, Integer> wordCount = parseWords(map.get(url));
                urlToWordCountLocal.put(url, wordCount);
                // loop trough all the words in url
                for (String word: wordCount.keySet()) {
                    HashSet<String> urls;
                    if (wordToListOfURLsLocal.containsKey(word)) {
                        urls = wordToListOfURLsLocal.get(word);
                    } else {
                        urls = new HashSet<>();
                    }
                    urls.add(url);
                    wordToListOfURLsLocal.put(word, urls);
                }
            }
        }

        // acts like a lock
        // even if search engine see that it is true, and it turns false straight away it is fine
        // because we are sleeping for 1 second which is enough for any search to finish and no new search will start
        isReady = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // assign index data
        indexData = new IndexData(wordToListOfURLsLocal, urlToWordCountLocal, urlToPlainLocal);
        isReady = true;
    }

    /**
     *
     * @param words is a raw words string for a specific url
     * @return returns a map of words mapped to frequency of how often they appear
     */
    private static HashMap<String, Integer> parseWords(String words) {
        HashMap<String, Integer> map = new HashMap<>();
        words = StringCleaner.cleanUpText(words);

        String[] w = words.split(" ");
        for (String word : w) {
            String lowerW = word.toLowerCase();
            if (word.equals("")) {
                continue;  // skip multiple spaces in a row
            }
            if (map.containsKey(lowerW)) {
                map.put(lowerW, map.get(lowerW) + 1);
            } else {
                map.put(lowerW, 1);
            }
        }
        return map;
    }




    public static class IndexData {
        /*
            This is an IndexData object that holds all the data from the indexing of the domains, and can be used to search
            for results.
         */
        private final HashMap<String, HashSet<String>> wordToListOfURLs;
        private final HashMap<String, HashMap<String,Integer>> urlToWordCount;
        private final HashMap<String, String> urlToPlain;

        public IndexData(HashMap<String, HashSet<String>> wordToListOfURLs, HashMap<String, HashMap<String, Integer>> urlToWordCount, HashMap<String, String> urlToPlain) {
            this.wordToListOfURLs = wordToListOfURLs;
            this.urlToWordCount = urlToWordCount;
            this.urlToPlain = urlToPlain;
        }

        public HashMap<String, String> getUrlToPlain() {
            return urlToPlain;
        }

        public  HashMap<String, HashMap<String, Integer>> getUrlToWordCount() {
            return urlToWordCount;
        }

        public  HashMap<String, HashSet<String>> getWordToListOfURLs() {
            return wordToListOfURLs;
        }
    }
}
