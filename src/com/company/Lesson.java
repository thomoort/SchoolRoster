package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lesson {

    //Possible idea to add a constructor without period, and create assign to others with period method. This way we might be able to randomize a roster,
    //Needs to be considered more though, because it seems unlikely to work.

    private Teacher teacher;
    private Group group = null;
    private Classroom classroom;
    private Subject subject;
    private Period period;

    public Lesson(Teacher teacher, Group group, Classroom classroom, Subject subject, Period period) {
        this.teacher = teacher;
        this.group = group;
        this.classroom = classroom;
        this.subject = subject;
        this.period = period;
    }

    public Lesson(Teacher teacher, Classroom classroom, Subject subject, Period period) {
        this.teacher = teacher;
        this.classroom = classroom;
        this.subject = subject;
        this.period = period;
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
        teacher.addToRoster(this);
        classroom.addToRoster(this);
        if (group != null) {
            group.addToRoster(this);
        }
    }

    public void removeLessonFromOthers() {
        teacher.removeFromRoster(this);
        classroom.removeFromRoster(this);
        if (group != null) {
            group.removeFromRoster(this);
        }
        this.teacher = null;
        this.group = null;
        this.classroom = null;
    }
}
