package ie.tcd.mcardleg;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        IndexConfig indexConfig = new IndexConfig(args);

        Indexer indexer = new Indexer(indexConfig);
        indexer.buildIndex();

        Querier querier = new Querier(indexConfig);
        try {
            querier.runQueries();
        } catch(Exception e) {
            System.err.println(e);
        }

        indexConfig.shutdown();
    }
}
