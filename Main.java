import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;


/**
 * Classe principale che gestisce il punto di ingresso dell'applicazione.
 * Si occupa di leggere i comandi da un file di input testuale, di processarli 
 * in modo efficiente attraverso l'Albero Rosso-Nero, 
 * e di scrivere i risultati in blocco su un file di output.
 * Misura i tempi di esecuzione per l'analisi dei benchmark prestazionali.
 */
public class Main {
    /**
     * Metodo di avvio del programma.
     *
     * @param args Array di argomenti da riga di comando:
     * args[0] = nome del file di input (opzionale, default: "input.txt")
     * args[1] = nome del file di output (opzionale, default: "output.txt")
     */
    public static void main(String[] args) {
        RedBlackBST<String, Book> catalogue = new RedBlackBST<>();
        
        String input = args.length > 0 ? args[0] : "input.txt";
        String output = args.length > 1 ? args[1] : "output.txt";

        // Costrutto try-with-resources per garantire la chiusura sicura degli stream
        try (
            BufferedReader reader = new BufferedReader(new FileReader(input));
            PrintStream outStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(output)));
        ) {
            System.setOut(outStream);
            long startTime = System.nanoTime();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                int firstSpace = line.indexOf(' ');
                String directive;
                String data = "";

                if (firstSpace == -1) {
                    directive = line;
                } else {
                    directive = line.substring(0, firstSpace);
                    data = line.substring(firstSpace).trim(); // Il resto della riga
                }

                switch (directive) {
                    case "INSERT":
                        try {
                            int lastIdx = data.lastIndexOf(' ');
                            int year = Integer.parseInt(data.substring(lastIdx + 1));
                            data = data.substring(0, lastIdx).trim();

                            lastIdx = data.lastIndexOf(' ');
                            String author = data.substring(lastIdx + 1);
                            data = data.substring(0, lastIdx).trim();

                            int firstIdx = data.indexOf(' ');
                            String isbn = data.substring(0, firstIdx);
                            String title = data.substring(firstIdx + 1).trim();

                            catalogue.put(isbn, new Book(title, author, year));
                        } catch (Exception e) {
                            System.err.println("Errore di formattazione nella riga INSERT: " + line);
                        }
                        break;

                    case "SEARCH":
                        System.out.println("(SEARCH)");
                        Book book = catalogue.get(data);
                        if (book != null) {
                            System.out.println("Libro trovato:");
                            System.out.println(book.getEntireDetails(data));
                        } else {
                            System.out.println("Libro non trovato:");
                        }
                        break;

                    case "DELETE":
                        catalogue.delete(data);
                        break;

                    case "RANGE":
                        int spaceIdx = data.indexOf(' ');
                        if (spaceIdx != -1) {
                            String startIsbn = data.substring(0, spaceIdx);
                            String endIsbn = data.substring(spaceIdx + 1).trim();
                            System.out.println("(RANGE " + startIsbn + " " + endIsbn + ")");
                            catalogue.range(startIsbn, endIsbn);
                        }
                        break;

                    case "PRINT":
                        System.out.println("PRINT");
                        catalogue.print();
                        break;
                }
            }

            long endTime = System.nanoTime();
            double executionTimeMs = (endTime - startTime) / 1_000_000.0;
            System.err.printf("Tempo di esecuzione: %.2f ms\n", executionTimeMs);
            System.err.println("Numero di nodi dell'albero: " + catalogue.size());
            System.err.println("Altezza nera dell'albero: " + catalogue.getBlackHeight());

        } catch (IOException e) {
            System.err.println("Errore I/O: " + e.getMessage());
        }
    }
}