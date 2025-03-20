package seedu.duke.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.expense.Expense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//@@author matthewyeo1
class DataStorageTest {
    public static final String TEST_DATA_FILE = "test_expenses.txt";

    @BeforeEach
    void setUp() {
        DataStorage.dataFile = TEST_DATA_FILE;
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_DATA_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testEnsureFileExists() {
        DataStorage.ensureFileExists();
        File file = new File(TEST_DATA_FILE);
        assertTrue(file.exists(), "File should be created");
    }

    @Test
    void testSaveAndLoadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Lunch", "Fast food", 10.50));
        expenses.add(new Expense("Transport", "Bus fare", 2.00));

        assertTrue(DataStorage.saveExpenses(expenses), "Saving should be successful");

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertEquals(2, loadedExpenses.size(), "Should load 2 expenses");

        assertEquals("Lunch", loadedExpenses.get(0).getTitle());
        assertEquals("Fast food", loadedExpenses.get(0).getDescription());
        assertEquals(10.50, loadedExpenses.get(0).getAmount());

        assertEquals("Transport", loadedExpenses.get(1).getTitle());
        assertEquals("Bus fare", loadedExpenses.get(1).getDescription());
        assertEquals(2.00, loadedExpenses.get(1).getAmount());
    }

    @Test
    void testLoadExpensesFromNonExistingFile() {
        File file = new File(TEST_DATA_FILE);
        if (file.exists()) {
            file.delete();
        }

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertTrue(loadedExpenses.isEmpty(), "Should return an empty list");
    }
}
//@@author
