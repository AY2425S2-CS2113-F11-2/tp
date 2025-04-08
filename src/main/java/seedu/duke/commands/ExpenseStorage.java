//@@author matthewyeo1
package seedu.duke.commands;

import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.io.FileWriter;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;

public class ExpenseStorage {

    // File paths for expenses data and checksum
    public static String expensesFile = "expenses.txt";
    public static String checksumFile = "expenses.chk";

    /**
     * Appends an expense record to the expenses file and updates the checksum.
     */
    public static void appendExpense(String expenseRecord) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(expensesFile, true))) {
            writer.println(expenseRecord);
        } catch (IOException e) {
            System.out.println("Error writing to expenses file: " + e.getMessage());
        }
        updateChecksum();
    }

    /**
     * Recalculates and writes the checksum for the expenses file.
     */
    public static void updateChecksum() {
        try {
            byte[] fileBytes = Files.readAllBytes(new File(expensesFile).toPath());
            String checksum = getSHA256Checksum(fileBytes);
            try (PrintWriter writer = new PrintWriter(new FileWriter(checksumFile, false))) {
                writer.println(checksum);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error updating checksum: " + e.getMessage());
        }
    }

    /**
     * Verifies the current checksum against the stored checksum.
     */
    public static boolean verifyChecksum() {
        try {
            byte[] fileBytes = Files.readAllBytes(new File(expensesFile).toPath());
            String currentChecksum = getSHA256Checksum(fileBytes);
            List<String> lines = Files.readAllLines(new File(checksumFile).toPath());
            if (lines.isEmpty()) {
                return false; // No stored checksum available
            }
            String storedChecksum = lines.get(0).trim();
            return currentChecksum.equals(storedChecksum);
        } catch (IOException | NoSuchAlgorithmException e) {
            return false;
        }
    }

    /**
     * Utility method to compute SHA-256 checksum of the given data.
     */
    private static String getSHA256Checksum(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(data);
        Formatter formatter = new Formatter();
        for (byte b : hashBytes) {
            formatter.format("%02x", b);
        }
        String hash = formatter.toString();
        formatter.close();
        return hash;
    }

    /**
     * Clears the contents of the expenses file if tampering is detected.
     */
    public static void clearFileIfTampered() {
        if (!verifyChecksum()) {
            // Delete the expenses file
            File expensesFileObj = new File(expensesFile);
            if (expensesFileObj.exists()) {
                boolean deleted = expensesFileObj.delete();
                System.out.println("WARNING: if you have tampered with the expenses file: " + expensesFile
                    + " it will be deleted, and a new one will be created following an add command.");
                System.out.println();
                /*
                if (!deleted) {
                    System.out.println("Error deleting expenses file. File may be in use or inaccessible.");
                } else {
                    System.out.println("Expenses file deleted successfully.");
                }
                */
            } else {
                System.out.println("No expense file found.");
            }

            // Reset the checksum file as well
            try (PrintWriter writer = new PrintWriter(new FileWriter(checksumFile, false))) {
                writer.print(""); // Clear the checksum file
            } catch (IOException e) {
                System.out.println("Error clearing checksum file: " + e.getMessage());
            }
        }
    }

    /**
     * Saves a transaction to the expenses file.
     */
    public static void saveTransaction(String transaction) {
        appendExpense(transaction);
    }

    /**
     * Loads all expenses from the file after verifying the checksum.
     */
    public static List<String> loadExpenses() {
        clearFileIfTampered();
        try {
            return Files.readAllLines(new File(expensesFile).toPath());
        } catch (IOException e) {
            return List.of(); // Return an empty list if there's an error
        }
    }
}
//@@author
