package frc.util.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import frc.robot.Constants;

public class CSVReader {
    
    private static final String COMMA_DELIMITER = ",";
    private static final String fileType = ".csv";
    private static String path;

    public static List<List<Double>> convertToArrayList(String fileName) throws FileNotFoundException {
        List<List<Double>> records = new ArrayList<>();
        path = new String(path = Constants.ROOT_DIRECTORY + fileName + fileType);
        System.out.println(path);
        try (Scanner scanner = new Scanner(new File(path));) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
            scanner.close();
        }
        return records;
    }

    private static List<Double> getRecordFromLine(String line) {
        List<Double> values = new ArrayList<Double>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(Double.parseDouble(rowScanner.next()));
            }
            rowScanner.close();
        }
        return values;
    }
}