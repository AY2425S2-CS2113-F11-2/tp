//@@author nandhananm7
package seedu.duke.friends;

import seedu.duke.messages.Messages;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<Friend> friends;
    private Messages messages;

    /**
     * Constructs a Group with a specified name.
     *
     * @param name the name of the group.
     */
    public Group(String name) {
        this.name = name;
        this.friends = new ArrayList<>();
    }

    /**
     * Adds a friend to the group.
     *
     * @param friend the friend to add.
     */
    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    /**
     * Removes a friend from the group by name.
     *
     * @param friendName the name of the friend to remove.
     * @return true if the friend was successfully removed, false if not found.
     */
    public boolean removeFriend(String friendName) {
        for (Friend friend : friends) {
            if (friend.getName().equals(friendName)) {
                friends.remove(friend);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a member is in the group.
     *
     * @param memberName the name to check.
     * @return true if the member is in the group, false otherwise.
     */
    public boolean isMemberInGroup(String memberName) {
        for (Friend friend : friends) {
            if (friend.getName().equals(memberName)) {
                return true;
            }
        }
        return false;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    /**
     * Retrieves the name of the group.
     *
     * @return the group name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the group.
     *
     * @return a formatted string of group name and members.
     */
    @Override
    public String toString() {
        String result = "Group Name: " + name + "\nMembers:\n";

        if (friends.isEmpty()) {
            result += messages.displayEmptyGroupMessage();
        } else {
            for (Friend friend : friends) {
                result += "- " + friend.getName() + "\n";
            }
        }
        return result;
    }
}
//@@author
