import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        RedBlackBST<String,Book> catalogue = new RedBlackBST<>();
        try {
            String input = args.length > 0 ? args[0] : "input.txt";
            Scanner scanner =  new Scanner(new File(input));


            Pattern pattern = Pattern.compile("^\\s+(\\S+)\\s+(.+)\\s+(\\S+)\\s+(\\d+)\\s*$");

            String output = args.length > 1 ? args[1] : "output.txt";
            PrintStream outStream = new PrintStream(new File(output));
            System.setOut(outStream);

            long startTime = System.currentTimeMillis();

            while(scanner.hasNext()) {
                String directive = scanner.next();

                switch(directive) {
                    case "INSERT":
                        String restOfLine = scanner.nextLine(); 
                        Matcher matcher = pattern.matcher(restOfLine);
                        if (matcher.matches()) {
                            String isbn = matcher.group(1);
                            String title = matcher.group(2).trim();
                            String author = matcher.group(3);
                            int year = Integer.parseInt(matcher.group(4));
                            
                            catalogue.put(isbn, new Book(title, author, year));
                        } else {
                            System.err.println("Errore di formattazione nella riga INSERT: " + restOfLine);
                        }
                        break;

                    case "SEARCH":
                        String searchIsbn = scanner.next();
                        System.out.println("(SEARCH)");
                        Book book = catalogue.get(searchIsbn);
                        if(book != null) {
                            System.out.println("Libro trovato:");
                            System.out.println(book.getEntireDetails(searchIsbn));
                        } else {
                            System.out.println("Libro non trovato:");
                        }
                        break;

                    case "DELETE":
                            String deleteIsbn = scanner.next();
                            catalogue.delete(deleteIsbn);
                        break;

                    case "RANGE":
                        String startIsbn = scanner.next();
                        String endIsbn = scanner.next();
                        System.out.println("(RANGE " + startIsbn + " " + endIsbn + ")");
                        catalogue.range(startIsbn, endIsbn);
                        break;

                    case "PRINT":
                        System.out.println("PRINT");
                        catalogue.print();
                        break;
                }
            }
            scanner.close();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.err.println("Tempo di esecuzione: " + executionTime + " ms");
        }
        catch(FileNotFoundException e){
            System.err.println("Errore: File di input non trovato.");
        }
    }
}
