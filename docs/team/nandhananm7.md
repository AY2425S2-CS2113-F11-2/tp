# Nandhana Nandhimandalam Madhavakrishna - Project Portfolio Page

## Overview

---

O\$P\$ is an expense-tracker application that allows the user to keep track of their expenditure with ease! Users can also manage expenses in a group setting with simple
splitting functions, as well as view analytics of their spending across multiple categories.

## Summary of Contributions

---

### Code Contributed

Code contribution to the project: [Reposense Page](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=nandhananm7&tabRepo=AY2425S2-CS2113-F11-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Features Implemented
Here is the list of features implemented:

#### 1. Group data Storage Feature

- **Function**: Every time a new group is created the group name as well as the members is saved onto groups.txt file automatically. 
- **Justification**: Users benefit from having their group data stored automatically, avoiding the need to re-enter information each session. This allows users to create and remove groups and members conveniently. 

#### 2. Group creation and modification

- **Function**: Users can create new groups, remove groups, add members to existing groups, view their groups
- **Justification**:

#### 3. Displaying the amount of money each member of the group owes

- **Function**: Loads the data from owedAmounts.txt and displays it next to each group member.
- **Justification**: Users can view the split of how much each person owes.

#### 4. Find/Search command for the expenses

- **Function**: Finds the expense based on the keyword
- **Justification**: Users can quickly view relevant expenses at one go.

---

### Enhancements Implemented

#### Unit Tests Across Friend and Group Related Classes
- **Input Validation**: Test cases validate inputs and ensure that error messages are returned for invalid entries.
- **Boundary and Edge Case Tests**: Test cases check for empty, null values, and other edge cases to ensure proper handling.
- **Error Handling**: Test cases confirm that appropriate error messages are displayed when specific errors occur.
- **Data Storage and Loading**: Test cases verify that the save file is created correctly and that data is properly saved and loaded.

### Contributions to User Guide

- **Documented Sections:**
    - `Find from previous expenses`
    - `Group Commands: 'create-group', 'view-group', 'add-member', 'remove-member', 'remove-group', 'my-groups'`

**Relevant Pull Requests:**
- [PR #73](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/73)
- [PR #31](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/31)

---

### Contributions to Developer Guide

#### Documented Sections
- **Documentation in Section 3**:
    - Section 3.1.2 `Group Storage Class`
    - Section 3.1.4 `ExpenseCommands Classes`
    - Section 3.1.5 `FriendsCommands class`
    - Section 3.1.9 `Friend Class`
    - Section 3.2.0 `Group Class`
    - Section 3.2.1 `GroupManeger Class`
    - Section 4.4 `Messages Class`

- **Documentation in Section 4**:
    - Section 4.3 `Create Group Feature`

**Relevant Pull Requests:**
- [PR #42](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/42)
- [PR #62](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/62)
- [PR #84](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/84)

---

### Review/Mentoring Contributions::
**Bug Finding and Suggestions:** Periodic code review of teammates PRs as well as expanding coverage of JUnit Tests

---

### Contributions to Team-Based Tasks
- **Release Management:**
    - Managed the `v1.0` release.
- **Progress Monitoring:**
    - Frequently reviewed and provided comments to teammates' PRs.
- **Codebase Maintenance:**
    - Proactive resolution of Java CI/CD pipeline errors and merging of code.
