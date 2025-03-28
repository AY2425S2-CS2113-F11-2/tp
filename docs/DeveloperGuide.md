# Developer Guide

---

## Table of Contents

---

[1. Introduction](#1-introduction) <br>
[2. Setup Guide](#2-setup-guide) <br>
&nbsp;&nbsp;[2.1 Prerequisites](#21-prerequisites) <br>
[3. Design](#3-design) <br>
&nbsp;&nbsp;[3.1 UI Class](#31-ui) <br>
&nbsp;&nbsp;[3.2 DataStorage Class](#32-datastorage-class) <br>
&nbsp;&nbsp;[3.3 GroupStorage Class](#33-groupstorage-class) <br>
&nbsp;&nbsp;[3.4 Commands Class](#34-commands-class) <br>
&nbsp;&nbsp;[3.5 ExpenseCommands Classes](#35-expensecommands-classes) <br>
&nbsp;&nbsp;[3.6 FriendsCommands Class](#36-friendscommands-income-and-incomemanager-class) <br>
&nbsp;&nbsp;[3.7 SplitCommand Class](#37-splitcommand-class) <br>
&nbsp;&nbsp;[3.8 BudgetManager Class](#38-budgetmanager-class) <br>
&nbsp;&nbsp;[3.9 Expense Class](#39-expense-class) <br>
&nbsp;&nbsp;[4.0 Friend Class](#40-friend-class) <br>
&nbsp;&nbsp;[4.1 Group Class](#41-group-class) <br>
&nbsp;&nbsp;[4.2 GroupManager Class](#41-groupmanager-class) <br>
&nbsp;&nbsp;[4.3 HelpPage Class](#42-helppage-class) <br>
&nbsp;&nbsp;[4.4 Messages Class](#43-messages-class) <br>
&nbsp;&nbsp;[4.5 ExpenseClassifier Class](#44-expenseclassifier-class) <br>
&nbsp;&nbsp;[4.6 Currency Class](#45-currency-class) <br>


## Acknowledgements

---

## 1. Introduction

---

O\$P\$ is an expense-tracker application that allows
the user to keep track of their expenditure with ease!
Users can also manage expenses in a group setting with
simple splitting functions, as well as view analytics
of their spending across multiple categories.

## 2. Setup Guide

---

### 2.1 Prerequisites

1.  Ensure you have Java 17 or above installed on your PC. **[Version 17 is preferred]**
2.  You may download [here](https://se-education.org/guides/tutorials/javaInstallationMac.html) for Mac users and [here](https://www.oracle.com/sg/java/technologies/downloads/) for Windows users.
3.  If you have it installed already, you may check it by running `java -version` in your terminal.
4.  Download the latest `.jar` file from here. **[link will be updated once v1 is ready]**
5.  Copy the file to the folder you want to use as the home folder for your **O$P$ budget tracking app** 🙂.
6.  Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar oSpS.jar` command to run the application.
7.  Type the command in the command box and press **Enter** to execute it.
8.  **Example:** Typing `help` and pressing **Enter** will open a mini window showing a list of all possible commands.
9.  Refer to the [features](https://docs.google.com/document/d/125Cg7wzuc4XFo3wsziwL2f64KN1uUfvFL5dIm6IQrSk/edit?tab=t.xl7ogrtj0a5q#heading=h.61o02m6y9xrc) section below for details on all commands and functionalities.

## 3. Design

---

### 3.1 UI Class

### 3.2 DataStorage Class

### 3.3 GroupStorage Class

### 3.4 Commands Class

### 3.5 ExpenseCommands Classes

The ExpenseCommand class handles all expense-related operations in the application. It provides functionality for adding, deleting, editing, and managing expenses.

#### Adding Expenses

The `executeAddExpense()` method handles the addition of new expenses with the following features:

- Validates input fields (title, description, date, amount)
- Ensures date format is DD-MM-YYYY
- Prevents negative amounts
- Handles empty inputs gracefully
- Uses assertions to validate state

#### Deleting Expenses

The `executeDeleteExpense()` method manages expense deletion with these features:

- Validates expense index before deletion
- Updates owed amounts in the owesData.txt file
- Handles invalid indices gracefully
- Uses assertions to ensure valid state


#### Displaying Settled Expenses

The `displaySettledExpenses()` method manages the display of settled expenses with these features:

- Retrieves all expenses from budgetManager before filtering
- Checks for empty expense lists and displays an appropriate message
- Prints the current currency to ensure user is aware which currency the amounts are printed in
- Iterates through the expense list while filtering for settled expenses
- Handles edge cases gracefully, such as no settled expenses found
- Uses proper singular/plural formatting for the summary output

#### Displaying Unsettled Expenses

The `displayUnsettledExpenses()` method manages the display of unsettled expenses with these features:

- Retrieves all expenses from budgetManager before filtering
- Checks for empty expense lists and displays an appropriate message
- Ensures expenses are printed in the correct currency
- Iterates through the expense list while filtering for unsettled expenses
- Handles edge cases gracefully, such as no unsettled expenses found
- Uses proper singular/plural formatting for the summary output

#### Marking Expenses

The `executeMarkCommand()` method manages marking an expense as settled with these features:

- Prompts the user to enter an expense number for marking
- Trims and parses user input to handle potential formatting issues
- Attempts to mark the specified expense using budgetManager.markExpense()
- Handles invalid indices gracefully with an IndexOutOfBoundsException
- Handles non-numeric input with a NumberFormatException
- Provides clear error messages to guide the user toward valid input

#### Unmarking Expenses

The `executeUnmarkCommand()` method manages unmarking an expense as unsettled with these features:

- Prompts the user to enter an expense number for unmarking
- Trims and parses user input to handle potential formatting issues
- Attempts to unmark the specified expense using budgetManager.unmarkExpense()
- Handles invalid indices gracefully with an IndexOutOfBoundsException
- Handles non-numeric input with a NumberFormatException
- Provides clear error messages to guide the user toward valid input

#### Expense Summaries

The class provides comprehensive expense summary functionality:

1. Viewing Summaries:

   - Monthly Summary: Shows total expenses and count per month
   - Category-wise Summary: Shows total expenses and count per category
   - Interactive menu for choosing summary view
   - Formatted output with clear headers and separators

2. Exporting Summaries:

   - Export to text files (monthly_summary.txt, category_summary.txt)
   - Same formatting as view summaries
   - Error handling for file operations
   - Confirmation messages for successful exports

3. Data Organization:
   - Uses Maps to group and aggregate expenses
   - Maintains both totals and counts
   - Sorts data for better readability
   - Handles empty expense lists gracefully

#### Error Handling

Both methods implement comprehensive error handling:

- NumberFormatException for invalid number inputs
- IndexOutOfBoundsException for invalid indices
- FileNotFoundException for file operations
- General exception handling with user-friendly messages

#### Data Validation

The class uses assertions to validate:

- Non-null objects
- Valid indices
- Non-negative amounts
- Valid expense states

### 3.6 FriendsCommands Class

### 3.7 SplitCommand Class

### 3.8 BudgetManager Class

### 3.9 Expense Class

### 4.0 Friend Class

### 4.1 Group Class


### Group Class

The `Group` class in the `seedu.duke.friends` package manages a collection of `Friend` objects under a specified group name.

#### Group Initialization
- **Constructor:** `Group(String name)`
- **Features:**
   - Sets the group name.
   - Initializes an empty list of `Friend` objects.

#### Adding Friends
- **Method:** `addFriend(Friend friend)`
- **Features:**
   - Adds a `Friend` object to the group.

#### Removing Friends
- **Method:** `removeFriend(String friendName)`
- **Features:**
   - Searches for the first friend with a matching name and removes them.
   - Returns `true` if removal is successful; otherwise returns `false`.
   - Note: Uses a for-each loop for removal, which may require caution regarding concurrent modifications.

#### Retrieving Friends
- **Method:** `getFriends()`
- **Features:**
   - Returns the list of `Friend` objects in the group.

#### Getting Group Name
- **Method:** `getName()`
- **Features:**
   - Returns the group’s name.

#### String Representation
- **Method:** `toString()`
- **Features:**
   - Constructs a formatted string showing the group name and member names.
   - If the group is empty, calls `messages.displayEmptyGroupMessage()` to display an appropriate message.
   - **Important:** Ensure that the `messages` field is initialized externally to prevent a `NullPointerException`.

### 4.2 GroupManager Class

### 4.3 HelpPage Class

### 4.4 Messages Class

### 4.5 ExpenseClassifier Class

### 4.6 Currency Class
The Currency class handles all currency-related operations within the application. It provides functionality for managing currency codes, conversion rates, and performing currency conversions.

#### Currency Constructor

The `Currency()` method sets up the exchange rates for different currencies with these features:

- Initializes a map to store the exchange rates between different currencies.
- Attempts to read current currency from the file `./currentCurrency`
- Handles any file-related issues or data retrieval failures gracefully by providing default exchange rates when necessary.

#### Initialising Exchange Rates

The `initializeExchangeRates()` method initializes currency exchange rates by adding predefined exchange rates for various currencies

- Each currency code is mapped to its corresponding exchange rate value.
- The exchange rates are hardcoded and include values for a wide range of currencies (e.g., AED, AFN, ALL, AMD, etc.).
- Ensures that the exchangeRates map is populated with accurate conversion values for use in currency calculations.

#### Change Currency Method

The `changeCurrency()` method handles the currency change process with these features:

- Prompts the user to choose between entering their own exchange rate (option 1) or using an estimated exchange rate (option 2).
- Handles the user's input by checking if they input '1' or '2', guiding them to make a valid choice.
- If the user selects option 1, it asks for the currency code (based on ISO 4217 standard) and validates if the currency exists in the exchangeRates map.
- In option 1, the user is also prompted to enter a custom exchange rate, which is validated and parsed.
- If the user selects option 2, it retrieves the current exchange rate for the selected currency using the getExchangeRate() method.
- If a valid exchange rate is found for the new currency, it calculates the exchange rate relative to the current currency.
- Handles NumberFormatException gracefully and prompts the user to input a valid number if there are formatting issues.
- The method invokes editExpenseCurrency() to update the expense currency with the final exchange rate and the new currency.

#### Edit Expense Currency Method

The `editExpenseCurrency()` method manages the process of updating the expense currency with these features:

- Accepts a finalExchangeRate and a newCurrency as parameters to update the expense currency.
- Calls the budgetManager.editExpenseCurrency() method to apply the new exchange rate.
- Updates the currentCurrency to the new currency.
- Attempts to write the new currency to a file using the writeToFile() method.
- Handles potential IOException by catching the error and displaying a message if there’s an issue with recording the change.
- Prints a success message confirming that the currency has been successfully changed to the new currency.

#### Write to File Method

The `writeToFile()` method handles writing the new currency to a file with these features:

- Accepts a newCurrency string as a parameter to be written to a file.
- Creates a FileWriter to open the file `./currentCurrency` in write mode, with the false parameter indicating that the file will be overwritten (not appended).
- Writes the newCurrency string to the file.
- Closes the FileWriter after writing the data to ensure resources are properly released.
- Declares the IOException to be thrown, which is handled by the calling method.

