package com.company;

public class Student {

    private String name;
    private int birthYear;
    private String code;
    private Group group;
    private Roster roster = new Roster();

    public Student(String name, int birthYear, Group group) {
        this.name = name;
        this.birthYear = birthYear;
        this.code = setCode();
        group.addStudentToGroup(this);
    }

    public Student(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.code = setCode();
    }

    private String setCode() {
        String[] splitName = this.name.split(" ");
        return "%s%d".formatted(splitName[0], birthYear);
    }

    public void addToRoster(Lesson lesson) {
        roster.addLesson(lesson);
    }

    public void removeFromRoster(Lesson lesson) {
        roster.removeLesson(lesson);
    }

    public Roster getRoster() {
        return roster;
    }

    public Roster getGroupRoster() {
        return group.getRoster();
    }

    public String getName() {
        return this.name;
    }

    public int getBirthYear() {
        return this.birthYear;
    }

    public String getCode() {
        return this.code;
    }

    public Group getGroup() {
        return this.group;
    }

    public void addToGroup(Group group) { this.group = group; }

    public void removeFromGroup() {
        this.group = null;
    }

}
