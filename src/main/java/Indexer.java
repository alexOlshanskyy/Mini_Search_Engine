import java.util.*;

public class Indexer {

    public static IndexData indexData = new IndexData();
    public static boolean isReady = false;

    public static void main(String[] args)
    {
        index();
    }

    public static boolean isReady() {
        return isReady;
    }

    public static IndexData getIndexData() {
        return indexData;
    }


    public static void index() {
        HashMap<String, HashSet<String>> wordToListOfURLsLocal = new HashMap<>();
        HashMap<String, HashMap<String,Integer>> urlToWordCountLocal = new HashMap<>();
        HashMap<String, String> urlToPlainLocal = new HashMap<>();
        ArrayList<String> domains = FileParser.getDomains();
        for (String domain : domains) {
            System.out.println(domain);
            HashMap<String, String> map = WebCrawler.crawlURL(domain);
            for (String url: map.keySet()) {
                urlToPlainLocal.put(url, StringCleaner.cleanUpText(map.get(url)).toLowerCase());
                HashMap<String, Integer> wordCount = parseWords(map.get(url));
                urlToWordCountLocal.put(url, wordCount);
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

        indexData.setWordToListOfURLs(wordToListOfURLsLocal);
        indexData.setUrlToWordCount(urlToWordCountLocal);
        indexData.setUrlToPlain(urlToPlainLocal);
        isReady = true;
    }

    private static HashMap<String, Integer> parseWords(String words) {
        HashMap<String, Integer> map = new HashMap<>();
        //System.out.println("This is words before:" + words);
        words = StringCleaner.cleanUpText(words);

        String[] w = words.split(" ");
        //System.out.println("This is words after:" + words);
        //System.out.println(Arrays.toString(w));
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
        private  HashMap<String, HashSet<String>> wordToListOfURLs = new HashMap<>();
        private  HashMap<String, HashMap<String,Integer>> urlToWordCount = new HashMap<>();
        private  HashMap<String, String> urlToPlain = new HashMap<>();

        public void setUrlToPlain(HashMap<String, String> urlToPlain) {
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

        public  void setWordToListOfURLs(HashMap<String, HashSet<String>> wordToListOfURLs) {
            this.wordToListOfURLs = wordToListOfURLs;
        }

        public  void setUrlToWordCount(HashMap<String, HashMap<String, Integer>> urlToWordCount) {
            this.urlToWordCount = urlToWordCount;
        }
    }
}
