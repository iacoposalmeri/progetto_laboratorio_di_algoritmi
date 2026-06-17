/**
 * Rappresenta un singolo libro all'interno del catalogo.
 * Contiene le informazioni descrittive (titolo, autore, anno) a eccezione dell'ISBN, 
 * che viene invece gestito esternamente come chiave nell'Albero Rosso-Nero.
 */
public class Book {
    private String title;
    private String author;
    private int year;

    /**
     * Crea un nuovo oggetto Book.
     *
     * @param title  Il titolo del libro.
     * @param author L'autore del libro.
     * @param year   L'anno di pubblicazione.
     */
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto.
     * L'Override di questo metodo restituisce intenzionalmente 
     * solo il titolo. Questa scelta permette di adattarsi dinamicamente alla classe 
     * parametrica RedBlackBST, rispettando nel modo più pulito e modulare possibile 
     * l'output richiesto dalla traccia per le operazioni di stampa PRINT e RANGE.
     *
     * @return Il titolo del libro.
     */
    @Override
    public String toString() {
        return this.title;
    }

    /**
     * Genera una scheda completa del libro formattata su più righe.
     * Questo metodo viene utilizzato specificamente dall'operazione di SEARCH, 
     * che richiede la visualizzazione dettagliata di tutti i campi.
     *
     * @param isbn Il codice ISBN associato al libro (fornito dal nodo dell'albero).
     * @return Una stringa formattata contenente ISBN, Titolo, Autore e Anno.
     */
    public String getEntireDetails(String isbn) {
        return "ISBN: " + isbn + "\n" +
               "Titolo: " + this.title + "\n" +
               "Autore: " + this.author + "\n" +
               "Anno: " + this.year;
    }
}