import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.Student;

public class UserService {
    private final Map<String, Student> students;

    public UserService() {
        this.students = new HashMap<>();
    }

    public void registerStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student already registered: " + student.getId());
        }
        students.put(student.getId(), student);
    }

    public Student getStudent(String studentId) {
        if (studentId == null || !students.containsKey(studentId)) {
            throw new IllegalArgumentException("Student is not registered: " + studentId);
        }
        return students.get(studentId);
    }

    public void validateStudent(String studentId) {
        if (studentId == null || !students.containsKey(studentId)) {
            throw new IllegalArgumentException("Student is not registered: " + studentId);
        }
    }

    public boolean isStudentRegistered(String studentId) {
        return studentId != null && students.containsKey(studentId);
    }

    public Map<String, Student> getAllStudents() {
        return Collections.unmodifiableMap(students);
    }
}
