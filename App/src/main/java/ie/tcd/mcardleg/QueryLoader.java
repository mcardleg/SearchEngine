package ie.tcd.mcardleg;

import java.io.BufferedReader;

public class QueryLoader {

    private String currentLine;
    private BufferedReader reader;
    private String question;

    public QueryLoader(String currentLine, BufferedReader reader) {
        this.currentLine = currentLine;
        this.reader = reader;
        loadQuery();
    }

    private void loadQuery() {
        Boolean finishedDocument = false;

        while(!finishedDocument) {
            try {
                switch (currentLine.charAt(1)) {
                    case 'I':
                        currentLine = reader.readLine();
                    case 'W':
                        String buffer = "";
                        currentLine = reader.readLine();
                        while (currentLine != null && currentLine.charAt(0) != '.') {
                            buffer += currentLine + " ";
                            currentLine = reader.readLine();
                        }
                        question = buffer;
                }

            } catch (Exception e) {
                System.err.println(e);
            }

            if(currentLine == null || currentLine.charAt(1) == 'I') {
                finishedDocument = true;
            }
        }
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public String getQuestion() {
        return question;
    }

}
