public class Book {
    private String title;
    private String author;
    private int year;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String toString() {
        return this.title;
    }

    public String getEntireDetails(String isbn) {
        return "ISBN: " + isbn + "\n" +
               "Titolo: " + this.title + "\n" +
               "Autore: " + this.author + "\n" +
               "Anno: " + this.year;
    }
    
    
}
