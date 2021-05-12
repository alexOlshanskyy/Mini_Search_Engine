import java.util.*;

public class Indexer {

    private static final String[] PUNCTUATION = new String[]{"\\.", "\\,", "\\:", "\\;", "\\!", "\\?", "\\[", "\\]", "\"", "\\'", "\\{", "\\}"};

    private static HashMap<String, HashSet<String>> wordToListOfURLs = new HashMap<>();
    private static HashMap<String, HashMap<String,Integer>> urlToWordCount = new HashMap<>();

    public static void main(String[] args)
    {
        index();
        //System.out.println(wordToListOfURLs);
        System.out.println(wordToListOfURLs.keySet().size());
        for (String s : wordToListOfURLs.keySet()){

            System.out.println("Key: " + s);
            System.out.println("Value: " + wordToListOfURLs.get(s));
        }
        //System.out.println(urlToWordCount);
    }


    public static void index() {
        HashMap<String, HashSet<String>> wordToListOfURLsLocal = new HashMap<>();
        HashMap<String, HashMap<String,Integer>> urlToWordCountLocal = new HashMap<>();
        ArrayList<String> domains = FileParser.getDomains();
        for (String domain : domains) {
            System.out.println(domain);
            HashMap<String, String> map = WebCrawler.crawlURL(domain);
            for (String url: map.keySet()) {
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
        wordToListOfURLs = wordToListOfURLsLocal;
        urlToWordCount = urlToWordCountLocal;
    }

    private static HashMap<String, Integer> parseWords(String words) {
        HashMap<String, Integer> map = new HashMap<>();
        //System.out.println("This is words before:" + words);
        words = cleanUpText(words);

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

    private static String cleanUpText(String words) {
        //System.out.println("Start");
        for (String c : PUNCTUATION) {
            //System.out.println("1: " + words);
            words = words.replaceAll(c, " ");
            //System.out.println("2: " + words);
        }
        return words;
    }
}
