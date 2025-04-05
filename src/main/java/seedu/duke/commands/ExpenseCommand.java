//@@author mohammedhamdhan
package seedu.duke.commands;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;

import seedu.duke.currency.Currency;
import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;
import seedu.duke.friends.Friend;
import seedu.duke.friends.GroupManager;
import seedu.duke.summary.Categories;
import seedu.duke.summary.ExpenseClassifier;

/**
 * Handles expense-related commands entered by the user.
 */
public class ExpenseCommand {
    private BudgetManager budgetManager;
    private Scanner scanner;
    private GroupManager groupManager;
    private Currency currency;

    /**
     * Constructs an ExpenseCommand with the given BudgetManager and Scanner.
     *
     * @param budgetManager the budget manager to use
     * @param scanner       the scanner for user input
     */
    public ExpenseCommand(BudgetManager budgetManager, Scanner scanner, Currency currency) {
        assert budgetManager != null : "BudgetManager cannot be null";
        assert scanner != null : "Scanner cannot be null";
        this.budgetManager = budgetManager;
        this.scanner = scanner;
        this.currency = currency;
    }
    //@@author

    //@@author matthewyeo1
    public static boolean isValidDate(String date) {
        if (date.isEmpty()) {
            return false;
        }

        // Ensure format is correct: DD-MM-YYYY
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }

        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[2]);

        if (year < 2000) {
            return false;
        }

        // Validate actual date values
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false); // Strict date checking
        try {
            dateFormat.parse(date); // Throws exception if date is invalid
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidDescription(String description) {
        return description.length() <= 200;
    }

    public static boolean isUniqueTitle(String title, List<Expense> expenses) {
        for (Expense expense : expenses) {
            if (expense.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAmountBelowCap(Double amount, Currency currency) {
        double maxSGDAmount = 50000.0;
        double sgdEquivalentAmount;
        String currentCurrency = currency.getCurrentCurrency();

        if (currentCurrency.equals("SGD")) {
            Double exchangeRate = currency.getExchangeRate(currentCurrency);
            if (exchangeRate == null) {
                System.out.println("Error: Unable to retrieve exchange rate for " + currentCurrency);
                return false;
            }
            sgdEquivalentAmount = amount / exchangeRate;
        } else {
            sgdEquivalentAmount = amount;
        }

        if (sgdEquivalentAmount > maxSGDAmount) {
            System.out.println("The entered amount exceeds the maximum " +
                    "allowed limit of 50,000 SGD (or its equivalent).");
            return false;
        }
        return true;
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Executes the add expense command.
     */
    public void executeAddExpense(String userInput) {
        String[] parts = userInput.split("/", 4); // Split into title, date, amount

        if (parts.length < 4 || parts[1].trim().isEmpty() || parts[2].trim().isEmpty() || parts[3].trim().isEmpty()) {
            System.out.println("Invalid format. Usage: add/<title>/<date>/<amount>");
            return;
        }

        try {
            String title = parts[1].trim();
            String date = parts[2].trim();
            String amountStr = parts[3].trim();

            // Validate title uniqueness
            List<Expense> expenses = budgetManager.getAllExpenses();
            if (!isUniqueTitle(title, expenses)) {
                System.out.println("Expense with the same title already exists.");
                return;
            }

            // Validate date
            if (!isValidDate(date)) {
                System.out.println("Invalid date. Please enter a valid date in DD-MM-YYYY format.");
                return;
            }

            // Validate amount
            double amount = Double.parseDouble(amountStr);
            if (amount < 0) {
                System.out.println("Amount cannot be negative.");
                return;
            }
            if (!isAmountBelowCap(amount, currency)) {
                return;
            }

            // Prompt for optional description
            System.out.println("Enter the description (press Enter to skip):");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "nil";
            } else if (!isValidDescription(description)) {
                System.out.println("Description exceeds 200 characters.");
                return;
            }

            // Create and add expense
            Expense expense = new Expense(title, description, date, amount);
            budgetManager.addExpense(expense);

            System.out.println("Expense added successfully:");
            System.out.println(expense);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }
    //@@author

    //@@author matthewyeo1
    /**
     * Executes the delete expense command by setting the expense amount to 0.0.
     */
    public void executeDeleteExpense(String userInput) {

        try {
            String[] parts = userInput.split("/", 2); // Split into command and expense ID
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                System.out.println("Invalid format. Usage: delete/<expense ID>");
                return;
            }

            int index = Integer.parseInt(parts[1].trim()) - 1; // Convert to 0-based index

            if (index < 0 || index >= budgetManager.getExpenseCount()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }

            Expense expenseToDelete = budgetManager.getExpense(index);
            System.out.println("Are you sure you want to delete this expense? (y/n)");
            System.out.println(expenseToDelete);
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("y")) {
                System.out.println("Deletion aborted.");
                return;
            }

            // Delete the expense
            Expense deletedExpense = budgetManager.deleteExpense(index);
            updateOwesDataFile(deletedExpense);
            System.out.println("Expense deleted successfully:");
            System.out.println(deletedExpense);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid expense ID.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch (Exception e) {
            System.out.println("Error deleting expense: " + e.getMessage());
        }
    }

    //@@author

    //@@author mohammedhamdhan
    /**
     * Executes the edit expense command.
     */
    public void executeEditExpense(String userInput) {
        try {
            String[] parts = userInput.split("/", 5); // Split into ID, title, date, amount
            if (parts.length < 5 || parts[1].trim().isEmpty() || parts[4].trim().isEmpty()) {
                System.out.println("Invalid format. Usage: edit/<expense ID>/<new title>/<new date>/<new amount>");
                return;
            }

            int index = Integer.parseInt(parts[1].trim()) - 1; // Convert to 0-based index
            if (index < 0 || index >= budgetManager.getExpenseCount()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }

            String title = parts[2].trim().equalsIgnoreCase("x") ? null : parts[2].trim();
            String date = parts[3].trim().equalsIgnoreCase("x") ? null : parts[3].trim();
            String amountStr = parts[4].trim().equalsIgnoreCase("x") ? null : parts[4].trim();

            // Validate date if provided
            if (date != null && !isValidDate(date)) {
                System.out.println("Invalid date format. Please enter a valid date in DD-MM-YYYY format.");
                return;
            }

            // Validate amount if provided
            double amount = -1; // Sentinel value for no change
            if (amountStr != null) {
                amount = Double.parseDouble(amountStr);
                if (amount < 0) {
                    System.out.println("Amount cannot be negative. Keeping current amount.");
                    amount = -1;
                }
                assert amount >= 0 || amount == -1 : "Amount should be non-negative or -1 for no change";

                if (!isAmountBelowCap(amount, currency)) {
                    System.out.println("Amount exceeds the maximum allowed limit of 50,000 SGD (or its equivalent).");
                    return;
                }
            }

            // Prompt for optional description
            System.out.println("Enter new description (press Enter to keep current):");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "nil";
            } else if (!isValidDescription(description)) {
                System.out.println("Description exceeds 200 characters.");
                return;
            }

            // Edit the expense
            Expense editedExpense = budgetManager.editExpense(index, title, description, date, amount);
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
     * Displays all expenses in chronological order (most recent first).
     */
    public void displayAllExpenses() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        assert expenses != null : "Expenses list should not be null";

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("All expenses are in " + currency.getCurrentCurrency());

        System.out.println("List of Expenses:");
        for (int i = 0; i < expenses.size(); i++) {
            assert expenses.get(i) != null : "Expense at index " + i + " should not be null";
            System.out.println("Expense #" + (i + 1));
            System.out.println(expenses.get(i));
            System.out.println();
        }
    }
    //@@author

    //@@author NandhithaShree
    /**
     * Displays all settled (completed) expenses sorted by date in descending order.
     */
    public void displaySettledExpenses(){
        List<Expense> expenses = budgetManager.getAllExpenses();
        int numberOfExpensesPrinted = 0;

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("All expenses are in " + currency.getCurrentCurrency());

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

        String pluralOrSingular = (numberOfExpensesPrinted != 1 ? "expenses" : "expense");
        System.out.println("You have " + numberOfExpensesPrinted + " settled " + pluralOrSingular);
    }

    /**
     * Displays all unsettled (pending) expenses sorted by date in descending order.
     */
    public void displayUnsettledExpenses() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        int numberOfExpensesPrinted = 0;

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("All expenses are in " + currency.getCurrentCurrency());

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

        String pluralOrSingular = (numberOfExpensesPrinted != 1 ? "expenses" : "expense");
        System.out.println("You have " + numberOfExpensesPrinted + " unsettled " + pluralOrSingular);
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Shows the balance overview.
     */
    public void showBalanceOverview() {
        double totalBalance = budgetManager.getTotalBalance();
        System.out.println("Balance Overview");
        System.out.println("----------------");
        System.out.println("Total number of unsettled expenses: " + budgetManager.getUnsettledExpenseCount());
        System.out.println("Total amount owed: " + String.format("%.2f", totalBalance));
    }
    //@@author

    //@@author NandhithaShree
    /**
     * Marks an expense as settled based on user input.
     *
     * @throws NumberFormatException if the input is not a valid number
     * @throws IndexOutOfBoundsException if the input is out of range
     */
    public void executeMarkCommand(String command) {
        try{
            String[] splitInput = command.split("/");
            if(splitInput.length != 2){
                System.out.println("Please provide input in correct format");
                return;
            }
            String expenseNumberToMark = splitInput[1];
            int indexToMark = Integer.parseInt(expenseNumberToMark) - 1;
            if(budgetManager.getExpense(indexToMark).getDone()){
                System.out.println("Expense was already marked before!");
                return;
            }

            budgetManager.markExpense(indexToMark);
            System.out.println("Expense " + expenseNumberToMark + " successfully marked!");

        } catch(IndexOutOfBoundsException e){
            System.out.println("Please enter a valid expense number.");

        } catch(NumberFormatException e){
            System.out.println("Please enter a number.");
        }
    }

    /**
     * Unmarks an expense as unsettled based on user input.
     *
     * @throws NumberFormatException if the input is not a valid number
     * @throws IndexOutOfBoundsException if the input is out of range
     */
    public void executeUnmarkCommand(String command) {
        try {
            String[] splitInput = command.split("/");
            if(splitInput.length != 2){
                System.out.println("Please provide input in correct format");
                return;
            }
            String expenseNumberToUnmark = splitInput[1];
            int indexToUnmark = Integer.parseInt(expenseNumberToUnmark) - 1;
            if(!budgetManager.getExpense(indexToUnmark).getDone()){
                System.out.println("Expense was already unmarked!");
                return;
            }

            budgetManager.unmarkExpense(indexToUnmark);
            System.out.println("Expense " + expenseNumberToUnmark + " successfully unmarked!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch(NumberFormatException e){
            System.out.println("Please enter a number.");
        }
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Shows the expense summary in different views.
     */
    public void showExpenseSummary() {
        System.out.println("Choose summary view:");
        System.out.println("1. Monthly Summary");
        System.out.println("2. Category-wise Summary");
        System.out.println("3. Cancel");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
        case "1":
            showMonthlySummary();
            break;
        case "2":
            showCategorySummary();
            break;
        case "3":
            return;
        default:
            System.out.println("Invalid choice. Please select 1, 2, or 3.");
            break;
        }
    }

    /**
     * Shows a monthly summary of expenses.
     */
    public void showMonthlySummary() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        assert expenses != null : "Expenses list should not be null";
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        // Group expenses by month
        Map<String, List<Expense>> monthlyExpenses = new HashMap<>();
        Map<String, Double> monthlyTotals = new HashMap<>();
        
        for (Expense expense : expenses) {
            String month = expense.getDate().substring(3, 10); // Get MM-YYYY
            
            // Add expense to the month's list
            if (!monthlyExpenses.containsKey(month)) {
                monthlyExpenses.put(month, new ArrayList<>());
            }
            monthlyExpenses.get(month).add(expense);
            
            // Update month's total amount
            monthlyTotals.merge(month, expense.getAmount(), Double::sum);
        }

        System.out.println("\nMonthly Expense Summary:");
        System.out.println("----------------------");
        for (Map.Entry<String, List<Expense>> entry : monthlyExpenses.entrySet()) {
            String month = entry.getKey();
            List<Expense> monthExpenses = entry.getValue();
            double total = monthlyTotals.get(month);
            
            System.out.printf("%s: $%.2f (%d expenses)%n", month, total, monthExpenses.size());
            
            // List all expense titles for this month
            System.out.println("  Expenses:");
            for (Expense expense : monthExpenses) {
                System.out.printf("  - %s (%s): $%.2f%n", 
                        expense.getTitle(), 
                        expense.getDate(), 
                        expense.getAmount());
            }
            System.out.println();
        }
    }

    /**
     * Shows a category-wise summary of expenses with visualization.
     */
    public void showCategorySummary() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        assert expenses != null : "Expenses list should not be null";
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        // Use ExpenseClassifier to calculate category proportions
        ExpenseClassifier classifier = new ExpenseClassifier();
        Map<Categories, Double> categoryTotals = classifier.calculateCategoryTotals(expenses);
        Map<Categories, Integer> categoryCounts = classifier.calculateCategoryCounts(expenses);

        // Display the summary
        System.out.println("\nCategory-wise Expense Summary:");
        System.out.println("----------------------------");
        
        // Prepare data for pie chart
        List<String> categoryNames = new ArrayList<>();
        List<Number> categoryValues = new ArrayList<>();
        
        for (Categories category : Categories.values()) {
            double total = categoryTotals.get(category);
            int count = categoryCounts.get(category);
            if (count > 0) {  // Only show categories with expenses
                System.out.printf("%s: $%.2f (%d expenses)%n", 
                        category.toString(), total, count);
                
                // Add to chart data
                categoryNames.add(category.toString());
                categoryValues.add(total);
            }
        }
        
        // Ask user if they want to see the chart visualization
        System.out.println("\nDo you want to see a pie chart visualization? (y/n)");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y")) {
            showPieChart(categoryNames, categoryValues);
        }
    }

    /**
     * Displays a pie chart of expenses by category using XChart.
     * 
     * @param categoryNames The list of category names
     * @param categoryValues The list of category values (total expense amounts)
     */
    private void showPieChart(List<String> categoryNames, List<Number> categoryValues) {
        // Create Chart
        PieChart chart = new PieChartBuilder()
                .width(800)
                .height(600)
                .title("Expenses by Category")
                .theme(ChartTheme.GGPlot2)
                .build();
        
        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setAnnotationTextPanelPadding(1);
        chart.getStyler().setPlotContentSize(0.7);
        chart.getStyler().setDecimalPattern("#,###.##");
        chart.getStyler().setLabelsVisible(true);
        chart.getStyler().setLabelType(LabelType.Percentage); // Show percentages on pie slices
        
        // Add an annotation panel with summary info
        chart.addAnnotation(
            new AnnotationTextPanel("Expense Categories Summary", 40, 40, true));
        
        // Create custom series names with formatted amounts for legend
        for (int i = 0; i < categoryNames.size(); i++) {
            String name = categoryNames.get(i);
            Number value = categoryValues.get(i);
            // Format the series name to include the amount
            String formattedName = String.format("%s: $%.2f", name, value.doubleValue());
            chart.addSeries(formattedName, value);
        }
        
        // Display the chart in a Swing window using SwingWrapper
        SwingWrapper<PieChart> wrapper = new SwingWrapper<>(chart);
        JFrame frame = wrapper.displayChart();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add instruction label
        JLabel label = new JLabel("Close this window to return to the application", SwingConstants.CENTER);
        frame.add(label, BorderLayout.SOUTH);
        
        // Resize and center
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        System.out.println("Displaying pie chart. Close the chart window to continue.");
    }

    /**
     * Exports the expense summary to a file.
     */
    public void exportExpenseSummary() {
        System.out.println("Choose export format:");
        System.out.println("1. Monthly Summary");
        System.out.println("2. Category-wise Summary");
        System.out.println("3. Back to main menu");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
        case "1":
            exportMonthlySummary();
            break;
        case "2":
            exportCategorySummary();
            break;
        case "3":
            return;
        default:
            System.out.println("Invalid choice. Please select 1, 2, or 3.");
            break;
        }
    }

    /**
     * Exports monthly summary to a file.
     */
    public void exportMonthlySummary() {
        try (FileWriter writer = new FileWriter("monthly_summary.txt")) {
            List<Expense> expenses = budgetManager.getAllExpenses();
            assert expenses != null : "Expenses list should not be null";
            
            if (expenses.isEmpty()) {
                writer.write("No expenses found.");
                return;
            }

            // Group expenses by month
            Map<String, List<Expense>> monthlyExpenses = new HashMap<>();
            Map<String, Double> monthlyTotals = new HashMap<>();
            
            for (Expense expense : expenses) {
                String month = expense.getDate().substring(3, 10);
                
                // Add expense to the month's list
                if (!monthlyExpenses.containsKey(month)) {
                    monthlyExpenses.put(month, new ArrayList<>());
                }
                monthlyExpenses.get(month).add(expense);
                
                // Update month's total amount
                monthlyTotals.merge(month, expense.getAmount(), Double::sum);
            }

            writer.write("Monthly Expense Summary\n");
            writer.write("----------------------\n");
            for (Map.Entry<String, List<Expense>> entry : monthlyExpenses.entrySet()) {
                String month = entry.getKey();
                List<Expense> monthExpenses = entry.getValue();
                double total = monthlyTotals.get(month);
                
                writer.write(String.format("%s: $%.2f (%d expenses)%n", 
                        month, total, monthExpenses.size()));
                
                // List all expense titles for this month
                writer.write("  Expenses:\n");
                for (Expense expense : monthExpenses) {
                    writer.write(String.format("  - %s (%s): $%.2f%n", 
                            expense.getTitle(), 
                            expense.getDate(), 
                            expense.getAmount()));
                }
                writer.write("\n");
            }
            System.out.println("Monthly summary exported to monthly_summary.txt");
        } catch (IOException e) {
            System.out.println("Error exporting monthly summary: " + e.getMessage());
        }
    }

    /**
     * Exports category-wise summary to a file.
     */
    public void exportCategorySummary() {
        try (FileWriter writer = new FileWriter("category_summary.txt")) {
            List<Expense> expenses = budgetManager.getAllExpenses();
            assert expenses != null : "Expenses list should not be null";
            
            if (expenses.isEmpty()) {
                writer.write("No expenses found.");
                return;
            }

            // Use ExpenseClassifier to calculate category proportions
            ExpenseClassifier classifier = new ExpenseClassifier();
            Map<Categories, Double> categoryTotals = classifier.calculateCategoryTotals(expenses);
            Map<Categories, Integer> categoryCounts = classifier.calculateCategoryCounts(expenses);

            writer.write("Category-wise Expense Summary\n");
            writer.write("----------------------------\n");
            for (Categories category : Categories.values()) {
                double total = categoryTotals.get(category);
                int count = categoryCounts.get(category);
                if (count > 0) {  // Only show categories with expenses
                    writer.write(String.format("%s: $%.2f (%d expenses)%n", 
                            category.toString(), total, count));
                }
            }
            System.out.println("Category summary exported to category_summary.txt");
        } catch (IOException e) {
            System.out.println("Error exporting category summary: " + e.getMessage());
        }
    }
    //@@author

    //@@author matthewyeo1
    /**
     * Gets the budget manager.
     *
     * @return the budget manager
     */
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

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

    //@@author nandhananm7
    /**
     * Searches for expenses containing the given keyword in the title or description.
     */
    public void findExpense(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            System.out.println("Invalid command. Please use the format: find /<keyword>");
            return;
        }

        String keyword = parts[1].trim();

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }

        List<Expense> expenses = budgetManager.getAllExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        List<Expense> matchingExpenses = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Expense expense : expenses) {

            if ((expense.getTitle() != null && expense.getTitle().toLowerCase().contains(lowerKeyword)) ||
                    (expense.getDescription() != null &&
                            expense.getDescription().toLowerCase().contains(lowerKeyword))) {
                matchingExpenses.add(expense);
            }
        }

        if (matchingExpenses.isEmpty()) {
            System.out.println("No matching expenses found for keyword: " + keyword);
        } else {
            System.out.println("Found " + matchingExpenses.size() + " matching expense(s):");
            for (Expense expense : matchingExpenses) {
                System.out.println(expense);
                System.out.println();
            }
        }
    }


    //@@author

    //@@author matthewyeo1
    public void sortExpenses(String option) {
        List<Expense> expenses = budgetManager.getAllExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses to sort.");
            return;
        }

        switch (option) {
        case "1":
            expenses.sort(Comparator.comparing(Expense::getTitle));
            System.out.println("Expenses sorted by title (ascending):");
            break;
        case "2":
            expenses.sort(Comparator.comparing(Expense::getTitle).reversed());
            System.out.println("Expenses sorted by title (descending):");
            break;
        case "3":
            expenses.sort(Comparator.comparing(Expense::getAmount));
            System.out.println("Expenses sorted by amount (ascending):");
            break;
        case "4":
            expenses.sort(Comparator.comparing(Expense::getAmount).reversed());
            System.out.println("Expenses sorted by amount (descending):");
            break;
        default:
            System.out.println("Invalid option.");
            return;
        }

        for (Expense expense : expenses) {
            System.out.println();
            System.out.println(expense);
        }
    }
    //@@author
}
//@@author

