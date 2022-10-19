package ie.tcd.mcardleg;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.BufferedReader;
import java.io.IOException;

public class DocumentLoader {

    private String currentLine;
    private BufferedReader reader;
    private Document document;

    public DocumentLoader(String currentLine, BufferedReader reader) {
        this.currentLine = currentLine;
        this.reader = reader;
        this.document = new Document();
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public Document loadDocument() throws IOException {
        Boolean finishedDocument = false;

        while(!finishedDocument) {

            switch (currentLine.charAt(1)) {
                case 'I': document = stringFieldHandler(document, "index");
                case 'T': document = textFieldHandler(document, "title");
                case 'A': document = textFieldHandler(document, "author");
                case 'B': document = textFieldHandler(document, "bibliography");
                case 'W': document = textFieldHandler(document, "content");
            }

            if(currentLine == null || currentLine.charAt(1) == 'I') {
                finishedDocument = true;
            }
        }

        return document;
    }

    private Document stringFieldHandler(Document document, String field) throws IOException {
        document.add(new StringField(field, currentLine.substring(3), Field.Store.YES));
        currentLine = reader.readLine();
        return document;
    }

    private Document textFieldHandler(Document document, String field) throws IOException {
        String buffer = "";

        currentLine = reader.readLine();
        while (currentLine != null && currentLine.charAt(0) != '.') {
            buffer += currentLine + " ";
            currentLine = reader.readLine();
        }

//        if(field == "title") {
//            System.out.println("Indexing: " + buffer);
//        }

        document.add(new TextField(field, buffer, Field.Store.YES));

        return document;
    }

}
