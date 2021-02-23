package com.company;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Lesson {

    private final Teacher teacher;
    private Group group = null;
    private final Classroom classroom;
    private final Subject subject;
    private final Period period;

    public Lesson(Teacher teacher, Group group, Classroom classroom, Subject subject, Period period) {
        this.teacher = teacher;
        this.group = group;
        this.classroom = classroom;
        this.subject = subject;
        this.period = period;
        assignLessonToOthers();
    }

    public Lesson(Teacher teacher, Classroom classroom, Subject subject, Period period) {
        this.teacher = teacher;
        this.classroom = classroom;
        this.subject = subject;
        this.period = period;
        assignLessonToOthers();
    }

    public Teacher getTeacher() { return teacher; }

    public Group getGroup() { return group; }

    public Classroom getClassroom() { return classroom; }

    public Subject getSubject() { return subject; }

    public Period getPeriod() { return period; }

    public void setGroup(Group group) {
        this.group = group;
        assignLessonToOthers(); //Could also make assign lesson to group, to make sure others don't get this reassigned.
    }

    public void assignLessonToOthers() {
//        Does not check if they are available for this lesson.That logic will be done in a method not yet made.
//        if (teacher.getRoster().getLessonsList().stream().noneMatch(l -> l.getTeacher() == teacher)) {
        teacher.addToRoster(this);
//        }
        classroom.addToRoster(this);
        if (group != null) {
            group.addToRoster(this);
        }
    }
}
