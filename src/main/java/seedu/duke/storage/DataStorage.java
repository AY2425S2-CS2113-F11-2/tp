package seedu.duke.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static final String DATA_FILE = "C:\\Users\\user\\tp\\src\\main\\java\\seedu\\duke\\storage\\data.txt";

    // Ensure file exists on startup (does NOT create directories)
    public static void ensureFileExists() {
        File file = new File(DATA_FILE);
        try {
            if (file.createNewFile()) {
                System.out.println("Storage file created: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error creating storage file: " + e.getMessage());
        }
    }

    public static void saveData(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to storage: " + e.getMessage());
        }
    }

    public static List<String> loadData() {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading storage: " + e.getMessage());
        }
        return data;
    }

    public static void displayStoredData(String filePath) {
        System.out.println("Loading previous session data in " + filePath + ": ");
        List<String> data = loadData();
        if (data.isEmpty()) {
            System.out.println("No previous data found.");
        } else {
            for (String line : data) {
                System.out.println(line);
            }
        }
    }

    // Clear storage file
    public static void clearStorage() {
        try (PrintWriter writer = new PrintWriter(DATA_FILE)) {
            writer.print(""); // Overwrite file with empty content
        } catch (IOException e) {
            System.out.println("Error clearing storage: " + e.getMessage());
        }
    }
}
