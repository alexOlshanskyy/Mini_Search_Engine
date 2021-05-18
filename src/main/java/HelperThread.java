
public class HelperThread implements Runnable{
    public void run() {
        System.out.println("Running Indexing");
        Indexer.index();
    }

}

