package ie.tcd.mcardleg;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class IndexConfig {

    private static String INDEX_DIRECTORY = "../index";

    private Analyzer analyzer;
    private Directory directory;

    private IndexWriterConfig config;

    public IndexConfig(String[] args) throws IOException {
        switch(args[0]) {
            case "standard": this.analyzer = new StandardAnalyzer();
            default: this.analyzer = new StandardAnalyzer();
        }
        this.directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        this.config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    }

    public IndexWriterConfig getConfig() {
        return config;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void shutdown() throws IOException {
        directory.close();
    }
}
