package journal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Group {
    private final String name;
    private final Map<Integer, int[]> studentGrades;

    public Group(String name, int studentsNumber, int gradesNumber) {
        this.name = name;
        this.studentGrades = new ConcurrentHashMap<>();
        this.initGrades(studentsNumber, gradesNumber);
    }

    public void initGrades(int studentsNumber, int gradesNumber) {
        for (int idx = 0; idx < studentsNumber; ++idx) {
            studentGrades.put(idx, new int[gradesNumber]);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setGrade(Integer studentId, int columnIdx, int grade) {
        this.studentGrades.get(studentId)[columnIdx] = grade;
        if (this.studentGrades.get(studentId)[columnIdx] != grade) {
            throw new RuntimeException("Race detected!");
        }
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
            int[] studentGrades = entry.getValue();
            groupString.append(String.format("%02d: ", studentId));
            List<String> mappedGrades = Arrays.stream(studentGrades)
                    .mapToObj((value) -> String.format("%03d", value))
                    .collect(Collectors.toList());
            groupString.append(mappedGrades).append("\n");
        }

        return groupString.toString();
    }
}
