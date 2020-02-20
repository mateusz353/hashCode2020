import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Discret Backpack Problem - simple Dynamic programming (top-down)

public class HashCode {

    private static final String problemName = "a_example";
    private static final String srcDir = "./input/";

    private static final Path inputFilePath = Paths.get(srcDir + problemName + ".txt");
    private static final Path outputFilePath = Paths.get(srcDir + problemName + ".txt");

    //the number of different books
    int B;
    //numbers of libraries
    int L;
    //the number of days
    int D;
    Map<Integer, Library> libraries = new HashMap<>();
    Map<Integer, Book> books = new HashMap<>();

    public static void main(String[] args) {
        Instant start = Instant.now();

        HashCode solution = new HashCode();
        solution.solveProblem();

        Instant finish = Instant.now();
        System.out.println("Execution time:" + Duration.between(start, finish).toMillis() + " ms.");

    }

    public void solveProblem() {

        readInputFile();
        LinkedHashMap<Library, List<Book>> libraryListLinkedHashMap = solveBetter();
        writeResultToFile(libraryListLinkedHashMap);

    }

    public LinkedHashMap<Library, List<Book>> solveBetter() {
        int day = 0;
        LinkedHashMap<Library, List<Book>> solution = new LinkedHashMap<>();
        while (day < D) {
            Library best = null;
            int bestScore = -1;
            List<Book> bestBooks = null;
            for (Library lib : libraries.values()) {
                if (day + lib.delay >= D)
                    continue;

                List<Book> bestCurrentBooks = lib.stupidBestBooksToLoad(day, D);
                int currentScore = countScore(bestBooks);
                if (best == null || bestScore < currentScore) {
                    best = lib;
                    bestScore = currentScore;
                    bestBooks = bestCurrentBooks;
                }
            }

            if (best == null)
                return solution;

            solution.put(best, bestBooks);
            for (Book book : bestBooks) {
                for (Library library : book.libraries.values()) {
                    library.books.remove(book.id);
                }
            }
            libraries.remove(best.id);
            day += best.delay;
        }

        return solution;
    }

    private void readInputFile() {
        try (BufferedReader reader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8)) {
            String[] firstLine = reader.readLine().split(" ");
            B = Integer.parseInt(firstLine[0]);
            L = Integer.parseInt(firstLine[1]);
            D = Integer.parseInt(firstLine[2]);

            String[] booksScore = reader.readLine().split(" ");

            for (int i = 0; i < B; i++) {
                Book book = new Book();
                book.id = i;
                book.score = Integer.parseInt(booksScore[i]);
                books.put(i, book);
            }

            for (int j = 0; j < L; j++) {
                String[] libraryInfo = reader.readLine().split(" ");
                Library library = new Library();
                library.id = j;
                library.booksNumber = Integer.parseInt(libraryInfo[0]);
                library.delay = Integer.parseInt(libraryInfo[1]);
                library.speed = Integer.parseInt(libraryInfo[2]);

                String[] libraryBooks = reader.readLine().split(" ");
                Map<Integer, Book> libraryBooksMap = new HashMap();
                for (int k = 0; k < library.booksNumber; k++) {
                    int bookId = Integer.parseInt(libraryBooks[k]);
                    Book libraryBook = books.get(bookId);
                    libraryBook.libraries.put(k, library);
                    libraryBooksMap.put(bookId, libraryBook);
                }
                library.books = libraryBooksMap;
                libraries.put(j, library);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResultToFile(LinkedHashMap<Library, List<Book>> result) {
        System.out.println(result.keySet().size());
        for (Map.Entry<Library, List<Book>> entry : result.entrySet()) {
            System.out.println(entry.getKey().id + " " + entry.getValue().size());
            System.out.println(entry.getValue().stream().map(b -> String.valueOf(b.id)).collect(Collectors.joining(" ")));
        }
    }

    private static class Book {

        public int id;
        public int score;
        public Map<Integer, Library> libraries = new HashMap<>();

    }

    public int countScore(List<Book> books) {
        return books.stream().mapToInt(b -> b.score).sum();
    }

    private static class Library {

        public int id;
        public int delay;
        public int booksNumber;
        public int speed;
        public Map<Integer, Book> books = new HashMap<>();

        public List<Book> stupidBestBooksToLoad(int signupDay, int dDay) {
            int pullDays = dDay - (signupDay + delay);
            List<Book> allBooks = new ArrayList<>();
            allBooks.addAll(books.values());
            allBooks.sort(Comparator.comparingInt(b -> -b.score));
            return allBooks.subList(0, pullDays * speed);
        }

    }
}
