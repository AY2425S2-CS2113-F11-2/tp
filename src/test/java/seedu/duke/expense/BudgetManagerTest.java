package seedu.duke.expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.duke.storage.DataStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
//@@author matthewyeo1
public class BudgetManagerTest {

    private BudgetManager budgetManager;
    private final String testTitle = "Test Expense";
    private final String testDescription = "Test Description";
    private final double testAmount = 100.0;

    @BeforeEach
    void setUp() {
        DataStorage.resetExpenses();
        budgetManager = new BudgetManager();
    }

    @AfterEach
    void tearDown() {
        DataStorage.resetExpenses();
    }

    @Test
    void testAddExpense() {
        Expense expense = new Expense(testTitle, testDescription, testAmount);

        budgetManager.addExpense(expense);

        assertEquals(1, budgetManager.getExpenseCount());
        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
    }

    @Test
    void testDeleteExpense() {

        Expense expense1 = new Expense("Expense 1", "Description 1", 50.0);
        Expense expense2 = new Expense("Expense 2", "Description 2", 30.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        Expense deletedExpense = budgetManager.deleteExpense(0);

        assertEquals(1, budgetManager.getExpenseCount());
        assertEquals(expense1, deletedExpense);
        assertEquals("Expense 2", budgetManager.getExpense(0).getTitle());
    }

    @Test
    void testEditExpense() {

        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);

        Expense editedExpense = budgetManager.editExpense(0, "Updated Title", "Updated Description", 200.0);

        assertEquals("Updated Title", editedExpense.getTitle());
        assertEquals("Updated Description", editedExpense.getDescription());
        assertEquals(200.0, editedExpense.getAmount());
    }

    @Test
    void testGetTotalBalance() {

        Expense expense1 = new Expense("Expense 1", "Description 1", 50.0);
        Expense expense2 = new Expense("Expense 2", "Description 2", 100.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        double totalBalance = budgetManager.getTotalBalance();

        assertEquals(150.0, totalBalance);
    }

    @Test
    void testGetAllExpenses() {

        Expense expense1 = new Expense("Expense 1", "Description 1", 50.0);
        Expense expense2 = new Expense("Expense 2", "Description 2", 100.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        List<Expense> expenses = budgetManager.getAllExpenses();

        assertEquals(2, expenses.size());
        assertTrue(expenses.contains(expense1));
        assertTrue(expenses.contains(expense2));
    }

    @Test
    void testDeleteExpenseOutOfRange() {

        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);

        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.deleteExpense(1));
    }

    @Test
    void testEditExpenseOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> budgetManager.editExpense(1,
                        "New Title", "New Description",
                        200.0));
    }

    @Test
    void testGetExpenseOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.getExpense(1));
    }
    //@@author

    //@@author NandhithaShree
    @Test
    void testMark(){
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        assertEquals(expense.getDone(), false);
        budgetManager.addExpense(expense);
        int testIndex = 0;

        budgetManager.markExpense(testIndex);
        assertEquals(expense.getDone(), true);
        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
    }

    @Test
    void testUnmark(){
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);
        int testIndex = 0;
        assertEquals(expense.getDone(), false);

        budgetManager.markExpense(testIndex);
        assertEquals(expense.getDone(), true);

        budgetManager.unmarkExpense(testIndex);
        assertEquals(expense.getDone(), false);
        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDescription, budgetManager.getExpense(0).getDescription());
    }

    @Test
    void testMarkExpenseOutOfRange(){
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);

        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.markExpense(1));
    }

    @Test
    void testUnmarkExpenseOutOfRange(){
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);

        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.unmarkExpense(1));
    }

    @Test
    void testgetUnsettledExpensesCountZeroUnsettled(){
        int numberOfUnsettledExpenses = 0;

        assertEquals(budgetManager.getUnsettledExpenseCount(), numberOfUnsettledExpenses);
    }

    @Test
    void testgetUnsettledExpensesCountOneUnsettled(){
        Expense expense = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense);
        int numberOfUnsettledExpenses = 1;

        assertEquals(budgetManager.getUnsettledExpenseCount(), numberOfUnsettledExpenses);
    }

    @Test
    void testGetUnsettledExpensesCountMultipleUnsettled(){
        Expense expense1 = new Expense(testTitle, testDescription, testAmount);
        Expense expense2 = new Expense(testTitle, testDescription, testAmount);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);
        int numberOfUnsettledExpenses = 2;

        assertEquals(budgetManager.getUnsettledExpenseCount(), numberOfUnsettledExpenses);
    }
    //@@author
}


