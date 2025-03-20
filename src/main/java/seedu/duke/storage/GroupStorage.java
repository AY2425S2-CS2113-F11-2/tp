package seedu.duke.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import seedu.duke.friends.Group;
import seedu.duke.friends.Friend;

public class GroupStorage {
    private static final String DATA_FILE = "groups.txt";
    private static final String SEPARATOR = "|";
    private static final String GROUP_HEADER = "[GROUP]";

    public static void saveGroups(List<Group> groups) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            for (Group group : groups) {
                writer.write(GROUP_HEADER + SEPARATOR + group.getName() + System.lineSeparator());
                // Write all friends in the group
                for (Friend friend : group.getFriends()) {
                    // Note the order of arguments here, saving group name first, then friend's name
                    writer.write(friend.getGroup() + SEPARATOR + friend.getName() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving groups: " + e.getMessage());
        }
    }


    public static List<Group> loadGroups() {
        List<Group> groups = new ArrayList<>();
        File file = new File(DATA_FILE);

        // If the file doesn't exist, return an empty list
        if (!file.exists()) {
            return groups;
        }

        try (Scanner scanner = new Scanner(file)) {
            Group currentGroup = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split("\\" + SEPARATOR);

                // When a new group is encountered, create a new group object
                if (parts.length == 2 && parts[0].equals(GROUP_HEADER)) {
                    currentGroup = new Group(parts[1]);  // Group name is after [GROUP]|
                    groups.add(currentGroup);  // Add the group to the list
                    // When a friend is encountered, add it to the current group
                } else if (parts.length == 2 && currentGroup != null) {
                    // Create a Friend object using group and friend name
                    currentGroup.addFriend(new Friend(parts[1], parts[0]));  // Correct order
                    // Group name and Friend name
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading groups: " + e.getMessage());
        }

        return groups;
    }
}
