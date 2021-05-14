import java.util.*;

public class SearchEngine {

    public static final Integer SCALE_CONST = 99999;

    public String search (String query) {
        if (!Indexer.isReady()) {
            return null;
        }
        String[] words = getWords(query);
        ArrayList<SearchResult> searchResults = scoreResults(words, cleanQuery(query));
        Collections.sort(searchResults);
        return generateString(searchResults);

    }

    private String[] getWords(String query) {
        query = query.toLowerCase();
        query = StringCleaner.cleanUpText(query);
        return query.split(" ");
    }

    private String cleanQuery(String query) {
        query = StringCleaner.cleanUpText(query);
        query = query.toLowerCase();
        return query;
    }


    private ArrayList<SearchResult> scoreResults (String[] words, String query) {
        Indexer.IndexData indexData = Indexer.getIndexData();
        HashMap<String, SearchResult> result = new HashMap<>();
        Set<String> websites = indexData.getUrlToWordCount().keySet();
        boolean wordMissingForWebsite = false;
        for (String website : websites) {
            for (String word : words) {
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
            //System.out.println(indexData.getUrlToPlain().get(website));
            //System.out.println(query);
            if (indexData.getUrlToPlain().get(website).contains(query)) {
                result.put(website,
                        new SearchResult(website,
                                result.get(website).score + SCALE_CONST));
            }
        }
        return new ArrayList<>(result.values());
    }

    private String generateString(ArrayList<SearchResult> searchResults) {
        StringBuilder sb = new StringBuilder();
        for (SearchResult sr : searchResults) {
            sb.append(sr.toString());
            sb.append("\n");
        }
        return sb.toString();
    }


    private class SearchResult implements Comparable<SearchResult>{
        private String link;
        private int score;

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

        public String getLink() {
            return link;
        }

        public int getScore() {
            return score;
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
