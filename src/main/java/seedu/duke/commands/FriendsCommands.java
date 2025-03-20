package seedu.duke.commands;

import seedu.duke.friends.Group;
import seedu.duke.friends.GroupManager;
import seedu.duke.friends.Friend;

import java.util.Scanner;

//@@author nandhananm7
public class FriendsCommands {
    private GroupManager groupManager;
    private Scanner scanner;

    public FriendsCommands(GroupManager groupManager) {
        this.groupManager = groupManager;
        this.scanner = new Scanner(System.in);
    }

    public void createGroup() {
        System.out.print("Enter the group name: ");
        String groupName = scanner.nextLine().trim();  // Get the group name from the user

        System.out.println("Who would you like to add to the group? (Type 'done' to finish)");

        while (true) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) {
                break;  // Exit when 'done' is entered
            }
            groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
        }

        groupManager.saveGroups();  // Save the updated groups using GroupManager
        System.out.println("Group created successfully!");
    }

    public void viewGroup() {
        System.out.print("Enter the group name to view: ");
        String groupName = scanner.nextLine().trim();

        if (groupManager.groupExists(groupName)) {
            System.out.println("Group: " + groupName);
            groupManager.viewGroupMembers(groupName);
        } else {
            System.out.println("Group not found.");
        }
    }

    public void viewAllGroups() {
        for (Group group : groupManager.getGroups()) {
            System.out.println(group);
        }
    }

    public void addMember() {
        System.out.print("Enter the name of the member to add: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter the group name: ");
        String groupName = scanner.nextLine().trim();

        if (groupManager.groupExists(groupName)) {
            groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
            groupManager.saveGroups();  // Save the updated group data
            System.out.println(name + " has been added to " + groupName);
        } else {
            System.out.print("Group does not exist. Would you like to create this group first? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                groupManager.addFriendToGroup(groupName, new Friend(name, groupName));  // Directly add the friend
                groupManager.saveGroups();  // Save the new group and member
                System.out.println("Group " + groupName + " has been created and " + name + " has been added.");
            } else {
                System.out.println("Operation cancelled. " + name + " was not added.");
            }
        }
    }

    public void removeMember() {
        System.out.print("Enter the name of the member to remove: ");
        String memberName = scanner.nextLine().trim();

        System.out.print("Enter the group name from which to remove " + memberName + ": ");
        String groupName = scanner.nextLine().trim();

        if (!groupManager.groupExists(groupName)) {
            System.out.println("Group does not exist.");
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
            groupManager.saveGroups();  // Save the updated group data
            System.out.println(memberName + " has been removed from " + groupName);
        } else {
            System.out.println(memberName + " is not in " + groupName);
        }
    }

}
//@@author
