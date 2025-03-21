package seedu.duke.friends;

import seedu.duke.messages.Messages;
import seedu.duke.storage.GroupStorage;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//@@author nandhananm7

public class GroupManager {
    private List<Group> groups;
    private Messages messages;

    public GroupManager() {
        this.groups = GroupStorage.loadGroups();  // Load the existing groups from storage
    }

    // Add a friend to a group
    public void addFriendToGroup(String groupName, Friend friend) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                group.addFriend(friend);
                return;
            }
        }
        // If group doesn't exist, create a new one and add the friend
        Group newGroup = new Group(groupName);
        newGroup.addFriend(friend);
        groups.add(newGroup);
    }

    // Check if a group exists
    public boolean groupExists(String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }
    //@@author matthewyeo1
    // View all members of a group and the amount each owes
    public void viewGroupMembers(String groupName) {
        // Load owed amounts from the file
        String owesFile = "owedAmounts.txt";
        File file = new File(owesFile);

        // Create a map to store member names and their owed amounts
        java.util.Map<String, Double> owedAmounts = new java.util.HashMap<>();

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("- ")) { // Lines with owed amounts start with "- "
                    String[] parts = line.split(" owes: ");
                    if (parts.length == 2) {
                        String name = parts[0].substring(2).trim(); // Extract the member's name
                        double amount = Double.parseDouble(parts[1].trim()); // Extract the owed amount
                        owedAmounts.put(name, amount); // Store in the map
                    }
                }
            }
        } catch (FileNotFoundException e) {
            messages.displayMissingFileExceptionMessage();
        } catch (NumberFormatException e) {
            messages.displayInvalidAmountExceptionMessage();
        }

        // Display group members and their owed amounts
        boolean groupFound = false;
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                groupFound = true;
                System.out.println("Members of group \"" + groupName + "\":");
                for (Friend friend : group.getFriends()) {
                    String name = friend.getName();
                    double amountOwed = owedAmounts.getOrDefault(name, 0.0); // Default to 0.0 if no amount is recorded
                    System.out.println("- " + name + " owes: " + String.format("%.2f", amountOwed));
                }
                break;
            }
        }

        if (!groupFound) {
            messages.displayMissingGroupMessage();
        }
    }
    //@@author

    public List<Friend> getGroupMembers(String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group.getFriends();
            }
        }
        return new ArrayList<>(); // Return an empty list if the group doesn't exist
    }

    // Remove a group by name
    public void removeGroup(String groupName) {
        boolean removed = groups.removeIf(group -> group.getName().equals(groupName));

        if (removed) {
            saveGroups(); // Save changes after removal
            System.out.println("Group '" + groupName + "' has been removed.");
        } else {
            messages.displayMissingGroupMessage();
        }
    }

    // Save the updated list of groups
    public void saveGroups() {
        GroupStorage.saveGroups(groups);
    }

    // Get all groups
    public List<Group> getGroups() {
        return groups;
    }
}
//@@author

