/**
 * Implementazione di un Albero di Ricerca Binaria Rosso-Nero nella variante Left-Leaning Red-Black BST di Sedgewick,
 * per simulare un albero 2-3 garantendo prestazioni logaritmiche O(log N) per le operazioni di ricerca, inserimento e cancellazione.
 * La classe è di tipo parametrico per favorirne la riutilizzabilità,
 * secondo il principio di astrazione della programmazione orientata agli oggetti.
 *
 * @param <K>     Il tipo della chiave, deve implementare Comparable.
 * @param <Value> Il tipo del valore associato alla chiave.
 */
public class RedBlackBST<K extends Comparable<K>, Value> {
    
    // I colori degli archi sono definiti a livello di classe come costanti booleane
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int n; // Numero totale di nodi nell'albero

    /**
     * Rappresenta un nodo interno dell'Albero Rosso-Nero.
     * Classe privata per rispettare il principio di incapsulamento: l'utente esterno 
     * interagisce solo con chiavi e valori, ignorando la struttura a nodi.
     */
    private class Node {
        private K key;
        private Value val;
        private Node left, right;
        private boolean color;

        /**
         * Crea un nuovo nodo.
         * @param key   La chiave di ricerca.
         * @param val   Il valore da associare.
         * @param color Il colore del legame con il nodo padre (RED o BLACK).
         */
        public Node(K key, Value val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
            // I puntatori left e right sono inizializzati automaticamente a null
        }
    }

    /**
     * Cerca il valore associato a una chiave specifica.
     * @param key La chiave da cercare.
     * @return Il valore associato, oppure null se la chiave non è presente.
     */
    public Value get(K key) {
        return get(root, key);
    }

