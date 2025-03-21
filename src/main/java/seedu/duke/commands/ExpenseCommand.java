package seedu.duke.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;
import seedu.duke.friends.Friend;
import seedu.duke.friends.GroupManager;

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
        assert budgetManager != null : "BudgetManager cannot be null";
        assert scanner != null : "Scanner cannot be null";
        this.budgetManager = budgetManager;
        this.scanner = scanner;
    }

    /**
     * Executes the add expense command.
     */
    public void executeAddExpense() {
        try {
            System.out.println("Enter expense title:");
            String title = scanner.nextLine().trim();
            
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty.");
                return;
            }
            
            System.out.println("Enter expense description:");
            String description = scanner.nextLine().trim();
            
            System.out.println("Enter expense amount:");
            String amountStr = scanner.nextLine().trim();
            
            if (amountStr.isEmpty()) {
                System.out.println("Amount cannot be empty.");
                return;
            }
            
            double amount = Double.parseDouble(amountStr);

            System.out.println("Enter :");
            
            if (amount < 0) {
                System.out.println("Amount cannot be negative.");
                return;
            }
            
            assert amount >= 0 : "Amount should be non-negative";
            Expense expense = new Expense(title, description, amount);
            budgetManager.addExpense(expense);
            
            System.out.println("Expense added successfully:");
            System.out.println(expense);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }
    //@@author matthewyeo1

    /**
     * Executes the delete expense command by setting the expense amount to 0.0.
     */
    public void executeDeleteExpense() {
        try {
            displayAllExpenses();

            if (budgetManager.getExpenseCount() == 0) {
                return;
            }

            System.out.println("Enter the index of the expense to delete:");
            String indexStr = scanner.nextLine().trim();
            
            if (indexStr.isEmpty()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }
            
            int index = Integer.parseInt(indexStr) - 1; // Convert to 0-based index
            
            if (index < 0 || index >= budgetManager.getExpenseCount()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }

            assert index >= 0 && index < budgetManager.getExpenseCount() : "Index should be within valid range";
            Expense deletedExpense = budgetManager.deleteExpense(index);
            // Update the owesData.txtfile
            updateOwesDataFile(deletedExpense);
            System.out.println("Expense deleted successfully:");
            System.out.println(deletedExpense);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch (Exception e) {
            System.out.println("Error deleting expense: " + e.getMessage());
        }
    }

    //@@author

    /**
     * Executes the edit expense command.
     */
    public void executeEditExpense() {
        try {
            displayAllExpenses();
            
            if (budgetManager.getExpenseCount() == 0) {
                return;
            }
            
            System.out.println("Enter the index of the expense to edit:");
            String indexStr = scanner.nextLine().trim();
            
            if (indexStr.isEmpty()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }
            
            int index = Integer.parseInt(indexStr) - 1; // Convert to 0-based index
            
            if (index < 0 || index >= budgetManager.getExpenseCount()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }
            
            assert index >= 0 && index < budgetManager.getExpenseCount() : "Index should be within valid range";
            
            // Display current details
            Expense currentExpense = budgetManager.getExpense(index);
            assert currentExpense != null : "Current expense should not be null";
            System.out.println("Current expense details:");
            System.out.println(currentExpense);
            
            // Get new details (leave blank to keep current)
            System.out.println("Enter new title (press Enter to keep current):");
            String title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                title = null;
            }
            
            System.out.println("Enter new description (press Enter to keep current):");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = null;
            }
            
            System.out.println("Enter new amount (press Enter to keep current):");
            String amountInput = scanner.nextLine().trim();
            double amount = -1; // Sentinel value to indicate no change
            if (!amountInput.isEmpty()) {
                amount = Double.parseDouble(amountInput);
                if (amount < 0) {
                    System.out.println("Amount cannot be negative. Keeping current amount.");
                    amount = -1;
                }
                assert amount >= 0 || amount == -1 : "Amount should be non-negative or -1 for no change";
            }
            
            Expense editedExpense = budgetManager.editExpense(index, title, description, amount);
            assert editedExpense != null : "Edited expense should not be null";
            System.out.println("Expense edited successfully:");
            System.out.println(editedExpense);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch (Exception e) {
            System.out.println("Error editing expense: " + e.getMessage());
        }
    }

    /**
     * Displays all expenses.
     */
    public void displayAllExpenses() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        assert expenses != null : "Expenses list should not be null";
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        
        System.out.println("List of Expenses:");
        for (int i = 0; i < expenses.size(); i++) {
            assert expenses.get(i) != null : "Expense at index " + i + " should not be null";
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
        System.out.println("Total amount owed: $" + String.format("%.2f", totalBalance));
    }

    /**
     * Executes the mark expense command.
     */
    public void executeMarkCommand() {
        System.out.println("Enter expense number to mark");
        String expenseNumberToMark = scanner.nextLine().trim();
        try{
            int indexToMark = Integer.parseInt(expenseNumberToMark) - 1;
            budgetManager.markExpense(indexToMark);
        } catch(IndexOutOfBoundsException e){
            System.out.println("Please enter a valid expense number.");
        } catch(NumberFormatException e){
            System.out.println("Please enter a number.");
        }
    }

    /**
     * Executes the mark expense command.
     */
    public void executeUnmarkCommand() {
        System.out.println("Enter expense number to mark");
        String expenseNumberToMark = scanner.nextLine().trim();
        try {
            int indexToUnmark = Integer.parseInt(expenseNumberToMark) - 1;
            budgetManager.unmarkExpense(indexToUnmark);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch(NumberFormatException e){
            System.out.println("Please enter a number.");
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
