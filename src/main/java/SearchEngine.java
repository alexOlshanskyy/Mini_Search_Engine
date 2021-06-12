import Utils.StringCleaner;

import java.util.*;

public class SearchEngine {

    public static final Integer SCALE_CONST = 100;

    /**
     *
     * @param query is a string that will be used to search
     * @return Returns a list of SearchResults or null if Indexer is not ready or if something goes wrong
     */
    public ArrayList<SearchResult> search (String query) {
        if (!Indexer.isReady()) {
            return null;
        }
        String[] words = getWords(query);
        ArrayList<SearchResult> searchResults = scoreResults(words, cleanQuery(query));
        Collections.sort(searchResults);
        return searchResults;

    }

    /**
     *
     * @param query is the search request from the user
     * @return Returns an array of words from the query
     */
    private String[] getWords(String query) {
        query = query.toLowerCase();
        query = StringCleaner.cleanUpText(query);
        return query.split(" ");
    }

    /**
     *
     * @param query is the search request from the user
     * @return Returns a cleaned up string based on StringCleaner object. Also turns everything to lowercase.
     */
    private String cleanQuery(String query) {
        query = StringCleaner.cleanUpText(query);
        query = query.toLowerCase();
        return query;
    }

    /**
     * This score functions scores the url search results by ranking them on how relevant they are to the search query
     * @param words all the words from the query
     * @param query users request query
     * @return Returns a list of SearchResults
     */
    private ArrayList<SearchResult> scoreResults (String[] words, String query) {
        Indexer.IndexData indexData = Indexer.getIndexData();
        HashMap<String, SearchResult> result = new HashMap<>();
        Set<String> websites = indexData.getUrlToWordCount().keySet();
        boolean wordMissingForWebsite = false;
        for (String website : websites) {
            for (String word : words) {
                if (word.equals("")) {
                    continue;  // skip empty spaces
                }
                if (indexData.getWordToListOfURLs().containsKey(word)) {
                    if (indexData.getWordToListOfURLs().get(word).contains(website)) {
                        if (!result.containsKey(website)) {
                            result.put(website, new SearchResult(website, indexData.getUrlToWordCount().get(website).get(word)));
                        } else {
                            result.put(website,
                                    new SearchResult(website,
                                            result.get(website).score + indexData.getUrlToWordCount().get(website).get(word)));
                        }
                    } else {
                        wordMissingForWebsite = true;
                        break;
                    }
                } else {
                    return new ArrayList<>();
                }
            }

            if (wordMissingForWebsite) {
                result.remove(website);
                wordMissingForWebsite = false;
            }
        }

        for (String website : websites) {
            // the reason we need result.containsKey(website) is because say the word you are looking for is friend.
            // Than friends will match, which  is not quite what we are looking for.
            if (indexData.getUrlToPlain().get(website).contains(query) && result.containsKey(website)) {
                result.put(website,
                        new SearchResult(website,
                                result.get(website).score + SCALE_CONST));
            }
        }
        return new ArrayList<>(result.values());
    }


    public static class  SearchResult implements Comparable<SearchResult>{
        /*
            This object contains the data for a search result. In includes teh link (url) and the score
            (how relevant this result is)
         */
        private final String link;
        private final int score;

        @Override
        public String toString() {
            return link + ' ' +
                    " score={" + score +
                    '}';
        }

        public SearchResult(String link, int score) {
            this.link = link;
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SearchResult that = (SearchResult) o;
            return Objects.equals(link, that.link);
        }

        @Override
        public int hashCode() {
            return Objects.hash(link);
        }

        @Override
        public int compareTo(SearchResult o) {
            return o.score - this.score;
        }
    }



}
