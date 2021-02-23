package com.company;

public class Classroom {

    private String code;
    private ClassroomType classroomType;
    private Roster roster = new Roster();

    public Classroom(String code, ClassroomType classroomType) {
        this.code = code;
        this.classroomType = classroomType;
    }

    public String getCode() {
        return code;
    }

    public ClassroomType getClassroomType() {
        return classroomType;
    }

    public void addToRoster(Lesson l) {
        roster.addLesson(l);
    }

    public Roster getRoster() {
        return roster;
    }

}
