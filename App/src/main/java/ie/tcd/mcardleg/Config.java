package ie.tcd.mcardleg;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Config {

    private static String INDEX_DIRECTORY = "../index";

    private Analyzer analyzer;
    private Similarity similarity;
    private Directory directory;

    private IndexWriterConfig indexWriterConfig;

    public Config(Analyzer analyzer, Similarity similarity) throws IOException {
        this.analyzer = analyzer;
        this.similarity = similarity;
        this.directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        this.indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriterConfig.setSimilarity(similarity);
    }

    public IndexWriterConfig getIndexWriterConfig() {
        return indexWriterConfig;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public Similarity getSimilarity() {
        return similarity;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void shutdown() throws IOException {
        directory.close();
    }
}
