package seedu.duke.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;
import seedu.duke.friends.Friend;
import seedu.duke.friends.GroupManager;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Handles expense-related commands entered by the user.
 */
public class ExpenseCommand {
    private BudgetManager budgetManager;
    private Scanner scanner;
    private GroupManager groupManager;

    /**
     * Constructs an ExpenseCommand with the given BudgetManager and Scanner.
     *
     * @param budgetManager the budget manager to use
     * @param scanner       the scanner for user input
     */
    public ExpenseCommand(BudgetManager budgetManager, Scanner scanner) {
        this.budgetManager = budgetManager;
        this.scanner = scanner;
    }

    /**
     * Executes the add expense command.
     */
    public void executeAddExpense() {
        System.out.println("Enter expense title:");
        String title = scanner.nextLine();
        
        System.out.println("Enter expense description:");
        String description = scanner.nextLine();
        
        System.out.println("Enter expense amount:");
        String amountStr = scanner.nextLine();
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            // Assert amount is positive
            assert amount > 0 : "Expense amount must be positive";
            
            int sizeBefore = budgetManager.getAllExpenses().size();
            Expense newExpense = new Expense(title, description, amount);
            budgetManager.addExpense(newExpense);
            
            // Assert expense was added
            assert budgetManager.getAllExpenses().size() == sizeBefore + 1 : "Failed to add expense";
            
            System.out.println("Expense added successfully:");
            System.out.println(newExpense.toString());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        }
    }

    /**
     * Executes the delete expense command.
     */
    public void executeDeleteExpense() {
        displayAllExpenses();
        
        System.out.println("Enter the index of the expense to delete:");
        String indexStr = scanner.nextLine();
        
        try {
            int index = Integer.parseInt(indexStr);
            
            if (budgetManager.isValidIndex(index)) {
                Expense expenseToDelete = budgetManager.getExpenseByIndex(index);
                
                // Assert expense exists
                assert expenseToDelete != null : "Expense at index " + index + " should not be null";
                
                int sizeBefore = budgetManager.getAllExpenses().size();
                budgetManager.removeExpense(index);
                
                // Assert expense was removed
                assert budgetManager.getAllExpenses().size() == sizeBefore - 1 : "Failed to delete expense";
                
                System.out.println("Expense deleted successfully:");
                System.out.println(expenseToDelete.toString());
            } else {
                System.out.println("Invalid expense index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid index format. Please enter a valid number.");
        }
    }

    //@@author

    /**
     * Executes the edit expense command.
     */
    public void executeEditExpense() {
        displayAllExpenses();
        
        System.out.println("Enter the index of the expense to edit:");
        String indexStr = scanner.nextLine();
        
        try {
            int index = Integer.parseInt(indexStr);
            
            if (budgetManager.isValidIndex(index)) {
                Expense expenseToEdit = budgetManager.getExpenseByIndex(index);
                
                // Assert expense exists
                assert expenseToEdit != null : "Expense at index " + index + " should not be null";
                
                System.out.println("Current expense details:");
                System.out.println(expenseToEdit.toString());
                
                String originalTitle = expenseToEdit.getTitle();
                String originalDescription = expenseToEdit.getDescription();
                double originalAmount = expenseToEdit.getAmount();
                
                // Get new title (leave empty to keep current)
                System.out.println("Enter new title (press Enter to keep current):");
                String newTitle = scanner.nextLine();
                if (newTitle.isEmpty()) {
                    newTitle = originalTitle;
                }
                
                // Get new description (leave empty to keep current)
                System.out.println("Enter new description (press Enter to keep current):");
                String newDescription = scanner.nextLine();
                if (newDescription.isEmpty()) {
                    newDescription = originalDescription;
                }
                
                // Get new amount (leave empty to keep current)
                System.out.println("Enter new amount (press Enter to keep current):");
                String newAmountStr = scanner.nextLine();
                double newAmount = originalAmount;
                if (!newAmountStr.isEmpty()) {
                    try {
                        newAmount = Double.parseDouble(newAmountStr);
                        
                        // Assert new amount is positive
                        assert newAmount > 0 : "New expense amount must be positive";
                        
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount format. Keeping original amount.");
                        newAmount = originalAmount;
                    }
                }
                
                budgetManager.updateExpense(index, newTitle, newDescription, newAmount);
                
                // Assert expense was updated
                Expense updatedExpense = budgetManager.getExpenseByIndex(index);
                assert updatedExpense.getTitle().equals(newTitle) : "Title was not updated correctly";
                assert updatedExpense.getDescription().equals(newDescription) : "Description was not updated correctly";
                assert Math.abs(updatedExpense.getAmount() - newAmount) < 0.001 : "Amount was not updated correctly";
                
                System.out.println("Expense edited successfully:");
                System.out.println(updatedExpense.toString());
            } else {
                System.out.println("Invalid expense index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid index format. Please enter a valid number.");
        }
    }

    /**
     * Displays all expenses.
     */
    public void displayAllExpenses() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        
        System.out.println("List of Expenses:");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println("Expense #" + (i + 1));
            System.out.println(expenses.get(i));
            System.out.println();
        }
    }

    /**
     * Displays all settled expenses.
     */
    public void displaySettledExpenses(){
        List<Expense> expenses = budgetManager.getAllExpenses();
        int numberOfExpensesPrinted = 0;
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        for (int i = 0; i < expenses.size(); i++) {
            while(i < expenses.size() && !expenses.get(i).getDone()) {
                i++;
            }
            if(i >= expenses.size()) {
                break;
            }
            numberOfExpensesPrinted++;
            System.out.println("Expense #" + (i + 1));
            System.out.println(expenses.get(i));
            System.out.println();
        }
        if(numberOfExpensesPrinted != 0){
            System.out.println("List of Settled Expenses:");
        }
        String pluralOrSingular = (numberOfExpensesPrinted != 1 ? "expenses" : "expense");
        System.out.println("You have " + numberOfExpensesPrinted + " settled " + pluralOrSingular);
    }

    /**
     * Displays all unsettled expenses.
     */
    public void displayUnsettledExpenses() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        int numberOfExpensesPrinted = 0;

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        for (int i = 0; i < expenses.size(); i++) {
            while (i < expenses.size() && expenses.get(i).getDone()) {
                i++;
            }
            if (i >= expenses.size()) {
                break;
            }
            numberOfExpensesPrinted++;
            System.out.println("Expense #" + (i + 1));
            System.out.println(expenses.get(i));
            System.out.println();
        }
        if(numberOfExpensesPrinted != 0){
            System.out.println("List of Settled Expenses:");
        }
        String pluralOrSingular = (numberOfExpensesPrinted != 1 ? "expenses" : "expense");
        System.out.println("You have " + numberOfExpensesPrinted + " unsettled " + pluralOrSingular);
    }

    /**
     * Shows the balance overview.
     */
    public void showBalanceOverview() {
        double totalBalance = budgetManager.getTotalBalance();
        System.out.println("Balance Overview");
        System.out.println("----------------");
        System.out.println("Total number of unsettled expenses: " + budgetManager.getUnsettledExpenseCount());
        System.out.println("Total number of unsettled expenses: " + budgetManager.getUnsettledExpenseCount());
        System.out.println("Total amount owed: $" + String.format("%.2f", totalBalance));
    }

    /**
     * Executes the mark expense command.
     */
    public void executeMarkCommand() {
        System.out.println("Enter expense number to mark");
        String expenseNumberToMark = scanner.nextLine().trim();
        int indexToMark = Integer.parseInt(expenseNumberToMark) - 1;
        try{
            budgetManager.markExpense(indexToMark);
        } catch(IndexOutOfBoundsException e){
            System.out.println("Please enter a valid expense number.");
        }
    }

    /**
     * Executes the mark expense command.
     */
    public void executeUnmarkCommand() {
        System.out.println("Enter expense number to mark");
        String expenseNumberToMark = scanner.nextLine().trim();
        int indexToUnmark = Integer.parseInt(expenseNumberToMark) - 1;
        try {
            budgetManager.unmarkExpense(indexToUnmark);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        }
    }

    /**
     * Gets the budget manager.
     *
     * @return the budget manager
     */
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

    //@@author matthewyeo1
    /**
     * Updates the owesData.txt file for the deleted expense.
     *
     * @param deletedExpense the expense being deleted
     */
    private void updateOwesDataFile(Expense deletedExpense) {
        String owesFile = "owedAmounts.txt";
        File file = new File(owesFile);

        // Temporary map to store updated owed amounts
        Map<String, Double> updatedOwes = new HashMap<>();

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("- ")) { // Lines with owed amounts start with "- "
                    String[] parts = line.split(" owes: ");
                    if (parts.length == 2) {
                        String name = parts[0].substring(2).trim(); // Extract the member's name
                        double amount = Double.parseDouble(parts[1].trim()); // Extract the owed amount
                        updatedOwes.put(name, amount); // Store in the map
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Owed amounts file not found. No amounts to update.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Error parsing owed amounts. Some amounts may not be updated.");
            return;
        }

        // Adjust owed amounts for the deleted expense
        List<Friend> groupMembers = getGroupMembersForExpense(deletedExpense);
        if (groupMembers != null && !groupMembers.isEmpty()) {
            double totalAmount = deletedExpense.getAmount();
            int numMembers = groupMembers.size();
            double sharePerMember = totalAmount / numMembers;

            for (Friend member : groupMembers) {
                String name = member.getName();
                double currentOwed = updatedOwes.getOrDefault(name, 0.0);
                double newOwed = Math.max(currentOwed - sharePerMember, 0.0); // Reduce owed amount
                updatedOwes.put(name, newOwed);
            }
        }

        // Clear the existing file content before appending updated data
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(""); // Clear the file
        } catch (IOException e) {
            System.out.println("Error clearing owed amounts file: " + e.getMessage());
            return;
        }

        System.out.println("Updated owed amounts written to file successfully.");
    }

    /**
     * Retrieves the group members associated with the given expense.
     *
     * @param expense the expense to find group members for
     * @return the list of group members, or null if none are found
     */
    private List<Friend> getGroupMembersForExpense(Expense expense) {
        // Assuming the expense has a reference to its associated group
        String groupName = expense.getGroupName(); // Add this method to your Expense class
        if (groupName == null || groupName.isEmpty()) {
            return null;
        }
        return groupManager.getGroupMembers(groupName);
    }
    //@@author
} 
