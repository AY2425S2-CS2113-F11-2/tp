package seedu.duke.friends;

import java.util.ArrayList;
import java.util.List;

//@@author nandhananm7
public class Group {
    private String name;
    private List<Friend> friends;

    public Group(String name) {
        this.name = name;
        this.friends = new ArrayList<>();
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    // In Group.java
    public boolean removeFriend(String friendName) {
        for (Friend friend : friends) {
            if (friend.getName().equals(friendName)) {
                friends.remove(friend);
                return true; // Successfully removed
            }
        }
        return false; // Friend not found
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String result = "Group Name: " + name + "\nMembers:\n";

        if (friends.isEmpty()) {
            result += "No members in this group.";
        } else {
            for (Friend friend : friends) {
                result += "- " + friend.getName() + "\n";
            }
        }
        return result;
    }

}
//@@author
