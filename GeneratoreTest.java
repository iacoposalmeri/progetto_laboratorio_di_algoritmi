import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Utility per la generazione automatica di file di test massivi.
 * Crea dataset casuali simulando le operazioni del catalogo (INSERT, SEARCH, DELETE, RANGE, PRINT)
 * con una distribuzione probabilistica realistica. È utilizzata per testare la solidità 
 * strutturale e la complessità asintotica O(log N) dell'Albero Rosso-Nero sotto stress.
 */
public class GeneratoreTest {
    /**
     * Metodo di avvio del generatore.
     *
     * @param args Array di argomenti da riga di comando:
     * args[0] = Nome del file di output da generare (default: "test.txt")
     * args[1] = Numero totale di operazioni da generare (default: 100)
     */
    public static void main(String[] args) {
        String nomeFile = args.length > 0 ? args[0] : "test.txt";
        int NUMERO_OPERAZIONI = args.length > 1 ? Integer.parseInt(args[1]) : 100;

        String[] autori = {"Sedgewick", "Hoffer", "Arnold", "Tolkien", "Asimov", "Dijkstra", "Knuth", "Turing"};
        String[] paroleTitolo = {"Algoritmi", "Database", "Java", "Programming", "Language", "Strutture", "Dati", "Avanzati", "Base", "Manuale"};
        
        Random random = new Random();

        try (PrintWriter writer = new PrintWriter(nomeFile)) {
            for (int i = 0; i < NUMERO_OPERAZIONI; i++) {
                
                // Genera un numero da 0 a 99 per decidere il comando
                int tipoComando = random.nextInt(100);
                
                // Genera un ISBN casuale
                int numeroIsbn = random.nextInt(10000000);
                String isbn = "978" + String.format("%07d", numeroIsbn);

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
                    if (NUMERO_OPERAZIONI <= 10000) {
                        String isbnEnd = "978" + String.format("%07d", random.nextInt(10000000));
                        if (isbn.compareTo(isbnEnd) < 0) {
                            writer.println("RANGE " + isbn + " " + isbnEnd);
                        } else {
                            writer.println("RANGE " + isbnEnd + " " + isbn);
                        }
                    } else {
                        String isbnEnd = "978" + String.format("%07d", numeroIsbn + 50);
                        writer.println("RANGE " + isbn + " " + isbnEnd);
                    }
                }
                else { // 1% probabilità: PRINT
                    if (NUMERO_OPERAZIONI <= 10000) {
                        writer.println("PRINT");
                    } else {
                        writer.println("SEARCH " + isbn);
                    }
                }
            }
            
            System.out.println("File '" + nomeFile + "' generato con successo con " + NUMERO_OPERAZIONI + " operazioni");

        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file: " + e.getMessage());
        }
    }
}