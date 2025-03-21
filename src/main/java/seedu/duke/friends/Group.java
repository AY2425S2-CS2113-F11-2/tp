package seedu.duke.friends;

import seedu.duke.messages.Messages;

import java.util.ArrayList;
import java.util.List;

//@@author nandhananm7
public class Group {
    private String name;
    private List<Friend> friends;
    private Messages messages;

    public Group(String name) {
        this.name = name;
        this.friends = new ArrayList<>();
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

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
