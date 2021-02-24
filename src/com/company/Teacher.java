package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Teacher {

    private final String name;
    private final int birthYear;
    private final String code;
    private final ArrayList<Subject> assignedSubjects = new ArrayList<>();
    private Roster roster = new Roster();

    public Teacher(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.code = setCode();
    }

    /**
     * Set's the code of the teacher by a combination of name and birth.
     * @return a String combination of firstname + birthYear, so Thom born in 1998 = Thom1998
     */
    private String setCode() {
        String[] splitName = this.name.split(" ");
        return "%s%d".formatted(splitName[0], birthYear);
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<Subject> getAssignedSubjects() {
        return assignedSubjects;
    }

    public Roster getRoster() {
        return roster;
    }

    public void addToRoster(Lesson lesson) {
        roster.addLesson(lesson);
    }

    public void removeFromRoster(Lesson lesson) { roster.removeLesson(lesson);}

    /**
     * Adds the subject to this teachers' assigned subjects.
     * It then adds this teacher to the subjects' qualifiedTeachers list.
     * If the teacher does not have the subject, but the subject does have the teacher, we catch the thrown exception and assign the teacher without altering the subject's list
     * @throws IllegalArgumentException if the teacher already has the subject assigned.
     * @param subject the subject being added to the assignedSubjects list.
     */
    public void addAssignedSubject(Subject subject) {
        if (assignedSubjects.contains(subject)) {
//            This gets thrown when the teacher already has the subject in it's assignedSubjects list\
//            System.out.println("assignedSubjects contains this");
            throw new IllegalArgumentException("Subject already assigned to teacher");
        }
        try {
            subject.addQualifiedTeacher(this);
        } catch (IllegalArgumentException e) {
//            This gets thrown when a teacher does not already have the subject in it's assigned list,
//            but the subject does have the teacher in it's qualified list. I don't think this should happen,
//            but try/catching it anyway
            System.out.printf("Teacher %s already qualified to teach subject %s%n", this.getName(), subject.getName());
        }
        this.assignedSubjects.add(subject);
    }

    /**
     * Given multiple subjects, it will go through each and call {@link #addAssignedSubject(Subject)} which does the actual adding.
     * Because {@link #addAssignedSubject(Subject)} can throw an exception, we catch it here so the loop isn't stopped.
     * @param subjects a list of subjects to be added to the assignedSubjects list
     */
    public void addAssignedSubjects(Subject[] subjects) {
        for (Subject s : subjects) {
            try {
                addAssignedSubject(s);
//                System.out.printf("%s now assigned to teacher %s%n", s.getName(), this.getName());
            } catch (IllegalArgumentException e) {
                System.out.printf("Subject %s already assigned to teacher %s%n", s.getName(), this.getName());
            }
        }
    }

    public void removeAssignedSubject(Subject subject) {
        subject.removeQualifiedTeacher(this);
        this.assignedSubjects.remove(subject);
    }
}
