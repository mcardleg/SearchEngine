package ie.tcd.mcardleg;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Querier {

    private static int MAX_RESULTS = 1400;
    private static final String CRAN_QUERIES_DIRECTORY = "../cran/cran.qry";
    private static final String RESULTS_DIRECTORY = "../results";

    IndexConfig indexConfig;
    DirectoryReader ireader;
    IndexSearcher isearcher;

    public Querier(IndexConfig indexConfig) throws IOException {
        this.indexConfig = indexConfig;
        this.ireader = DirectoryReader.open(indexConfig.getDirectory());
        this.isearcher = new IndexSearcher(ireader);
    }

    private HashMap<String, Float> buildBooster() {
        HashMap<String, Float> boosts = new HashMap<String, Float>();
        boosts.put("title", 5f);
        return boosts;
    }

    private Query buildQuery(String queryString) throws ParseException {
        QueryParser parser = new MultiFieldQueryParser(
                new String[] {"title", "author", "bibliography", "text"},
                indexConfig.getAnalyzer(),
                buildBooster());

        return parser.parse(queryString);
    }

    private ArrayList<String> parseQueries() {
        Path cranDocumentsPath = Paths.get(CRAN_QUERIES_DIRECTORY);
        Boolean finished = false;
        ArrayList<String> queries = new ArrayList<String>();

        try(BufferedReader reader = Files.newBufferedReader(cranDocumentsPath, Charset.forName("UTF-8"))){
            String currentLine = reader.readLine();

            System.out.println("Parsing queries.");

            while (!finished) {
                QueryLoader queryLoader = new QueryLoader(currentLine, reader);
                queries.add(queryLoader.getQuestion().replace("?", ""));
                currentLine = queryLoader.getCurrentLine();

                if (currentLine == null) {
                    finished = true;
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }

        System.out.println("Finished parsing queries.");

        return queries;
    }

    private ArrayList<String> search(Integer queryIndex, String queryString) throws ParseException, IOException {
        Query query = buildQuery(queryString);

        ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;
        if (hits.length <= 0) {
            System.out.println("Failed to retrieve a document");
            return null;
        }

        ArrayList<String> results = new ArrayList<String>();
        for (ScoreDoc hit : hits) {
            Document hitDoc = isearcher.doc(hit.doc);
            results.add(queryIndex + " Q0 " + hitDoc.get("index") + " 1 " + hit.score + " STANDARD \n");
        }
        return results;
    }

    private ArrayList<String> searchAllQueries() throws IOException, ParseException {
        ArrayList<String> results = new ArrayList<String>();

        ArrayList<String> queries = parseQueries();

        System.out.println("Performing queries.");
        Integer queryIndex = 1;
        for (String queryString : queries) {
            results.addAll(search(queryIndex, queryString));
            queryIndex++;
        }
        System.out.println("Finished queries.");

        ireader.close();

        return results;
    }

    private void writeToFile(ArrayList<String> results) throws IOException {
        System.out.println("Writing results to file.");

        Directory directory = FSDirectory.open(Paths.get(RESULTS_DIRECTORY));
        String filename = "results.txt";
        File file = new File (RESULTS_DIRECTORY, filename);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for(String result: results) {
            writer.write(result);
        }

        writer.close();
        directory.close();
        System.out.println("Finished writing results.");
    }

    public void runQueries() throws IOException, ParseException {
        writeToFile(searchAllQueries());
    }

}
