package seedu.duke.commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;

public class ExpenseCommandTest {
    private BudgetManager budgetManager;
    private ExpenseCommand expenseCommand;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        budgetManager = new BudgetManager();
        scanner = new Scanner(System.in);
        expenseCommand = new ExpenseCommand(budgetManager, scanner);
    }

    @Test
    public void executeAddExpense_validInput_expenseAdded() {
        // Arrange
        String input = "Lunch\nTeam lunch\n25.50\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        scanner = new Scanner(System.in);
        expenseCommand = new ExpenseCommand(budgetManager, scanner);
        int initialSize = budgetManager.getAllExpenses().size();
        
        // Act
        expenseCommand.executeAddExpense();
        
        // Assert
        assertEquals(initialSize + 1, budgetManager.getAllExpenses().size());
        Expense addedExpense = budgetManager.getAllExpenses().get(0);
        assertEquals("Lunch", addedExpense.getTitle());
        assertEquals("Team lunch", addedExpense.getDescription());
        assertEquals(25.50, addedExpense.getAmount());
        
        // Using Java assert
        assert budgetManager.getAllExpenses().size() > initialSize : "Expense was not added";
    }

    @Test
    public void executeEditExpense_validInput_expenseEdited() {
        // Arrange - First add an expense
        budgetManager.addExpense(new Expense("Test", "Description", 10.0));
        String input = "1\nEdited Title\nEdited Description\n20.0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        scanner = new Scanner(System.in);
        expenseCommand = new ExpenseCommand(budgetManager, scanner);
        
        // Act
        expenseCommand.executeEditExpense();
        
        // Assert
        Expense editedExpense = budgetManager.getAllExpenses().get(0);
        assertEquals("Edited Title", editedExpense.getTitle());
        assertEquals("Edited Description", editedExpense.getDescription());
        assertEquals(20.0, editedExpense.getAmount());
        
        // Using Java assert
        assert "Edited Title".equals(editedExpense.getTitle()) : "Title was not edited correctly";
        assert 20.0 == editedExpense.getAmount() : "Amount was not edited correctly";
    }

    @Test
    public void executeDeleteExpense_validInput_expenseDeleted() {
        // Arrange - First add an expense
        budgetManager.addExpense(new Expense("To Delete", "Will be deleted", 15.0));
        int initialSize = budgetManager.getAllExpenses().size();
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        scanner = new Scanner(System.in);
        expenseCommand = new ExpenseCommand(budgetManager, scanner);
        
        // Act
        expenseCommand.executeDeleteExpense();
        
        // Assert
        assertEquals(initialSize - 1, budgetManager.getAllExpenses().size());
        
        // Using Java assert
        assert budgetManager.getAllExpenses().size() < initialSize : "Expense was not deleted";
    }

    @Test
    public void executeEditExpense_invalidIndex_noChange() {
        // Arrange - First add an expense
        budgetManager.addExpense(new Expense("Original", "Original Desc", 10.0));
        String input = "999\n"; // Invalid index
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        scanner = new Scanner(System.in);
        expenseCommand = new ExpenseCommand(budgetManager, scanner);
        
        // Act
        expenseCommand.executeEditExpense();
        
        // Assert
        Expense unchangedExpense = budgetManager.getAllExpenses().get(0);
        assertEquals("Original", unchangedExpense.getTitle());
        
        // Using Java assert
        assert "Original".equals(unchangedExpense.getTitle()) : "Expense was incorrectly modified";
    }
} 