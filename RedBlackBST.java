public class RedBlackBST<K extends Comparable<K>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int n;

    private class Node {
        private K key;
        private Value val;
        private Node left, right;
        private boolean color;
        public Node (K key, Value val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
        }
    }

    public Value get(K key) {
        return get(root, key);
    }

    public Value get(Node x, K key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public void put(K key, Value val) {
        root = insert(root, key, val);
        root.color = BLACK;
    }

    private Node insert(Node h, K key, Value val) {
        if (h == null){
            n++;
            return new Node(key, val, RED);
        }
        int cmp = key.compareTo(h.key);

        if (cmp < 0) h.left = insert(h.left, key, val);
        else if (cmp > 0) h.right = insert(h.right, key, val);
        else h.val = val;

        return balance(h);
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left),height(x.right));
    }

    public K min() {
        if (isEmpty()) return null;
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    public K max() {
        if (isEmpty()) return null;
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public void range(K lowest, K highest) {
        StringBuilder sb = new StringBuilder();
        range(root, lowest, highest, sb);
        System.out.print(sb.toString());
    }

    private void range(Node x, K lowest, K highest, StringBuilder sb) {
        if (x == null) return;
        
        int cmpLo = lowest.compareTo(x.key);
        int cmpHi = highest.compareTo(x.key);

        if (cmpLo < 0) range(x.left, lowest, highest, sb);
        
        if (cmpLo <= 0 && cmpHi >= 0) {
            sb.append(x.key).append(" ").append(x.val.toString()).append("\n");
        }
        
        if (cmpHi > 0) range(x.right, lowest, highest, sb);
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        print(root, sb);
        System.out.print(sb.toString());
    }

    private void print(Node x, StringBuilder sb) {
        if (x == null) return;
        print(x.left,sb);
        sb.append(x.key).append(" ").append(x.val.toString()).append("\n");
        print(x.right,sb);
    }

    public void delete(K key) {
        if (!contains(key)) return;
        
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, key);
        n--;
        if (!isEmpty()) root.color = BLACK;
    }

    private Node delete(Node h, K key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && (h.right == null)) {
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
    }


    private Node deleteMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = deleteMin(h.left);
        return balance(h);
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }
}