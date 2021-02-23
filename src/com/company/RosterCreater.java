package com.company;

import java.util.ArrayList;

public class RosterCreater {

    private ArrayList<Classroom> classrooms;
    private ArrayList<Subject> currentSubjects;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;
    private ArrayList<Lesson> lessons;
    private ArrayList<Period> periods;
    private ArrayList<Student> students;

    public RosterCreater(ArrayList<Classroom> classrooms, ArrayList<Subject> currentSubjects, ArrayList<Teacher> teachers, ArrayList<Group> groups, ArrayList<Lesson> lessons, ArrayList<Period> periods, ArrayList<Student> students) {
        this.classrooms = classrooms;
        this.currentSubjects = currentSubjects;
        this.teachers = teachers;
        this.groups = groups;
        this.lessons = lessons;
        this.periods = periods;
        this.students = students;
    }


}
