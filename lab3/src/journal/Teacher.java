package journal;

import java.util.concurrent.ThreadLocalRandom;

public class Teacher extends Thread {
    private static final int MAX_GRADE = 100;
    private final Journal journal;
    private final String[] groupNames;
    private final int weeksNumber;

    public Teacher(Journal journal, String[] groupNames, int weeksNumber) {
        this.journal = journal;
        this.groupNames = groupNames;
        this.weeksNumber = weeksNumber;
    }

    @Override
    public void run() {
        for (int weekIdx = 0; weekIdx < weeksNumber; ++weekIdx) {
            for (var groupName : groupNames) {
                Group group = journal.getGroup(groupName);
                for (var student : group.getStudents()) {
                    int grade = ThreadLocalRandom.current().nextInt(0, MAX_GRADE + 1);
                    journal.addGrade(groupName, student, grade);
                }
            }
        }
    }
}
