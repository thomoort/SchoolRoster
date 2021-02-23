package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Roster {

//    private ArrayList<Lesson> lessons = new ArrayList<>();
    private final Lesson[][] lessons = new Lesson[5][4];
    public static final int LESSONS_PER_WEEK = 20;

    public Roster() {

    }

    public Lesson[][] getLessons() {
        return lessons;
    }

    public List<Lesson> getLessonsList() {
        return Arrays.stream(lessons).flatMap(l -> Arrays.stream(l).filter(Objects::nonNull)).collect(Collectors.toList());
    }

    public Lesson[] getLessonsForDay(Period.Day day) {
        return lessons[Period.getDayInt(day)];
    }

    public List<Lesson> getLessonsForDayList(Period.Day day) {
        return Arrays.stream(lessons[Period.getDayInt(day)]).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public int getRosterLessonsTotalWeight() {
        int totalWeight = 0;
        for (Lesson l : getLessonsList()) {
            totalWeight += l.getSubject().getWeight();
        }
        return totalWeight;
    }

    public int getSubjectOccurrence(Subject subject) {
        int occurrence = 0;
        for (Lesson l : getLessonsList()) {
            if (l.getSubject().equals(subject)) {
                occurrence++;
            }
        }
        return occurrence;
    }

    public void addLesson(Lesson lesson) {
        Period period = lesson.getPeriod();
        lessons[Period.getDayInt(period.getDay())][Period.getBlockInt(period.getBlock())] = lesson;
//        System.out.printf("adding lesson to day: %s,  block: %s%n", Period.getDayInt(period.getDay()), Period.getBlockInt(period.getBlock()));
    }

    public void removeLesson(Lesson lesson) {
        Period period = lesson.getPeriod();
        lessons[Period.getDayInt(period.getDay())][Period.getBlockInt(period.getBlock())] = null;
    }

}
