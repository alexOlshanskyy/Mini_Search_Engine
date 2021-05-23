package Utils;

public class StringCleaner {

    private static final String[] PUNCTUATION = new String[]{"\\.", "\\,", "\\:", "\\;", "\\!", "\\?", "\\[", "\\]", "\"", "\\'", "\\{", "\\}"};

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
