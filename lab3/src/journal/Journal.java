package journal;

import java.util.HashMap;
import java.util.Map;

public class Journal {
    private final Map<String, Group> groups;

    public Journal(Group[] groups) {
        this.groups = new HashMap<>();
        this.initGroups(groups);
    }

    public void initGroups(Group[] groups) {
        for (var group : groups) {
            this.groups.put(group.getName(), group);
        }
    }

    public Group getGroup(String groupName) {
        return this.groups.get(groupName);
    }

    public void addGrade(String groupName, Integer studentId, int columnIdx, int grade) {
        Group group = this.groups.get(groupName);
        group.setGrade(studentId, columnIdx, grade);
    }

    @Override
    public String toString() {
        StringBuilder journalString = new StringBuilder();
        for (var group : this.groups.values()) {
            journalString.append(group).append("\n");
        }
        return journalString.toString();
    }
}
