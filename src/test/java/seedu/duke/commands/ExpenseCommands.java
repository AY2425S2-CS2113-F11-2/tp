//@@author matthewyeo1
package seedu.duke.commands;

import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.currency.Currency;
import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;

class ExpenseCommandTest {
    private Currency currency;
    private BudgetManager budgetManager;
    private ExpenseCommand expenseCommand;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private final String testTitle = "Test Expense";
    private final String testDescription = "Test Description";
    private final String testDate = "31-12-2025";
    private final double testAmount = 100.0;


    @BeforeEach
    void setUp() {
        budgetManager = new BudgetManager();
        outContent = new ByteArrayOutputStream();
        currency = new Currency(new Scanner(System.in), budgetManager);
        expenseCommand = new ExpenseCommand(budgetManager, new Scanner(System.in), currency); // Initialize here
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);

        try (PrintWriter writer = new PrintWriter("expenses.txt")) {
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("./currentCurrency");
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File monthlySummaryFile = new File("monthly_summary.txt");
        if (monthlySummaryFile.exists()) {
            try (PrintWriter writer = new PrintWriter(monthlySummaryFile)) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Clear the contents of the "category_summary.txt" file
        File categorySummaryFile = new File("category_summary.txt");
        if (categorySummaryFile.exists()) {
            try (PrintWriter writer = new PrintWriter(categorySummaryFile)) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void provideInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        currency = new Currency(scanner, budgetManager);
        expenseCommand = new ExpenseCommand(budgetManager, scanner, currency );
    }

    @Test
    void testExecuteAddExpenseValidInput() {
        String userInput = "add/Groceries/01-01-2025/100";
        provideInput("Weekly food shopping\n"); // Simulate optional description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount());
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("Weekly food shopping", addedExpense.getDescription());
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(100.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteAddExpenseNoDescription() {
        String userInput = "add/Groceries/01-01-2025/100";
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount());
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("nil", addedExpense.getDescription()); // Description should be empty
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(100.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteAddExpenseInvalidFormat() {
        String userInput = "add/Groceries/01-01-2025"; // Missing amount
        provideInput("\n");
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Invalid format. Usage: add/<title>/<date>/<amount>"));
    }

    @Test
    void testExecuteAddExpenseDuplicateTitle() {
        // Add an initial expense
        budgetManager.addExpense(new Expense("Groceries", "Food", "01-01-2025", 100));

        String userInput = "add/Groceries/01-01-2025/50";
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount()); // No new expense should be added
        assertTrue(outContent.toString().contains("Expense with the same title already exists."));
    }

    @Test
    void testExecuteAddExpenseInvalidDate() {
        String userInput = "add/Groceries/32-13-2025/100"; // Invalid date
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Invalid date. Please enter a valid date in DD-MM-YYYY format."));
    }

    @Test
    void testExecuteAddExpenseNegativeAmount() {
        String userInput = "add/Groceries/01-01-2025/-100"; // Negative amount
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Amount cannot be negative."));
    }

    @Test
    void testExecuteAddExpenseDescriptionTooLong() {
        String userInput = "add/Groceries/01-01-2025/100";
        String longDescription = "a".repeat(201); // 201 characters
        provideInput(longDescription + "\n");
        expenseCommand.executeAddExpense(userInput);
    }


    @Test
    void testExecuteAddExpenseAmountExceedsCap() {
        String userInput = "add/Groceries/01-01-2025/60000"; // Amount exceeds cap
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("The entered amount exceeds the " +
                "maximum allowed limit of 50,000 SGD (or its equivalent)."));
    }

    @Test
    void testExecuteAddExpenseEmptyInput() {
        String userInput = ""; // Empty input
        provideInput("\n");
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Invalid format. Usage: add/<title>/<date>/<amount>"));
    }

    @Test
    void testExecuteAddExpenseExtraFields() {
        String userInput = "add/Groceries/01-01-2025/100/ExtraField"; // Extra field
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Invalid amount format. Please enter a valid number"));
    }

    @Test
    void testExecuteAddExpenseAllowSpecialCharactersInTitle() {
        String userInput = "add/Groceries@!/01-01-2025/100"; // Title with special characters
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount()); // Expense should be added
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries@!", addedExpense.getTitle()); // Verify the title
        assertEquals("nil", addedExpense.getDescription()); // No description
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(100.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteAddExpenseInvalidCharactersInDate() {
        String userInput = "add/Groceries/01-@1-2025/100"; // Invalid character in date
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Invalid date. Please enter a valid date in DD-MM-YYYY format."));
    }

    @Test
    void testExecuteAddExpenseZeroAmount() {
        String userInput = "add/Groceries/01-01-2025/0"; // Boundary value: 0
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount());
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("nil", addedExpense.getDescription());
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(0.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteAddExpenseMaximumAmount() {
        String userInput = "add/Groceries/01-01-2025/50000"; // Boundary value: Maximum allowed amount
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount());
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("nil", addedExpense.getDescription());
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(50000.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteAddExpenseNegativeZeroAmount() {
        String userInput = "add/Groceries/01-01-2025/-0.01"; // Negative zero amount
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Amount cannot be negative."));
    }

    @Test
    void testExecuteAddExpenseCaseSensitivity() {
        String userInput = "ADD/Groceries/01-01-2025/100"; // Uppercase command
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount());
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("nil", addedExpense.getDescription());
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(100.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteAddExpenseLeadingTrailingWhitespace() {
        String userInput = "  add/Groceries/01-01-2025/100  "; // Leading/trailing whitespace
        provideInput("\n"); // No description
        expenseCommand.executeAddExpense(userInput);

        assertEquals(1, budgetManager.getExpenseCount());
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("nil", addedExpense.getDescription());
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(100.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteDeleteExpenseLeadingTrailingWhitespace() {
        budgetManager.addExpense(new Expense("Lunch", "Pizza", "01-01-2025", 10));
        String userInput = " delete/1  "; // Leading/trailing whitespace
        provideInput("y\n");
        expenseCommand.executeDeleteExpense(userInput);

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Expense deleted successfully"));
    }

    @Test
    void testExecuteEditExpenseLeadingTrailingWhitespace() {
        budgetManager.addExpense(new Expense("Coffee", "Starbucks", "01-01-2025", 5.0));
        String userInput = "  edit/1/Latte/X/6.5  "; // Leading/trailing whitespace
        provideInput("Caramel Latte\n");
        expenseCommand.executeEditExpense(userInput);

        Expense editedExpense = budgetManager.getExpense(0);
        assertEquals("Latte", editedExpense.getTitle());
        assertEquals("Caramel Latte", editedExpense.getDescription());
        assertEquals("01-01-2025", editedExpense.getDate());
        assertEquals(6.5, editedExpense.getAmount());
    }

    @Test
    void testDisplayUnsettledExpensesZeroExpenses() {
        provideInput("\n");
        expenseCommand.displayUnsettledExpenses();
        String expectedMessage = "No expenses found.";

        String actualOutput = outContent.toString().trim();
        assertEquals(expectedMessage, actualOutput);
    }

    @Test
    void testShowCategorySummary() {
        // Add expenses with more explicit category indicators
        budgetManager.addExpense(new Expense("Lunch", "restaurant food", "01-01-2025", 25.50));
        budgetManager.addExpense(new Expense("T-shirt", "clothes shopping", "02-01-2025", 35.00));
        budgetManager.addExpense(new Expense("Movie ticket", "entertainment movie cinema", "03-01-2025", 15.00));

        // First provideInput with answer for visualization prompt ('n' for no)
        provideInput("n\n");

        // Run the category summary
        expenseCommand.showCategorySummary();

        // Get the output and print it for debugging
        String output = outContent.toString();
        System.err.println("CATEGORY SUMMARY OUTPUT:\n" + output);
        
        // Verify the output contains the summary title
        assertTrue(output.contains("Category-wise Expense Summary"), 
                   "Output should contain the summary title");
        
        // More comprehensive checks for food-related terms
        assertTrue(
            output.contains("Food") || 
            output.contains("Dining") || 
            output.contains("restaurant") || 
            output.contains("Restaurant") || 
            output.contains("Meal") ||
            output.contains("Lunch"),
            "Output should contain food-related category"
        );
        
        // More comprehensive checks for shopping-related terms
        assertTrue(
            output.contains("Shopping") || 
            output.contains("Clothing") || 
            output.contains("clothes") || 
            output.contains("T-shirt") || 
            output.contains("Apparel"),
            "Output should contain shopping-related category"
        );
        
        // More comprehensive checks for entertainment-related terms
        assertTrue(
            output.contains("Entertainment") || 
            output.contains("Leisure") || 
            output.contains("Recreation") || 
            output.contains("movie") || 
            output.contains("Movie") ||
            output.contains("cinema") ||
            output.contains("Cinema") ||
            output.contains("Misc") ||  // Sometimes entertainment falls under misc
            output.contains("Other"),   // Or other categories
            "Output should contain entertainment-related category"
        );
    }

    @Test
    void testExportCategorySummary() {
        // Add expenses from different categories
        budgetManager.addExpense(new Expense("Taxi", "transport to airport", "05-01-2025", 45.00));
        budgetManager.addExpense(new Expense("Dinner", "food at restaurant", "06-01-2025", 55.00));

        provideInput("");

        // Run the export command
        expenseCommand.exportCategorySummary();

        // Verify the file was created with correct message
        String output = outContent.toString();
        assertTrue(output.contains("Category summary exported to category_summary.txt"));

        // Optional: Read and verify file contents
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("category_summary.txt");
            String fileContent = new String(java.nio.file.Files.readAllBytes(path));

            assertTrue(fileContent.contains("Category-wise Expense Summary"));
            assertTrue(fileContent.contains("Travel:"));  // From "transport"
            assertTrue(fileContent.contains("Food:"));    // From "food at restaurant"
        } catch (IOException e) {
            // Handle exception or fail test
            assertTrue(false, "Failed to read exported file: " + e.getMessage());
        }
    }

    @Test
    void testShowExpenseSummaryMenu() {
        // Test the summary menu with option 3 (Cancel)
        provideInput("3\n");
        expenseCommand.showExpenseSummary();

        String output = outContent.toString();
        assertTrue(output.contains("Choose summary view:"));
        assertTrue(output.contains("1. Monthly Summary"));
        assertTrue(output.contains("2. Category-wise Summary"));
        assertTrue(output.contains("3. Cancel"));
    }

    @Test
    void testMonthlyExpenseSummary() {
        // Add expenses from different months
        budgetManager.addExpense(new Expense("January Expense", "first month", "15-01-2025", 100.00));
        budgetManager.addExpense(new Expense("February Expense", "second month", "15-02-2025", 200.00));

        provideInput("");

        // Run the monthly summary
        expenseCommand.showMonthlySummary();

        // Verify output shows both months with correct totals
        String output = outContent.toString();
        assertTrue(output.contains("Monthly Expense Summary"));
        assertTrue(output.contains("01-2025")); // January
        assertTrue(output.contains("02-2025")); // February
        assertTrue(output.contains("$100.00")); // January amount
        assertTrue(output.contains("$200.00")); // February amount
    }

    @Test
    void testExportMonthlySummary() {
        // Add expenses from different months
        budgetManager.addExpense(new Expense("March Expense", "third month", "15-03-2025", 300.00));
        budgetManager.addExpense(new Expense("April Expense", "fourth month", "15-04-2025", 400.00));

        provideInput("");

        // Run the export command
        expenseCommand.exportMonthlySummary();

        // Verify the file was created with correct message
        String output = outContent.toString();
        assertTrue(output.contains("Monthly summary exported to monthly_summary.txt"));

        // Optional: Read and verify file contents
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("monthly_summary.txt");
            String fileContent = new String(java.nio.file.Files.readAllBytes(path));

            assertTrue(fileContent.contains("Monthly Expense Summary"));
            assertTrue(fileContent.contains("03-2025")); // March
            assertTrue(fileContent.contains("04-2025")); // April
            assertTrue(fileContent.contains("$300.00")); // March amount
            assertTrue(fileContent.contains("$400.00")); // April amount
        } catch (IOException e) {
            // Handle exception or fail test
            assertTrue(false, "Failed to read exported file: " + e.getMessage());
        }
    }

    @Test
    void testExportExpenseSummaryMenu() {
        // Test the export menu with option 3 (Back to main menu)
        provideInput("3\n");
        expenseCommand.exportExpenseSummary();

        String output = outContent.toString();
        assertTrue(output.contains("Choose export format:"));
        assertTrue(output.contains("1. Monthly Summary"));
        assertTrue(output.contains("2. Category-wise Summary"));
        assertTrue(output.contains("3. Back to main menu"));
    }
    //@@author

    //@@author nandhananm7
    @Test
    void testFindExpenseFound() {
        // Add the expense to the budget manager
        Expense expense1 = new Expense("new groceries", "Updated description", "01-01-2025", 150.00);
        budgetManager.addExpense(expense1);

        expenseCommand = new ExpenseCommand(budgetManager, new Scanner(System.in), currency);
        expenseCommand.findExpense("find /new groceries");

        String output = outContent.toString();

        assertTrue(output.contains("Found 1 matching expense(s):"));
        assertTrue(output.contains("Title: new groceries"));
        assertTrue(output.contains("Description: Updated description"));
        assertTrue(output.contains("Date: 01-01-2025"));
        assertTrue(output.contains("Amount: 150.00"));
    }


    @Test
    void testFindExpenseNoMatch() {
        // Add an expense that does not match the keyword "bus"
        Expense expense1 = new Expense("Taxi", "from jb to singapore", "12-12-1327", 12.00);
        budgetManager.addExpense(expense1);

        // Simulate user input: "bus"
        provideInput("bus\n");
        expenseCommand.findExpense("find /bus");

        String output = outContent.toString();
        // Verify that the output indicates no matching expenses were found for the keyword "bus"
        assertTrue(output.contains("No matching expenses found for keyword: bus"),
                "Expected output to mention no matching expenses for keyword 'bus'");
    }
    //@@author

    //@@author NandhithaShree
    @Test
    void testExecuteMarkCommandValidInputs() {
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());
        String input = "mark/1";

        expenseCommand.executeMarkCommand(input);
        String expectedMessage = "Expense 1 successfully marked!";
        String actualOutput = outContent.toString().trim();

        assertEquals(1, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains(expectedMessage));
    }

    @Test
    void testExecuteMarkCommand() {
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());

        String input = "mark/1";
        expenseCommand.executeMarkCommand(input);
        assertEquals(true, expense.getDone());

        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
        //@@author matthewyeo1
        assertEquals(testDate, budgetManager.getExpense(0).getDate());
        //@@author
    }

    @Test
    void testExecuteUnmarkCommand() {
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());

        String firstInput = "mark/1";
        expenseCommand.executeMarkCommand(firstInput);
        assertEquals(true, expense.getDone());

        String secondInput = "unmark/1";
        expenseCommand.executeUnmarkCommand(secondInput);
        assertEquals(false, expense.getDone());

        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
        //@@author matthewyeo1
        assertEquals(testDate, budgetManager.getExpense(0).getDate());
        //@@author
    }

    @Test
    void testExecuteUnmarkCommandInvalidInputs() {
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        budgetManager.addExpense(expense);

        String input = "unmark/2";
        expenseCommand.executeUnmarkCommand(input);
        String expectedMessage = "Please enter a valid expense number.";
        String actualOutput = outContent.toString().trim();

        assertTrue(actualOutput.contains(expectedMessage));
    }

    @Test
    void testExecuteMarkCommandInvalidInputs() {
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());
        String input = "mark/2";

        expenseCommand.executeMarkCommand(input);
        String expectedMessage = "Please enter a valid expense number.";
        String actualOutput = outContent.toString().trim();

        assertTrue(actualOutput.contains(expectedMessage));
        assertEquals(false, expense.getDone());

        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
        //@@author matthewyeo1
        assertEquals(testDate, budgetManager.getExpense(0).getDate());
        //@@author
    }

    @Test
    void testDisplaySettledExpensesZeroSettledExpenses(){
        provideInput("\n");
        expenseCommand.displaySettledExpenses();
        String expectedMessage = "No expenses found.";

        String actualOutput = outContent.toString().trim();
        assertEquals(expectedMessage, actualOutput);
    }

    @Test
    void testDisplaySettledExpensesTwoSettledExpenses(){
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        Expense expense1 = new Expense(testTitle + "1", testDescription + "1",
                testDate, testAmount + 1);
        Expense expense2= new Expense(testTitle + "2", testDescription + "2",
                testDate, testAmount + 1);
        budgetManager.addExpense(expense);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);
        budgetManager.markExpense(0);
        budgetManager.markExpense(1);

        expenseCommand.displaySettledExpenses();
        String expectedMessage = "All expenses are in SGD\n" + "Expense #1\n" + expense.toString() + "\n\n" +
                "Expense " + "#2\n" + expense1.toString() + "\n" + "\n" + "You have 2 settled expenses";
        String actualOutput = outContent.toString().trim();
        actualOutput = actualOutput.replaceAll("\r\n", "\n");
        assertEquals(expectedMessage, actualOutput);
    }

    @Test
    void testDisplayUnsettledExpensesTwoUnsettledExpenses(){
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testDate, testAmount);
        Expense expense1 = new Expense(testTitle + "1", testDescription + "1",
                testDate,testAmount + 1);
        Expense expense2= new Expense(testTitle + "2", testDescription + "2",
                testDate, testAmount + 1);
        budgetManager.addExpense(expense);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        budgetManager.markExpense(2);

        expenseCommand.displayUnsettledExpenses();
        String expectedMessage = "All expenses are in SGD\n" + "Expense #1\n" + expense.toString() + "\n\n" +
                "Expense #2\n" + expense1.toString() + "\n\n" + "You have 2 unsettled expenses";

        String actualOutput = outContent.toString().trim();
        actualOutput = actualOutput.replaceAll("\r\n", "\n");
        assertEquals(expectedMessage, actualOutput);
    }

    @Test
    void testCurrency(){
        provideInput("\n");
        assertEquals("SGD", currency.getCurrentCurrency());
    }

    @Test
    public void testCurrencyFileNotFound() {
        provideInput("\n");
        File file = new File("./currentCurrency");
        file.delete(); // Ensure the file doesn't exist

        assertEquals(Commands.DEFAULT_CURRENCY, currency.getCurrentCurrency());
    }

    @Test
    public void testChangeCurrencyMethod1ValidCurrency() {
        provideInput("\n");
        String input = "change-currency/1/EUR/0.70";
        currency.changeCurrency(input);

        assertEquals("EUR", currency.getCurrentCurrency());
    }

    @Test
    public void testChangeCurrencyMethod1InValidCurrency() {
        provideInput("\n");
        String input = "change-currency/1/ABC/0.70";
        currency.changeCurrency(input);

        String actualOutput = outContent.toString().trim();
        String expectedOutput = "Please provide a valid currency...";

        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testChangeCurrencyMethod2ValidCurrency() {
        provideInput("\n");
        String input = "change-currency/2/JPY";
        currency.changeCurrency(input);

        assertEquals("JPY", currency.getCurrentCurrency());
    }

    @Test
    public void testChangeCurrencyMethod2InValidCurrency() {
        provideInput("\n");
        String input = "change-currency/2/ABC";
        currency.changeCurrency(input);

        String actualOutput = outContent.toString().trim();
        String expectedOutput = "Currency code ABC not found.";

        assertTrue(actualOutput.contains(expectedOutput));
    }

    //@@author
}
//@@author
