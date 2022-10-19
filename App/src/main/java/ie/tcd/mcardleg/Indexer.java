package ie.tcd.mcardleg;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;


import java.io.IOException;

public class Indexer {

    private static final String cranDocumentsDirectory = "../cran/cran.all.1400";

    private IndexConfig indexConfig;

    public Indexer(IndexConfig indexConfig) {
        this.indexConfig = indexConfig;
    }

    public void buildIndex() throws IOException {

//        // Create a new field type which will store term vector information
//        FieldType ft = new FieldType(TextField.TYPE_STORED);
//        ft.setTokenized(true); //done as default
//        ft.setStoreTermVectors(true);
//        ft.setStoreTermVectorPositions(true);
//        ft.setStoreTermVectorOffsets(true);
//        ft.setStoreTermVectorPayloads(true);

        IndexWriter iwriter = new IndexWriter(indexConfig.getDirectory(), indexConfig.getConfig());

        ArrayList<Document> documents = parseDocuments();
        System.out.println("Writing documents to index.");
        iwriter.addDocuments(documents);
        System.out.println("Finished writing to index.");

        iwriter.close();
    }

    private ArrayList<Document> parseDocuments() throws IOException {
        Path cranDocumentsPath = Paths.get(cranDocumentsDirectory);
        Boolean finished = false;
        ArrayList<Document> documents = new ArrayList<Document>();

        try(BufferedReader reader = Files.newBufferedReader(cranDocumentsPath, Charset.forName("UTF-8"))){
            String currentLine = reader.readLine();

            System.out.println("Parsing documents.");

            while (!finished) {
                DocumentLoader documentLoader = new DocumentLoader(currentLine, reader);
                documents.add(documentLoader.loadDocument());
                currentLine = documentLoader.getCurrentLine();

                if (currentLine == null) {
                    finished = true;
                }
            }
        }

        System.out.println("Finished parsing documents.");

        return documents;
    }
}
