package ie.tcd.mcardleg;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.*;

import java.io.IOException;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws IOException {

        ArrayList<Analyzer> analyzers = new ArrayList<Analyzer>();
        analyzers.add(new StandardAnalyzer());
        analyzers.add(new SimpleAnalyzer());
        analyzers.add(new EnglishAnalyzer());

        ArrayList<Similarity> similarities = new ArrayList<Similarity>();
        similarities.add(new ClassicSimilarity());
        similarities.add(new BooleanSimilarity());

        similarities.add(new BM25Similarity());
        similarities.add((new BM25Similarity(0.6f, 0.75f)));
        similarities.add((new BM25Similarity(1.2f, 0.5f)));

        for(Analyzer analyzer: analyzers) {
            for(Similarity similarity: similarities) {

                Config config = new Config(analyzer, similarity);
                new Indexer(config);
                Querier querier = new Querier(config);
                try {
                    querier.runQueries();
                } catch(Exception e) {
                    System.err.println(e);
                }

                config.shutdown();
            }
        }
    }
}
