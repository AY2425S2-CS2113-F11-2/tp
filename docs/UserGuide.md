## Quick Start

1.  Ensure you have Java 17 or above installed on your PC. [Version 17 is preferred]
2.  You may download [here](https://se-education.org/guides/tutorials/javaInstallationMac.html) for Mac users and [here](https://www.oracle.com/sg/java/technologies/downloads/) for Windows users.
3.  If you have it installed already, you may check it by running “java -version” in your terminal.
4.  Download the latest .jar file from here. [link will be updated once v1 is ready]
5.  Copy the file to the folder you want to use as the home folder for your O$P$ budget tracking app 🙂.
6.  Open a command terminal, cd into the folder you put the jar file in, and use the “java -jar oSpS.jar” command to run the application.
7.  Type the command in the command box and press Enter to execute it.
8.  EG: Typing “help” and pressing Enter will open a mini window showing a list of all possible commands.
9.  Refer to the [features](https://docs.google.com/document/d/125Cg7wzuc4XFo3wsziwL2f64KN1uUfvFL5dIm6IQrSk/edit?tab=t.xl7ogrtj0a5q#heading=h.61o02m6y9xrc) section below for details on all commands and functionalities.


## Feature List

### Admin:

----------
#### Viewing help: /help

Lists the commands available to the user.
Usage: /help
*Expected output: [To insert list of commands that will be returned]

----------
#### Exiting the program: /bye
Terminates the program and saves the user’s data, such as their payee list, groups, balance and transaction information to a .csv file.
Usage: /bye
* Expected output: [Save aforementioned data into file and produce an exit message]
----------

#### Check Current Category: /cats

Shows all the previously created categories
Usage: /cats
*Sample expected output:
Your payment categories are: Personal, Transport, Accommodation

----------

### Manage Balance:

----------
#### Add an expense: /add

Add an expense with a description, amount, category, payee

Format:
-   /add `<amount>`
-   “App asks for description”  `<description of payment>`
-   “App asks for category of expense” `<category of expense>`
-   “App asks for currency” `<*optionally convert the amount into SGD as the home currency>`

Usage:
-   /add 400
-   “Add a description for this expense!”
-   User input: Grab-booking
-   “Add a category for this expense!”
-   User input: transport
-   “What currency is this expense in? Leave EMPTY if in SGD”
-   User input: YEN
-   “Awesome! I have added this expense :)  
    You can view it under your expenses now!”
-   The entry will automatically be tagged with a unique expense ID.

----------

#### Delete an expense: /delete
Delete expenses to remove unwanted expenses. However, users can use mark or unmark to mark as paid
Format: /delete `<expense ID>`
Usage: /delete 2

----------

#### View unsettled expenses: /list-unsettled
View expenses you owe or is owed to you
Format: /list-unsettled
NOTE: A positive number next to the name means that the friend owes you money, while a negative number indicates you owe the friend money
EXAMPLE:
1.  Alex owes you $10
2.  Mary owes you $20
3.  You owe Raj $30

----------
#### Mark settled expenses: /mark
Once a transaction is made user can mark as paid (for himself or for friends)
User has the option of using mark to settle all outstanding balances, all to be paid, and all that is owed to user.
Usage:
-   /mark `<expense ID>`
-   /mark-all
-   /mark-all-to-pay
-   /mark-all-to-receive
----------

#### View all the people user owes money to: /list-to-pay
User will be able to see all the people they owe money to.
`<Name of person>` `<group name>` `<amount>`
Output: `<Alex>` `<Thailand trip>` `<$10>`

----------
#### View all the people user needs to receive money from: /list-to-receive
User will be able to see all the people they need to receive money from.
`<Name of person>` `<group name>` `<amount>`
Output: `<Mary>` `<Thailand trip>` `<$10>`

----------

#### View balance in wallet: /balance
Shows total money to be paid and total money to pay.
Output:
Total money to pay: `<total amount user owes>`
Total money to receive: `<total amount user is owed>`

----------

### Manage Group Members:
----------
#### View Friends in a group: /view-mem
Views all the members of a specified group
Usage: /view-mem `<group name>`
*Sample expected output:
Here are the members in Jap Travel Group (3 members):
Person 1
Person 2
Person 3

----------
#### Add Friends to a group: /add-to-grp
Adds a user to a group. If the user does not exist, auto creates the user before adding him to the specified group.
Usage: /add-to-group `<username>` `<groupname>`
*Sample expected output:
(If user exists) Done! Added qwerty to Jap Travel Group
(If user doesn’t exists) Done! Created and added qwerty to Jap Travel Group

----------
#### Remove friends in a group: /remove-friend:
Removes the friend specified
Format: /remove_friend `<friend ID>` `<groupname>`
Usage: /remove_friend 3 besties

----------
### Manage Payments:
----------
#### Select split method: /split
Opens a menu for the user to select a method of splitting: equal or custom percentage/amount. The user can also close the menu.
Format: /split
NOTE 1:
-   Entering this command displays the splitting options on the terminal as such:
[1] Split equally among all members of the selected group
[2] Manually input percentage and members involved in transaction
[x]: Cancel

NOTE 2:
-   At this point, the user can input 1, 2 or x
Format: 1 / 2 / x;

NOTE 3:
-   If input is 2, prompt the user to add payees of their choice within the selected group by keying in “/users `<user 1>` `<user2>` … `<userN>`”, select currency using “/c `<currency code>`” and include amount to be assigned to each payee, starting with “/a `<...XX.XX in selected currency>` `<userX>` ” for the flat amount or “/p `<0 `< Y `<= 100>` `<userX>`” for percentage
-   User can repeat the amount assigning process infinitely and can finish this process by typing “/done” to update the list of transactions

*Expected output:
(If user enters 2) Members to split with:
(After “/users `<user 1>` `<user2>` … `<userN>`”) Select currency:
(After “/c `<currency code>`”) Amount to user:
(IF: After “/a `<...XX.XX in selected currency>`”) Repeat above step indefinitely
(ELSE IF: After “/done”) [Update list of transactions] Updated list!

Usage: /split, then either 1, 2 or x

----------

### Manage foreign currencies
----------
#### List currencies: /currencies
Lists all the currencies that have been entered
Usage: /currencies
[This is relative to SGD$1. ]
Sample expected output:
SGD$1 is equal to:
USD $0.75
MYR $3.33
YEN $113.73

----------
#### Add a new currency: /add-currency
Adds a new currency with a user specified exchange rate so it is stored in the exchange rates list.
Usage: /add-currency `<currency code>` /rate `<exchange rate>`
Sample Usage: /add-currency MYR /rate 3.29
[This is relative to SGD$1. ]

----------

#### Delete a currency rate: /delete-currency
Deletes a currency. * Note that expenses created before the currency rate was deleted will be in terms of SGD. All transaction data is stored in SGD.
Usage: /delete-currency `<currency code>`
Sample Usage: /delete-currency MYR

----------
#### Switch to common foreign currencies: /switch-currency
Switch all expenses to a currency of your choice.
Usage: /switch-currency `<currency code>`
