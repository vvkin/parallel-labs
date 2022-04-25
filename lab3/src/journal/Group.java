package journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Group {
    private final String name;
    private final Map<Integer, List<Integer>> studentGrades;

    public Group(String name, int studentsNumber) {
        this.name = name;
        this.studentGrades = new ConcurrentHashMap<>();
        this.initStudents(studentsNumber);
    }

    public void initStudents(int studentsNumber) {
        for (int idx = 0; idx < studentsNumber; ++idx) {
            studentGrades.put(idx, new ArrayList<>());
        }
    }

    public String getName() {
        return this.name;
    }

    public void addGrade(Integer studentId, int point) {
        this.studentGrades.get(studentId).add(point);
    }

    public Iterable<Integer> getStudents() {
        return this.studentGrades.keySet();
    }

    @Override
    public String toString() {
        StringBuilder groupString = new StringBuilder();
        groupString.append("Group: ").append(this.name).append("\n");

        for (var entry : studentGrades.entrySet()) {
            Integer studentId = entry.getKey();
            List<Integer> studentGrades = entry.getValue();
            groupString.append(String.format("%02d: ", studentId));
            List<String> mappedGrades = studentGrades.stream().map((value) -> String.format("%03d", value)).collect(Collectors.toList());
            groupString.append(mappedGrades).append("\n");
        }
        
        return groupString.toString();
    }
}
