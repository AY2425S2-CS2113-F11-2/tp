package seedu.duke.friends;

import seedu.duke.messages.Messages;
import seedu.duke.storage.GroupStorage;
import java.util.List;
import java.util.ArrayList;

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

    public boolean isMemberInGroup(String groupName, String memberName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group.isMemberInGroup(memberName);
            }
        }
        return false;
    }

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

