import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        input.print();
        Set<Integer> result = new HashSet<Integer>();

        System.out.println(Arrays.toString(result.toArray()));

        writeResultToFile(result);

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

    private class Input {

        private int maxWeight;
        private int numberOfElements;
        private int[] weights;

        public int getMaxWeight() {
            return maxWeight;
        }

        public void setMaxWeight(int maxWeight) {
            this.maxWeight = maxWeight;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int[] getWeights() {
            return weights;
        }

        public void setWeights(int[] weights) {
            this.weights = weights;
        }

        public void print() {
            System.out.println("maxWeight = " + maxWeight + "\nnumberOfElements = " + numberOfElements + "\nweights = " + Arrays.toString(weights) + "\n");
        }
    }
}
