# Lucene Search Engine 
Lucene search engine assignment for Information Retrieval and Web Search module 
(CS7IS3) at Trinity College Dublin.

## Application Description
Application which parses the Cran dataset, writes it into a new index and parses 
the Cran queries and uses them to query the index. It writes the query ID, retrieved 
document ID and the match score to a file in the "results" directory.

For loops in App.java make the program run multiple times with different combinations 
of analyzers and similarities. Each combination outputs a separate results file.

## To run:
When in the "App" directory, build with "mvn package" and run with "java -jar target/SearchEngine-1.0.jar".

## Assignment extras:
TREC_eval was run with each result file, and the results were discussed in my report.