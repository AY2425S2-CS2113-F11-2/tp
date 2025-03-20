package seedu.duke.expense;

import java.util.ArrayList;
import java.util.List;

import seedu.duke.messages.Messages;
import seedu.duke.storage.DataStorage;

/**
 * Manages a collection of expenses and provides operations on them.
 */
public class BudgetManager {
    Messages messages = new Messages();
    private List<Expense> expenses;

    /**
     * Constructs a BudgetManager with an empty list of expenses.
     */
    public BudgetManager() {
        this.expenses = DataStorage.loadExpenses();
    }

    /**
     * Adds an expense to the collection.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
        DataStorage.saveExpenses(expenses);
    }

    /**
     * Deletes an expense at the specified index.
     *
     * @param index the index of the expense to delete
     * @return the deleted expense
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Expense deleteExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        Expense deletedExpense = expenses.remove(index);
        DataStorage.saveExpenses(expenses);
        return deletedExpense;
    }

    /**
     * Edits an expense at the specified index.
     *
     * @param index   the index of the expense to edit
     * @param title   the new title (null to keep existing)
     * @param description the new description (null to keep existing)
     * @param amount  the new amount (negative to keep existing)
     * @return the edited expense
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Expense editExpense(int index, String title, String description, double amount) 
            throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        
        Expense expense = expenses.get(index);
        
        if (title != null) {
            expense.setTitle(title);
        }
        
        if (description != null) {
            expense.setDescription(description);
        }
        
        if (amount >= 0) {
            expense.setAmount(amount);
        }
        
        DataStorage.saveExpenses(expenses);
        return expense;
    }

    /**
     * Gets all expenses.
     *
     * @return the list of expenses
     */
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * Gets the number of expenses.
     *
     * @return the number of expenses
     */
    public int getExpenseCount() {
        return expenses.size();
    }

    /**
     * Gets the number of unsettled expenses.
     *
     * @return the number of unsettled expenses
     */
    public int getUnsettledExpenseCount() {
        int numberOfUnsettledExpenses = 0;
        for (Expense expense : expenses) {
            if (!expense.getDone()) {
                numberOfUnsettledExpenses++;
            }
        }
        return numberOfUnsettledExpenses;
    }

    /**
     * Calculates the total balance (sum of all unsettled expense amounts).
     *
     * @return the total balance
     */
    public double getTotalBalance() {
        double total = 0;
        for (Expense expense : expenses) {
            if(!expense.getDone()) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    /**
     * Gets an expense at the specified index.
     *
     * @param index the index of the expense to get
     * @return the expense at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Expense getExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        return expenses.get(index);
    }

    /**
     * Marks an expense at the specified index.
     *
     * @param index the index of the expense to mark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        expenses.get(index).setDone(true);
        saveAllExpenses();
    }

    /**
     * Unmarks an expense at the specified index.
     *
     * @param index the index of the expense to unmark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void unmarkExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        expenses.get(index).setDone(false);
        saveAllExpenses();
    }

    /**
     * Saves all expenses to storage.
     */
    public void saveAllExpenses() {
        DataStorage.saveExpenses(expenses);
    }

    /**
     * Checks if the given index is valid.
     *
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    public boolean isValidIndex(int index) {
        return index > 0 && index <= expenses.size();
    }

    /**
     * Gets an expense by index (1-based).
     *
     * @param index the 1-based index of the expense to get
     * @return the expense at the specified index
     */
    public Expense getExpenseByIndex(int index) {
        return expenses.get(index - 1);
    }

    /**
     * Removes an expense at the specified index (1-based).
     *
     * @param index the 1-based index of the expense to remove
     * @return the removed expense
     */
    public Expense removeExpense(int index) {
        Expense removedExpense = expenses.remove(index - 1);
        saveAllExpenses();
        return removedExpense;
    }

    /**
     * Updates an expense at the specified index (1-based).
     *
     * @param index       the 1-based index of the expense to update
     * @param title       the new title
     * @param description the new description
     * @param amount      the new amount
     * @return the updated expense
     */
    public Expense updateExpense(int index, String title, String description, double amount) {
        Expense expense = getExpenseByIndex(index);
        expense.setTitle(title);
        expense.setDescription(description);
        expense.setAmount(amount);
        saveAllExpenses();
        return expense;
    }
} 
