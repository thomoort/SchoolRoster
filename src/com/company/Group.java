package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Group extends Roster{

    private String name;
    private int maxCapacity;
    private ArrayList<Student> students = new ArrayList<>();
    private Roster roster = new Roster();

    public Group(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxCapacity() { return this.maxCapacity; }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    public Student getStudentByCode(String code) {
        List<Student> result = students.stream().filter(s -> s.getCode().equals(code)).collect(Collectors.toList());
        return result.get(0);
    }

    public void addLesson(Lesson lesson) {
        super.addLesson(lesson);
        for (Student s : students) {
            s.addLesson(lesson);
        }
    }

    @Override
    public void removeLesson(Lesson lesson) {
        super.removeLesson(lesson);
        for (Student s : students) {
            s.removeLesson(lesson);
        }
    }

    public void addStudentToGroup(Student student) {
        if (student.getGroup() != null) {
            throw new IllegalArgumentException("Student already assigned to a group, remove them first");
        }

        student.addToGroup(this);
        students.add(student);
    }

    public void removeStudentFromGroup(Student student) {
        if (student.getGroup() == null) {
            throw new IllegalArgumentException("Student not in a group, can't remove.");
        }

        student.removeFromGroup();
        students.remove(student);
    }
}
