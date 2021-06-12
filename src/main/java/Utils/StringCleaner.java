package Utils;

public class StringCleaner {
    // all the punctuation that will be filtered from the search query
    private static final String[] PUNCTUATION = new String[]{"\\.", "\\,", "\\:", "\\;", "\\!", "\\?", "\\[", "\\]", "\"", "\\'", "\\{", "\\}"};

    /**
     *
     * @param words is a string that has all the text from a web page
     * @return returns a cleaned up string after filtering the punctuations from above
     */
    public static String cleanUpText(String words) {
        //System.out.println("Start");
        for (String c : PUNCTUATION) {
            //System.out.println("1: " + words);
            words = words.replaceAll(c, "");
            //System.out.println("2: " + words);
        }
        return words;
    }
}
