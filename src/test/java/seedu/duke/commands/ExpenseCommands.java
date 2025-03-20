package seedu.duke.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;

//@@author matthewyeo1
class ExpenseCommandTest {
    private BudgetManager budgetManager;
    private ExpenseCommand expenseCommand;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private final String testTitle = "Test Expense";
    private final String testDescription = "Test Description";
    private final double testAmount = 100.0;

    @BeforeEach
    void setUp() {
        budgetManager = new BudgetManager();
        outContent = new ByteArrayOutputStream();
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
    }

    void provideInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        expenseCommand = new ExpenseCommand(budgetManager, scanner);
    }

    @Test
    void testExecuteAddExpense() {
        provideInput("Groceries\nWeekly food shopping\n100\n");

        expenseCommand.executeAddExpense();
        assertEquals(1, budgetManager.getExpenseCount());

        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Groceries", addedExpense.getTitle());
        assertEquals("Weekly food shopping", addedExpense.getDescription());
        assertEquals(100.0, addedExpense.getAmount());
    }

    @Test
    void testExecuteDeleteExpense() {
        budgetManager.addExpense(new Expense("Lunch", "Pizza", 10));

        provideInput("1\n");
        expenseCommand.executeDeleteExpense();

        assertEquals(0, budgetManager.getExpenseCount());
        assertTrue(outContent.toString().contains("Expense deleted successfully"));
    }

    @Test
    void testExecuteEditExpense() {
        budgetManager.addExpense(new Expense("Coffee", "Starbucks", 5.0));

        provideInput("1\nLatte\nCaramel Latte\n6.5\n");
        expenseCommand.executeEditExpense();

        Expense editedExpense = budgetManager.getExpense(0);
        assertEquals("Latte", editedExpense.getTitle());
        assertEquals("Caramel Latte", editedExpense.getDescription());
        assertEquals(6.5, editedExpense.getAmount());
    }

    @Test
    void testInvalidInputHandling() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        provideInput("Test Expense\nDescription\nInvalid\n");

        expenseCommand.executeAddExpense();

        String expectedMessage = "Invalid amount format. Please enter a valid number.";

        String actualOutput = outContent.toString().trim();

        assertTrue(actualOutput.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + "\nBut found: " + actualOutput);
    }
    //@@author

    //@@author NandhithaShree
    @Test
    void testExecuteMarkCommand() {
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());

        provideInput("1\n");
        expenseCommand.executeMarkCommand();
        assertEquals(true, expense.getDone());

        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
    }

    @Test
    void testExecuteUnmarkCommand() {
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());

        provideInput("1\n");
        expenseCommand.executeMarkCommand();
        assertEquals(true, expense.getDone());

        provideInput("1");
        expenseCommand.executeUnmarkCommand();
        assertEquals(false, expense.getDone());

        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
    }

    @Test
    void testExecuteMarkCommandInvalidInputs() {
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);
        assertEquals(false, expense.getDone());
        provideInput("2\n");

        expenseCommand.executeMarkCommand();
        String expectedMessage = "Please enter a valid expense number.";
        String actualOutput = outContent.toString().trim();

        assertTrue(actualOutput.contains(expectedMessage));
        assertEquals(false, expense.getDone());

        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
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
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        Expense expense1 = new Expense(testTitle + "1", testDescription + "1", testAmount + 1);
        Expense expense2= new Expense(testTitle + "2", testDescription + "2", testAmount + 1);
        budgetManager.addExpense(expense);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);
        budgetManager.markExpense(0);
        budgetManager.markExpense(1);

        expenseCommand.displaySettledExpenses();
        String expectedMessage = "Expense #1\n" + expense.toString() + "\n\n" + "Expense #2\n" + expense1.toString()
                + "\n\n" + "List of Settled Expenses:" + "\n" + "You have 2 settled expenses";
        String actualOutput = outContent.toString().trim();
        actualOutput = actualOutput.replaceAll("\r\n", "\n");
        assertEquals(expectedMessage, actualOutput);
    }

    void testDisplayUnsettledExpensesTwoUnsettledExpenses(){
        provideInput("\n");
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        Expense expense1 = new Expense(testTitle + "1", testDescription + "1", testAmount + 1);
        Expense expense2= new Expense(testTitle + "2", testDescription + "2", testAmount + 1);
        budgetManager.addExpense(expense);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);
        budgetManager.markExpense(3);

        expenseCommand.displayUnsettledExpenses();
        String expectedMessage = "Expense #1\n" + expense.toString() + "\n\n" + "Expense #2\n" + expense1.toString()
                + "\n\n" + "List of Unsettled Expenses:" + "\n" + "You have 2 unsettled expenses";
        String actualOutput = outContent.toString().trim();
        actualOutput = actualOutput.replaceAll("\r\n", "\n");
        assertEquals(expectedMessage, actualOutput);
    }
    //@@author

    //@@author mohammedhamdhan
    @Test
    void testExecuteEditExpenseInvalidAmount() {
        // Prepare test data - add an expense first
        Expense expense = new Expense("Original", "Original Description", 50.0);
        budgetManager.addExpense(expense);
        
        // Provide invalid amount input
        provideInput("1\nUpdated Title\nUpdated Description\ninvalid\n");
        expenseCommand.executeEditExpense();
        
        // Check that original amount is kept
        Expense editedExpense = budgetManager.getExpense(0);
        assertEquals("Updated Title", editedExpense.getTitle());
        assertEquals("Updated Description", editedExpense.getDescription());
        assertEquals(50.0, editedExpense.getAmount());
        
        // Check output message
        String actualOutput = outContent.toString().trim();
        assertTrue(actualOutput.contains("Invalid amount format. Keeping original amount."));
    }
    
    @Test
    void testShowBalanceOverview() {
        // Prepare test data with unsettled expenses
        budgetManager.addExpense(new Expense("Expense 1", "Description 1", 25.0));
        budgetManager.addExpense(new Expense("Expense 2", "Description 2", 35.0));
        
        // Call the method
        provideInput("");
        expenseCommand.showBalanceOverview();
        
        // Check output
        String actualOutput = outContent.toString().trim();
        assertTrue(actualOutput.contains("Balance Overview"));
        assertTrue(actualOutput.contains("Total number of unsettled expenses: 2"));
        assertTrue(actualOutput.contains("Total amount owed: $60.00"));
    }
    
    @Test
    void testDisplayAllExpensesEmpty() {
        // Clear any existing expenses
        while (budgetManager.getExpenseCount() > 0) {
            budgetManager.deleteExpense(0);
        }
        
        // Test display with empty list
        provideInput("");
        expenseCommand.displayAllExpenses();
        
        // Check output
        String actualOutput = outContent.toString().trim();
        assertEquals("No expenses found.", actualOutput);
    }
    
    @Test
    void testDisplayAllExpenses() {
        // Add test expenses
        Expense expense1 = new Expense("Coffee", "Morning coffee", 5.0);
        Expense expense2 = new Expense("Lunch", "Business lunch", 15.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);
        
        // Display all expenses
        provideInput("");
        expenseCommand.displayAllExpenses();
        
        // Check output contains both expenses
        String actualOutput = outContent.toString().trim();
        actualOutput = actualOutput.replaceAll("\r\n", "\n");
        
        assertTrue(actualOutput.contains("List of Expenses:"));
        assertTrue(actualOutput.contains("Expense #1"));
        assertTrue(actualOutput.contains("Coffee"));
        assertTrue(actualOutput.contains("Morning coffee"));
        assertTrue(actualOutput.contains("$5.0"));
        assertTrue(actualOutput.contains("Expense #2"));
        assertTrue(actualOutput.contains("Lunch"));
        assertTrue(actualOutput.contains("Business lunch"));
        assertTrue(actualOutput.contains("$15.0"));
    }
    //@@author
}

