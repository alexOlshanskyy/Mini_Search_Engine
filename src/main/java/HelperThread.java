
public class HelperThread implements Runnable{
    /**
     * Helper method to run the indexing in the separate thread
     */
    public void run() {
        System.out.println("Running Indexing");
        Indexer.index();
    }

}

