import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

public class GeneratoreTest {
    public static void main(String[] args) {
        // Numero di righe da generare (es. 100.000)
        int NUMERO_OPERAZIONI = 100000;
        String nomeFile = "test_massivo.txt";

        String[] autori = {"Sedgewick", "Hoffer", "Arnold", "Tolkien", "Asimov", "Dijkstra", "Knuth", "Turing"};
        String[] paroleTitolo = {"Algoritmi", "Database", "Java", "Programming", "Language", "Strutture", "Dati", "Avanzati", "Base", "Manuale"};
        
        Random random = new Random();

        try (PrintWriter writer = new PrintWriter(nomeFile)) {
            for (int i = 0; i < NUMERO_OPERAZIONI; i++) {
                
                // Generiamo un numero da 0 a 99 per decidere il comando
                int tipoComando = random.nextInt(100);
                
                // Genera un ISBN casuale tra 9780000 e 9789999
                String isbn = "978" + String.format("%04d", random.nextInt(10000));

                if (tipoComando < 80) { // 80% probabilità: INSERT
                    String autore = autori[random.nextInt(autori.length)];
                    int anno = 1900 + random.nextInt(127); // Anno tra 1900 e 2026
                    
                    // Titolo composto da 2 o 3 parole a caso
                    String titolo = paroleTitolo[random.nextInt(paroleTitolo.length)] + " " + 
                                    paroleTitolo[random.nextInt(paroleTitolo.length)];
                    
                    writer.println("INSERT " + isbn + " " + titolo + " " + autore + " " + anno);
                } 
                else if (tipoComando < 90) { // 10% probabilità: SEARCH
                    writer.println("SEARCH " + isbn);
                } 
                else if (tipoComando < 97) { // 7% probabilità: DELETE
                    writer.println("DELETE " + isbn);
                } 
                else if (tipoComando < 99) { // 2% probabilità: RANGE
                    String isbnEnd = "978" + String.format("%04d", random.nextInt(10000));
                    // Ordina correttamente min e max per il range
                    if (isbn.compareTo(isbnEnd) < 0) {
                        writer.println("RANGE " + isbn + " " + isbnEnd);
                    } else {
                        writer.println("RANGE " + isbnEnd + " " + isbn);
                    }
                }
                else { // 1% probabilità: PRINT
                    writer.println("PRINT");
                }
            }
            
            System.out.println("File '" + nomeFile + "' generato con successo con " + NUMERO_OPERAZIONI + " operazioni!");

        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file: " + e.getMessage());
        }
    }
}