    // Metodo di supporto privato per la ricerca
    private Value get(Node x, K key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val; // Chiave trovata
        }
        return null; // Chiave non trovata
    }

    /**
     * Verifica se l'albero contiene una determinata chiave.
     * @param key La chiave da verificare.
     * @return true se presente, false altrimenti.
     */
    public boolean contains(K key) {
        return get(key) != null;
    }

    /**
     * Inserisce una coppia chiave-valore nell'albero. 
     * Se la chiave esiste già, aggiorna il suo valore.
     * @param key La chiave da inserire.
     * @param val Il valore associato.
     */
    public void put(K key, Value val) {
        root = insert(root, key, val);
        // La radice dell'albero deve sempre essere nera per le proprietà L-LRB
        root.color = BLACK;
    }

    // Metodo di supporto per l'inserimento
    private Node insert(Node h, K key, Value val) {
        // I nuovi nodi sono sempre rossi inizialmente
        if (h == null) {
            n++;
            return new Node(key, val, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = insert(h.left, key, val);
        else if (cmp > 0) h.right = insert(h.right, key, val);
        else h.val = val; // Aggiornamento valore se la chiave esiste

        // Risale l'albero e applica le regole L-LRB per ripristinare il bilanciamento
        return balance(h);
    }

    /**
     * @return Il numero di elementi presenti nell'albero.
     */
    public int size() {
        return n;
    }

    /**
     * @return true se l'albero è vuoto, false altrimenti.
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Calcola l'altezza totale dell'albero, ovvero la lunghezza del percorso più lungo.
     * @return L'altezza dell'albero.
     */
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /**
     * Calcola l'altezza nera dell'albero.
     * Per le proprietà dell'Albero Rosso-Nero, tutti i percorsi dalla radice 
     * alle foglie hanno esattamente lo stesso numero di nodi neri.
     * @return L'altezza nera dell'albero.
     */
    public int getBlackHeight() {
        int blackHeight = 0;
        Node current = root;
        // Scende solo a sinistra, poiché l'altezza nera è uniforme su tutti i rami
        while (current != null) {
            if (!isRed(current)) {
                blackHeight++;
            }
            current = current.left;
        }
        return blackHeight;
    }

    /**
     * Trova la chiave minima presente nell'albero.
     * @return La chiave minima, o null se l'albero è vuoto.
     */
    public K min() {
        if (isEmpty()) return null;
        return min(root).key;
    }

    private Node min(Node x) {
        // Il minimo si trova sempre scendendo tutto a sinistra
        if (x.left == null) return x;
        return min(x.left);
    }

    /**
     * Trova la chiave massima presente nell'albero.
     * @return La chiave massima, o null se l'albero è vuoto.
     */
    public K max() {
        if (isEmpty()) return null;
        return max(root).key;
    }

    private Node max(Node x) {
        // Il massimo si trova sempre scendendo tutto a destra
        if (x.right == null) return x;
        return max(x.right);
    }

    /**
     * Controlla se un nodo è collegato al padre con un legame rosso.
     */
    private boolean isRed(Node x) {
        if (x == null) return false; // I nodi null (foglie esterne) sono considerati neri
        return x.color == RED;
    }

    /**
     * Esegue una rotazione a sinistra sul nodo specificato.
     * Ripristina il bilanciamento spostando un legame rosso inclinato a destra verso sinistra.
     *
     * @param h Il nodo da ruotare.
     * @return La nuova radice del sottoalbero.
     */
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    /**
     * Esegue una rotazione a destra sul nodo specificato.
     * Risolve la presenza di due legami rossi consecutivi a sinistra.
     *
     * @param h Il nodo da ruotare.
     * @return La nuova radice del sottoalbero.
     */
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    /**
     * Inverte i colori di un nodo e dei suoi figli.
     * Corrisponde alla "rottura di un nodo a 4" nell'isomorfismo con gli alberi 2-3.
     */
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    /**
     * Stampa a schermo tutti i nodi con chiavi comprese tra lowest e highest.
     * @param lowest  Il limite inferiore del range.
     * @param highest Il limite superiore del range.
     */
    public void range(K lowest, K highest) {
        StringBuilder sb = new StringBuilder();
        range(root, lowest, highest, sb);
        System.out.print(sb.toString());
    }

    private void range(Node x, K lowest, K highest, StringBuilder sb) {
        if (x == null) return;
        
        int cmpLo = lowest.compareTo(x.key);
        int cmpHi = highest.compareTo(x.key);

        // Esplora il ramo sinistro solo se potrebbe contenere chiavi nel range
        if (cmpLo < 0) range(x.left, lowest, highest, sb);
        
        // Se la chiave corrente è nel range, si concatena allo StringBuilder
        if (cmpLo <= 0 && cmpHi >= 0) {
            sb.append(x.key).append(" ").append(x.val.toString()).append("\n");
        }
        
        // Esplora il ramo destro solo se potrebbe contenere chiavi nel range
        if (cmpHi > 0) range(x.right, lowest, highest, sb);
    }

    /**
     * Stampa in ordine tutto il contenuto dell'albero.
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        print(root, sb);
        System.out.print(sb.toString());
    }

    private void print(Node x, StringBuilder sb) {
        if (x == null) return;
        print(x.left, sb);
        sb.append(x.key).append(" ").append(x.val.toString()).append("\n");
        print(x.right, sb);
    }

    /**
     * Elimina una chiave e il relativo valore dall'albero.
     * @param key La chiave da rimuovere.
     */
    public void delete(K key) {
        if (!contains(key)) return;
        
        // Se la radice e i suoi figli sono neri, viene resa rossa temporaneamente
        // per permettere la propagazione del rosso verso il basso
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, key);
        n--;
        if (!isEmpty()) root.color = BLACK;
    }

    private Node delete(Node h, K key) {
        if (key.compareTo(h.key) < 0) {
            // Bisogna assicurarsi che il nodo h.left o il suo figlio sinistro siano rossi
            // per evitare di eliminare da un nodo solo nero
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && (h.right == null)) {
                return null; // Eliminazione del nodo foglia
            }
            // Per preparare la discesa a destra è propagato il rosso
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                // Sostituzione con il successore (il minimo del sottoalbero destro)
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h); // L'albero viene ribilanciato risalendo verso la radice
    }

    /**
     * Elimina il nodo con la chiave minima nel sottoalbero.
     */
    private Node deleteMin(Node h) {
        if (h.left == null) return null;
        // Spinge il legame rosso verso sinistra in modo da non eliminare un nodo foglia nero
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = deleteMin(h.left);
        return balance(h);
    }

    /**
     * Operazioni di supporto per l'eliminazione: spinge un legame rosso verso il figlio sinistro.
     */
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    /**
     * Operazioni di supporto per l'eliminazione: spinge un legame rosso verso il figlio destro.
     */
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    /**
     * Ripristina le regole strutturali dell'Albero Rosso-Nero (L-LRB) per il nodo specificato.
     * Viene richiamato in fase di risalita dopo inserimenti o cancellazioni.
     *
     * @param h Il nodo da bilanciare.
     * @return Il nodo bilanciato.
     */
    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }
}