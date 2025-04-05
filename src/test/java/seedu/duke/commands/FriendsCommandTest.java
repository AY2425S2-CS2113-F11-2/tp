package seedu.duke.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.friends.Friend;
import seedu.duke.friends.GroupManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
//@@author matthewyeo1
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//@@author

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendsCommandTest {
    private FriendsCommands friendsCommands;
    private GroupManager groupManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);
        System.setOut(new PrintStream(outContent)); // Capture console output
    }

    //@@author matthewyeo1
    @AfterEach
    void tearDown() {
        System.setIn(originalIn); // Restore original System.in
        outContent.reset();       // Clear output buffer
    }
    //@@author

    @AfterEach
    void clearGroupsFile() {
        File file = new File("groups.txt");
        try (FileWriter writer = new FileWriter(file, false)) { // Open in overwrite mode
            writer.write(""); // Clear file contents
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to provide simulated input.
     * This sets System.in to the given input and reinitializes friendsCommands so that its Scanner uses it.
     *
     * @param input the simulated user input
     */
    void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        friendsCommands = new FriendsCommands(groupManager);
    }

    @Test
    void testCreateGroupWithDuplicateName() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        friendsCommands = new FriendsCommands(groupManager);
        friendsCommands.createGroup("create-group /TestGroup");

        assertTrue(outContent.toString().contains("Group 'TestGroup' already exists."));
    }

    @Test
    void testViewGroupExistingGroup() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        provideInput("TestGroup\n");
        friendsCommands = new FriendsCommands(groupManager);
        friendsCommands.viewGroup("view-group /TestGroup");

        assertTrue(outContent.toString().contains("Group: TestGroup"));
    }

    @Test
    void testViewGroupNonExistentGroup() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        // Use the new single-line command format
        friendsCommands.viewGroup("view-group /NonExistingGroup");

        assertTrue(outContent.toString().contains("Group not found"));
    }


    @Test
    void testViewAllGroupsWithGroups() {
        groupManager.addFriendToGroup("Group1", new Friend("Alice", "Group1"));
        groupManager.addFriendToGroup("Group2", new Friend("Bob", "Group2"));

        friendsCommands = new FriendsCommands(groupManager);
        friendsCommands.viewAllGroups();

        assertTrue(outContent.toString().contains("Group1"));
        assertTrue(outContent.toString().contains("Group2"));
    }

    @Test
    void testAddMemberToExistingGroup() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.addMember("add-member /Bob /TestGroup");

        List<Friend> friends = groupManager.getGroupMembers("TestGroup");
        assertEquals(2, friends.size());
        assertTrue(friends.stream().anyMatch(friend -> friend.getName().equals("Bob")));
    }

    @Test
    void testAddMemberToNonExistentGroupCreatesGroup() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        provideInput("y\n");

        friendsCommands.addMember("add-member /Charlie /NewGroup");

        assertTrue(groupManager.groupExists("NewGroup"));
        List<Friend> friends = groupManager.getGroupMembers("NewGroup");
        assertEquals(1, friends.size());
        assertTrue(friends.stream().anyMatch(friend -> friend.getName().equals("Charlie")));
    }



    @Test
    void testAddMemberToNonExistentGroupUserRejects() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        provideInput("n\n");

        friendsCommands.addMember("add-member /Charlie /NonExistingGroup");

        assertFalse(groupManager.groupExists("NonExistingGroup"));
        assertTrue(outContent.toString().contains("Operation cancelled. Charlie was not added."));
    }

    @Test
    void testRemoveMemberFromNonExistentGroup() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.removeMember("remove-member /Alice /NonExistingGroup");

        assertTrue(outContent.toString().contains("Alice is not in NonExistingGroup"));
    }

    @Test
    void testRemoveMemberFromExistingGroup() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        friendsCommands = new FriendsCommands(groupManager);

        provideInput("y\n");

        friendsCommands.removeMember("remove-member /Alice /TestGroup");

        List<Friend> friends = groupManager.getGroupMembers("TestGroup");
        assertTrue(friends.isEmpty());
        assertTrue(outContent.toString().contains("Alice has been removed from TestGroup"));
    }


    @Test
    void testRemoveMemberNotPresentInGroup() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.removeMember("remove-member /Bob /TestGroup");

        assertTrue(outContent.toString().contains("Bob is not in TestGroup"));
    }

    @Test
    void testRemoveExistingGroup() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        friendsCommands = new FriendsCommands(groupManager);

        // Simulate user confirmation for removal
        provideInput("y\n");

        friendsCommands.removeGroup("remove-group /TestGroup");

        assertFalse(groupManager.groupExists("TestGroup"));
        assertTrue(outContent.toString().contains("Group TestGroup has been removed."));
    }


    @Test
    void testRemoveNonExistentGroup() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.removeGroup("remove-group /NonExistingGroup");

        assertTrue(outContent.toString().contains("Group: NonExistingGroup does not exist."));
    }

    @Test
    void testAddDuplicateMemberToExistingGroup() {
        groupManager = new GroupManager();
        groupManager.addFriendToGroup("TestGroup", new Friend("Alice", "TestGroup"));
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.addMember("add-member /Alice /TestGroup");

        assertTrue(outContent.toString().contains("Member 'Alice' already exists in group 'TestGroup'."));
    }

    @Test
    void testAddMemberWithInvalidCharacters() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.addMember("add-member /@lice /NewGroup");

        assertFalse(groupManager.groupExists("NewGroup"));
        assertTrue(outContent.toString().contains("Invalid member name. " +
                "Name cannot be empty or contain special characters."));
    }

    @Test
    void testCreateGroupWithInvalidCharacters() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.createGroup("create-group /New@Group");

        assertFalse(groupManager.groupExists("New@Group"));
        assertTrue(outContent.toString().contains("Invalid group name. " +
                "Name cannot be empty or contain special characters."));
    }

    @Test
    void testViewAllGroupsNoGroupsExist() {
        groupManager = new GroupManager();
        friendsCommands = new FriendsCommands(groupManager);

        friendsCommands.viewAllGroups();

        assertTrue(outContent.toString().contains("You have no groups."));
    }


}
