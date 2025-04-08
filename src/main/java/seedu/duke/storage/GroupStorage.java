//@@author nandhananm7
package seedu.duke.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import seedu.duke.friends.Friend;
import seedu.duke.friends.Group;

public class GroupStorage {
    private static final String DATA_FILE = "groups.txt";
    private static final String SEPARATOR = "|";
    private static final String GROUP_HEADER = "[GROUP]";

    /**
     * Checks whether a given name is valid (not null/empty and contains only letters, numbers, and spaces).
     *
     * @param name the name to check.
     * @return true if valid, false otherwise.
     */
    private static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[A-Za-z0-9 ]+");
    }

    public static void saveGroups(List<Group> groups) {
        assert groups != null : "Groups list must not be null";
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            for (Group group : groups) {
                assert group != null : "Group in list must not be null";
                writer.write(GROUP_HEADER + SEPARATOR + group.getName() + System.lineSeparator());
                // Write all friends in the group.
                for (Friend friend : group.getFriends()) {
                    // Note: saving group name first, then friend's name.
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

        // If the file doesn't exist, return an empty list.
        if (!file.exists()) {
            return groups;
        }

        int skippedRecords = 0; // Count how many records were invalid/skipped.

        try (Scanner scanner = new Scanner(file)) {
            Group currentGroup = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue; // Skip blank lines.
                }
                try {
                    String[] parts = line.split("\\" + SEPARATOR);
                    // Validate the record has exactly 2 parts.
                    if (parts.length != 2) {
                        System.err.println("Warning: Unrecognized record format: " + line);
                        skippedRecords++;
                        continue;
                    }

                    // Process group header record.
                    if (parts[0].equals(GROUP_HEADER)) {
                        String groupName = parts[1].trim().toLowerCase();
                        if (!isValidName(groupName)) {
                            System.err.println("Warning: Invalid group name encountered: " + groupName);
                            skippedRecords++;
                            currentGroup = null;
                            continue;
                        }
                        currentGroup = new Group(groupName);
                        groups.add(currentGroup);
                    }
                    // Process friend record.
                    else {
                        if (currentGroup == null) {
                            System.err.println("Warning: Friend record found before any group header: " + line);
                            skippedRecords++;
                            continue;
                        }
                        // parts[0]: group name as stored in the record; parts[1]: friend name.
                        String groupFromRecord = parts[0].trim().toLowerCase();
                        String friendName = parts[1].trim().toLowerCase();
                        if (!isValidName(friendName)) {
                            System.err.println("Warning: Invalid friend name encountered: " + friendName +
                                    " removing entry");
                            skippedRecords++;
                            continue;
                        }
                        // Check consistency: the group name in the friend record should match the current group's name.
                        if (!groupFromRecord.equals(currentGroup.getName())) {
                            System.err.println("Warning: Group name in friend record (" + groupFromRecord
                                    + ") does not match current group header (" + currentGroup.getName() + "). " +
                                    "Skipping record.");
                            skippedRecords++;
                            continue;
                        }
                        currentGroup.addFriend(new Friend(friendName, groupFromRecord));
                    }
                } catch (Exception e) {
                    System.err.println("Warning: Failed to parse record: " + line + ". Error: " + e.getMessage());
                    skippedRecords++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading groups: " + e.getMessage());
        }

        // Summarize if any records were skipped.
        if (skippedRecords > 0) {
            System.out.println("Note: " + skippedRecords + " invalid record(s) were skipped during group loading.");
        }

        return groups;
    }
}
//@@author
