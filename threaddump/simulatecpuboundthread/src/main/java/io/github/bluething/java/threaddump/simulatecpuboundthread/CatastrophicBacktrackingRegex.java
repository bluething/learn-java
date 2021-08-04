package io.github.bluething.java.threaddump.simulatecpuboundthread;

public class CatastrophicBacktrackingRegex {
    public static void main(String[] args) {
        int NUM_OF_ROWS = Integer.parseInt(args[0]);
        int NUM_OF_NUMBERS_IN_ROW = Integer.parseInt(args[1]);
        StringBuilder longCsvString = new StringBuilder();
        for (int i = 1; i <= NUM_OF_NUMBERS_IN_ROW; i++) {
            if (i == NUM_OF_NUMBERS_IN_ROW) {
                longCsvString.append(i);
            } else {
                longCsvString.append(i).append(",");
            }
        }

        System.out.println("Matching string: \"" + longCsvString + "\" with pattern \"^(.*?,)" +
                "{" + NUM_OF_NUMBERS_IN_ROW + "}P\" "
                + NUM_OF_ROWS + " times");

        for (int i = 0; i < NUM_OF_ROWS; i++) {
            longCsvString.toString().matches("^(.*?,){" + NUM_OF_NUMBERS_IN_ROW + "}P");
        }
    }
}
