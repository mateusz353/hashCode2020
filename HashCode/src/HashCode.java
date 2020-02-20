import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

// Discret Backpack Problem - simple Dynamic programming (top-down)

public class HashCode {

    private static final String problemName = "file_name";
    private static final String srcDir = "./";

    private static final Path inputFilePath = Paths.get(srcDir + problemName + ".in");
    private static final Path outputFilePath = Paths.get(srcDir + problemName + ".out");

    public static void main(String[] args) {


        Instant start = Instant.now();

        HashCode solution = new HashCode();
        solution.solveProblem();

        Instant finish = Instant.now();
        System.out.println("Execution time:" + Duration.between(start, finish).toMillis() + " ms.");

    }

    public void solveProblem() {

        Input input = readInputFile();
        Set<Integer> result = new HashSet<Integer>();

        System.out.println(Arrays.toString(result.toArray()));

        writeResultToFile(result);

    }

    public void solveBetter() {
        Map<Integer, Library> libraries = new HashMap<>();
        Map<Integer, Book> books = new HashMap<>();
        int D = 120;
        int day = 0;
        Map<Library, List<Book>> solution = new LinkedHashMap<>();
        while (day < D) {
            Library best = null;
            int bestScore = -1;
            List<Book> bestBooks = null;
            for (Library lib : libraries.values()) {
                List<Book> bestCurrentBooks = lib.stupidBestBooksToLoad(day, D);
                int currentScore = countScore(bestBooks);
                if (best == null || bestScore < currentScore) {
                    best = lib;
                    bestScore = currentScore;
                    bestBooks = bestCurrentBooks;
                }
            }

            solution.put(best, bestBooks);
            for (Book book : bestBooks) {
                for (Library library : book.libraries.values()) {
                    library.books.remove(book.id);
                }
            }
            libraries.remove(best.id);
            day -= best.delay;
        }
    }

    private Input readInputFile() {
        try (BufferedReader reader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8)) {
            Input input = new Input();
            String[] firstLine = reader.readLine().split(" ");
            return input;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeResultToFile(Set<Integer> result) {
        StringBuilder outputFileBuilder = new StringBuilder();
        outputFileBuilder.append(result.size() + "\n");
        result.stream().forEach(item -> outputFileBuilder.append(item + " "));

        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            writer.write(outputFileBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
        public int speed;
        public Map<Integer, Book> books = new HashMap<>();


        public List<Book> stupidBestBooksToLoad(int signupDay, int dDay) {
            int pullDays = dDay - (signupDay + delay);
            List<Book> allBooks = new ArrayList<>();
            allBooks.addAll(books.values());
            allBooks.sort(Comparator.comparingInt(b -> -b.score));
            return allBooks.subList(0, pullDays*speed);
        }

    }

    private class Input {

    }
}
