package seedu.duke.friends;

//@@author nandhananm7
public class Friend {
    private String group;
    private String name;

    public Friend(String name, String group) {
        this.group = group;
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }
}
//@@author
