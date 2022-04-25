package journal;

import java.util.List;

public class JournalApp {
    public static void main(String[] args) throws InterruptedException {
        final int WEEKS_NUMBER = 30;

        Group[] groups = {
                new Group("IP-91", 10, WEEKS_NUMBER),
                new Group("IP-92", 10, WEEKS_NUMBER),
                new Group("IP-93", 10, WEEKS_NUMBER)
        };
        Journal journal = new Journal(groups);

        List<Teacher> teachers = List.of(
                new Teacher(journal, new String[]{"IP-91", "IP-92", "IP-93"}, WEEKS_NUMBER),
                new Teacher(journal, new String[]{"IP-91"}, WEEKS_NUMBER),
                new Teacher(journal, new String[]{"IP-92"}, WEEKS_NUMBER)
        );

        for (var teacher : teachers) teacher.start();
        for (var teacher : teachers) teacher.join();

        System.out.println(journal);
    }
}
