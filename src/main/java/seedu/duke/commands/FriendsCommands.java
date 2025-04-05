package seedu.duke.commands;
import seedu.duke.friends.Group;
import seedu.duke.friends.GroupManager;
import seedu.duke.friends.Friend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

//@@author nandhananm7
public class FriendsCommands {
    private GroupManager groupManager;
    private Scanner scanner;

    public FriendsCommands(GroupManager groupManager) {
        this.groupManager = groupManager;
        this.scanner = new Scanner(System.in);
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // This regex allows letters, numbers, and spaces.
        return name.matches("[A-Za-z0-9 ]+");
    }

    public void createGroup(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || !parts[0].equals("create-group")) {
            System.out.println("Invalid command. Please use the format: create-group /<group-name>");
            return;
        }

        String groupName = parts[1].trim();
        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupManager.groupExists(groupName)) {
            System.out.println("Group '" + groupName + "' already exists.");
            System.out.println("If you would like to add more members to" + groupName + "use the add-member command.");
            return;
        }

        System.out.println("Who would you like to add to the group? (Type 'done' to finish)");
        boolean hasMembers = false;

        while (true) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) {
                break;
            }

            if (!isValidName(name)) {
                System.out.println("Invalid name. It cannot be empty or contain special characters. Try again.");
                continue;
            }

            if (groupManager.isMemberInGroup(groupName, name)) {
                System.out.println("Member '" + name + "' already exists in the group.");
                continue;
            }

            groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
            hasMembers = true;
        }

        if (!hasMembers) {
            System.out.println("Operation cancelled. Group must have at least one member.");
            return;
        }

        groupManager.saveGroups();
        System.out.println("Group created successfully!");
    }



    // Edited to ensure sum up values from owedAmounts.txt instead of showing the last value
    public void viewGroup(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || !parts[0].equals("view-group")) {
            System.out.println("Invalid command. Please use the format: view-group <group-name>");
            return;
        }

        String groupName = parts[1].trim();
        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        // Check if this group exists
        if (!groupManager.groupExists(groupName)) {
            System.out.println("Group not found");
            return;
        }
        System.out.println("Group: " + groupName);

        // Load owedAmounts.txt into owedAmounts map
        java.util.Map < String, Double > owedAmounts = new java.util.HashMap < > ();
        File file = new File("owedAmounts.txt");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("- ")) { // Format for data storage, delimiter
                    String[] lineParts = line.split(" owes: ");
                    if (lineParts.length == 2) {
                        // Extract the member's name and the owed amount
                        String name = lineParts[0].substring(2).trim(); //remove "- "
                        double amount = Double.parseDouble(lineParts[1].trim());

                        // Accumulate amounts for that name, do NOT overwrite
                        double existing = owedAmounts.getOrDefault(name, 0.0);
                        owedAmounts.put(name, existing + amount);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Exception for file not found
            System.out.println("Owed amounts file not found. No expense data available.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing expense data.");
        }

        // Get group members and print their name with expense.
        List <Friend> members = groupManager.getGroupMembers(groupName);
        if (members.isEmpty()) {
            System.out.println("No members in this group.");
        } else {
            System.out.println("Members:");
            for (Friend friend: members) {
                String friendName = friend.getName();
                double totalOwed = owedAmounts.getOrDefault(friendName, 0.0);
                System.out.println(friendName + " - Expense: $" + String.format("%.2f", totalOwed));
            }
        }
    }

    //@@author Ashertan256
    public void viewGroupDirect(String groupName) {
        // Exactly the same as viewGroup but WITHOUT requiring the user to input the group name. 
        //Called directly from the split command, to show the sum of amounts. 
        //Should not be callable from user input side.

        Map < String, Double > owedAmounts = new HashMap < > ();
        File file = new File("owedAmounts.txt");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("- ")) {
                    String[] parts = line.split(" owes: ");
                    if (parts.length == 2) {
                        String name = parts[0].substring(2).trim();
                        double amount = Double.parseDouble(parts[1].trim());
                        owedAmounts.put(name, owedAmounts.getOrDefault(name, 0.0) + amount);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Owed amounts file not found. No expense data available.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing expense data.");
        }

        // Retrieve the group’s members and print their total so far
        List < Friend > members = groupManager.getGroupMembers(groupName);
        if (members.isEmpty()) {
            System.out.println("No members in this group.");
        } else {
            System.out.println("Members:");
            for (Friend friend: members) {
                String name = friend.getName();
                double totalOwed = owedAmounts.getOrDefault(name, 0.0);
                System.out.println(name + " - Expense: $" + String.format("%.2f", totalOwed));
            }
        }
    }
    //@@author

    //@@author nandhananm7
    public void viewAllGroups() {
        if (groupManager.getGroups().isEmpty()) {
            System.out.println("You have no groups.");
        } else {
            for (Group group: groupManager.getGroups()) {
                System.out.println(group);
            }
        }
    }

    public void addMember(String command) {
        String[] parts = command.trim().split(" */", 3);
        if (parts.length < 3 || !parts[0].equals("add-member")) {
            System.out.println("Invalid command. Please use the format: add-member /<member name> /<group-name>");
            return;
        }

        String name = parts[1].trim();
        String groupName = parts[2].trim();

        if (name.isEmpty() || !isValidName(name)) {
            System.out.println("Invalid member name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        // Check if group exists and prevent adding to a duplicate group
        if (groupManager.groupExists(groupName)) {
            if (groupManager.isMemberInGroup(groupName, name)) {
                System.out.println("Member '" + name + "' already exists in group '" + groupName + "'.");
                return;
            }
            groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
            groupManager.saveGroups(); // Save the updated group data
            System.out.println(name + " has been added to " + groupName);
        } else {
            System.out.print("Group does not exist. Would you like to create this group first? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y")) {
                groupManager.addFriendToGroup(groupName, new Friend(name, groupName)); // Directly add the friend
                groupManager.saveGroups(); // Save the new group and member
                System.out.println("Group " + groupName + " has been created and " + name + " has been added.");
            } else {
                System.out.println("Operation cancelled. " + name + " was not added.");
            }
        }
    }


    public void removeMember(String command) {
        String[] parts = command.trim().split(" */", 3);
        if (parts.length < 3 || !parts[0].equals("remove-member")) {
            System.out.println("Invalid command. Please use the format: remove-member /<member name> /<group-name>");
            return;
        }

        String memberName = parts[1].trim();
        String groupName = parts[2].trim();

        if (memberName.isEmpty() || !isValidName(memberName)) {
            System.out.println("Invalid member name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        // Check if the member is in the group before asking for confirmation
        boolean memberExists = false;
        for (Group group : groupManager.getGroups()) {
            if (group.getName().equals(groupName) && group.hasFriend(memberName)) {
                memberExists = true;
                break;
            }
        }

        if (!memberExists) {
            System.out.println(memberName + " is not in " + groupName);
            return;
        }

        System.out.print("Are you sure you want to remove " + memberName + " from " + groupName + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Operation cancelled. " + memberName + " was not removed from " + groupName);
            return;
        }

        boolean removed = false;
        for (Group group : groupManager.getGroups()) {
            if (group.getName().equals(groupName)) {
                removed = group.removeFriend(memberName);
                break;
            }
        }

        if (removed) {
            groupManager.saveGroups(); // Save the updated group data
            System.out.println(memberName + " has been removed from " + groupName);
        } else {
            System.out.println(memberName + " is not in " + groupName);
        }
    }

    public void removeGroup(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || !parts[0].equals("remove-group")) {
            System.out.println("Invalid command. Please use the format: remove-group /<group-name>");
            return;
        }

        String groupName = parts[1].trim();

        if (!groupManager.groupExists(groupName)) {
            System.out.println("Group: " + groupName + " does not exist.");
            return;
        }

        System.out.print("Are you sure you want to remove the group " + groupName + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Operation cancelled. Group " + groupName + " was not removed.");
            return;
        }

        groupManager.removeGroup(groupName);
        groupManager.saveGroups(); // Save the updated groups list
        System.out.println("Group " + groupName + " has been removed.");
    }

}
//@@author
