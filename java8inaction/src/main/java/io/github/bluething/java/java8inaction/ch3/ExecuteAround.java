package io.github.bluething.java.java8inaction.ch3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteAround {

    public static void main(String[] args) throws IOException {
        String result = readFileInOldWay();
        System.out.println(result);

        System.out.println("===");

        // Behavior is read file, pass it
        String readOneLine = processFile((BufferedReader bufferedReader) -> bufferedReader.readLine());
        System.out.println(readOneLine);

        System.out.println("===");

        String readTwoLines = processFile((BufferedReader bufferedReader) -> bufferedReader.readLine() + bufferedReader.readLine());
        System.out.println(readTwoLines);
    }

    // Fail fast!
    // Only read one line? How if we want to read two lines?
    // Setup and cleanup can be reuse
    private static String readFileInOldWay() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/ch3/data.txt"))) {
            return bufferedReader.readLine();
        }
    }

    // Behavior of read file as functional interface
    // BufferedReader -> String that throws IOException
    public interface BufferedReaderProcessor {
        public String process(BufferedReader bufferedReader) throws IOException;
    }
    // Argument is a functional interface
    public static String processFile(BufferedReaderProcessor bufferedReaderProcessor) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/ch3/data.txt"))) {
            return bufferedReaderProcessor.process(bufferedReader);
        }
    }
}
