package seedu.duke.expense;

/**
 * Represents an individual expense with title, description, and amount.
 */
public class Expense {
    private String title;
    private String description;
    private double amount;
    private Boolean isDone;
    private String groupName;

    /**
     * Constructs an Expense object with the given title, description, and amount.
     *
     * @param title       A short name or summary of the expense
     * @param description Detailed information about the expense
     * @param amount      The monetary value of the expense
     */
    public Expense(String title, String description, double amount) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.isDone = false;
    }

    /**
     * Creates a new Expense with the specified title, description, amount, and completion status.
     *
     * @param title       A short name or summary of the expense
     * @param description Detailed information about the expense
     * @param amount      The monetary value of the expense.
     * @param isDone      Indicates whether the expense is settled
     */
    public Expense(String title, String description, double amount, boolean isDone) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.isDone = isDone;
    }

    /**
     * Returns the title of the expense.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the expense.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the expense.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the expense.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the amount of the expense.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense.
     *
     * @param amount the new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns a string representation of the expense.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Title: " + title + "\n"
                + "Description: " + description + "\n"
                + "Amount: $" + String.format("%.2f", amount);
    }

    /**
     * Returns whether the expense is marked as completed.
     *
     * @return {@code true} if the expense is completed, {@code false} otherwise.
     */
    public Boolean getDone() {
        return isDone;
    }

    /**
     * Sets the completion status of the expense.
     *
     * @param isDone {@code true} to mark the expense as completed, {@code false} otherwise.
     */
    public void setDone(Boolean isDone) {
        this.isDone = isDone;
    }

    //@@author matthewyeo1
    /**
     * Gets the group name associated with the expense.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the group name associated with the expense.
     *
     * @param groupName the group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    //@@author
}
