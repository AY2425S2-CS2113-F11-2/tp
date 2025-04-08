# Asher Tan - Project Portfolio Page

## Overview

---

O\$P\$ is an expense-tracker application that empowers users to manage their spending efficiently. Among its many features, the expense splitting functionality plays a crucial role in helping groups track shared costs seamlessly. My primary contribution to the project is the design and implementation of the **SplitCommand** class, which handles the logic for dividing expenses among group members.

## Summary of Contributions

---

### Code Contributed
Code Contribution to the project: https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=asher&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21

---

### Features Implemented
Here is the list of features implemented:

#### 1. Split Expense Feature

- **Function**: Lets the user split expenses between members of the group.
- **Justification**: This is the backbone of OSPS, to be able to keep track of the amounts owed to individuals, and that individuals owe to you. Split is a command that is simple to use, yet has a lot of functionality and customizability, allowing you to split equally, or by percentage or absolute amounts. The data is persisted and each split operation is logged to be retrieved in `view-member` command.

- **Equal Split:**  
  Automatically divides the selected expense evenly among all members of a specified group.
- **Manual Split:**  
  Offers two modes:
  - **Absolute Mode (/a):** Users assign fixed monetary values to each member.
  - **Percentage Mode (/p):** Users specify the share as a percentage of the total expense.
- **Checks and Assertions:**  
  Includes assertions and input checks to ensure that:
  - The expense list and group data are valid.
  - User inputs do not exceed available amounts or percentages.
  - The input stream is not prematurely exhausted.
- **Data Storage:**  
  - Implemented data storage to ensure that the data is persistent and is granular enough to be able to track back individual transaction and split operations from other commands.
- **Anti-Tampering:**  
  - Implemented checksum functionality to detect tampering of the data files used for persistent storage, and triggering to clear the files if the files are tampered with.
- **Balance Update:**  
  After a split, the updated balances are immediately displayed by invoking the group view functionality.

#### 2. View Group Command

- **Function**: Lets the user see all the split expenses of the members in a group.
- **Justification**: Users are able to, in a glance, view all the summary expenses allocated via split to each member in a selected group. This lets the user know the balance/inbalance of amounts in a particular group, and can also be used for when the user wants the members to return him/her the amounts.

#### 3. View Member Command

- **Function**: Lets the user see all the individual split expenses allocated to a member in a group.
- **Justification**: Users will be able to track each expense allocated via the `Split` command for logging and backtracking purposes.

### Contributions to Documentation

- **Developer Guide:**  
  Wrote detailed sections documenting the `SplitCommand` class, including its design, key methods, and integration with other modules including DataStorage, GroupManager, OwesStorage, FriendsCommand. Also wrote the documentation for `ViewGroup` and `ViewMember` methods in the `FriendCommand` class.

- **Documented Sections:**
    - `Split Commands`
    - `View Group Command`
    - `View Member Command`

### Contributions to Team-Based Tasks

- **Code Review & Mentorship:**  
  Provided critical feedback on pull requests related to the expense splitting functionality, ensuring adherence to design standards and robust error handling.
- **Collaboration:**  
  Participated in design discussions and helped resolve issues related to input validation and file handling for the split feature. 
- **Anti-Tampering Efforts:**  
  Spearheaded the checksum implementation for preventing file tampering of persistent data to address the bug of tampering of local files on the testing device.

---

**Relevant Pull Request:** 
[PR #177](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/177)
[PR #167](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/167)
[PR #65](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/65)
[PR #51](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/51)


---
### Review/Mentoring Contributions:
**Bug Finding and Suggestions:** Thorough analysis of PR code to ensure no conflicts..

---

### Contributions to Team-Based Tasks
- **Code Review**
  - Sequence Diagram for Split Class
  - Code reviews for teammates' PR

---
### Contributions to Developer Guide & User Guide

#### Documented Sections
- **Documentation in Section 3.1.6**:
- SplitCommand Class, inclusive of `executeSplit()` and owesStorage
- **Documentation in Section 3.1.5**:
- Viewing an existing group (`viewGroup()` method, `viewMember()` method)
- **UML Sequence Diagram in Section 4.3**:
- Split Expense Feature  in `Overall Application Architecture`, including a Sequence Diagram
---