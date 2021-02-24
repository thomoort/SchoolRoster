package com.company;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private final static ArrayList<Classroom> classrooms = new ArrayList<>();
    private final static ArrayList<Subject> currentSubjects = new ArrayList<>();
    private final static ArrayList<Teacher> teachers = new ArrayList<>();
    private final static ArrayList<Group> groups = new ArrayList<>();
    private final static ArrayList<Lesson> lessons = new ArrayList<>();
    private final static ArrayList<Period> periods = new ArrayList<>();
    private final static ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
	// write your code here
        initClassrooms();
        initSubjects();
        initTeachers();
        initGroups();
        initPeriods();
        initStudents();
        rosterGenerator();
    }

    /**
     * First test case showed a discrepancy between the two groups, the former getting most of the higher weight subjects
     * I assume this is because I do not compare the groups subject weight list to the other group(s). I will implement this now by checking the total weight.
     *  - This might not be a good solution, because the current weight distribution is 47 - 44, what we want is a per subject weight comparison,
     *      there we'll find bigger differences, like for english 15 - 9, and geography/history 6, 10
     */
    public static void rosterGenerator() {
        RosterGenerator rosterGenerator = new RosterGenerator(classrooms, currentSubjects, teachers, groups, lessons,
                periods, students);
        rosterGenerator.generateRostersForGroups();
        for (Group g : groups) {
            printRoster(g.getName(), g.getRoster());
        }
        for (Teacher t : teachers) {
            printRoster(t.getName(), t.getRoster());
        }
    }

    private static void printRoster(String name, Roster roster) {
        System.out.println("\n");
        System.out.printf("%s roster:%n", name);
        for (Lesson l : roster.getLessonsList()) {
            System.out.printf("%s,%s,%s,%s,%s%n", l.getPeriod().getDayString(), l.getPeriod().getBlockString(),
                    l.getTeacher().getName(), l.getSubject().getName(), l.getClassroom().getCode());
        }
    }

    public static void initClassrooms() {
        classrooms.add(new Classroom("1.1", ClassroomType.BASIC));
        classrooms.add(new Classroom("1.2", ClassroomType.BASIC));
        classrooms.add(new Classroom("1.3", ClassroomType.SCIENCE));
        classrooms.add(new Classroom("Gym", ClassroomType.GYM));
    }

    public static void initSubjects() {
        currentSubjects.add(new Subject("English", 3, ClassroomType.BASIC));
        currentSubjects.add(new Subject("Math", 3, ClassroomType.BASIC));
        currentSubjects.add(new Subject("Science", 2, ClassroomType.SCIENCE));
        currentSubjects.add(new Subject("Geography", 2, ClassroomType.BASIC));
        currentSubjects.add(new Subject("History", 2, ClassroomType.BASIC));
        currentSubjects.add(new Subject("Gym", 1, ClassroomType.GYM));
    }

    public static void initTeachers(){
        Teacher teacher1 = new Teacher("John Addams", 1980);
        Teacher teacher2 = new Teacher("Alexander Hamilton", 1970);
        Teacher teacher3 = new Teacher("George Washington", 2000);

        teachers.add(teacher1);
        teachers.add(teacher2);
        teachers.add(teacher3);

        teacher1.addAssignedSubjects( new Subject[]{currentSubjects.get(0), currentSubjects.get(1), currentSubjects.get(2)});
        teacher1.addAssignedSubjects( new Subject[]{currentSubjects.get(0), currentSubjects.get(1)});
        teacher2.addAssignedSubjects( new Subject[]{currentSubjects.get(3), currentSubjects.get(4), currentSubjects.get(5)});
        teacher2.addAssignedSubjects( new Subject[]{currentSubjects.get(2), currentSubjects.get(3)});
        teacher3.addAssignedSubjects(new Subject[]{currentSubjects.get(0), currentSubjects.get(2), currentSubjects.get(4)});
    }

    public static void initGroups() {
        Group group1 = new Group("Group 1", 10);
        Group group2 = new Group("Group 2", 10);

        groups.add(group1);
        groups.add(group2);
    }

    public static void initPeriods() {
        Period.Day[] days = Period.Day.class.getEnumConstants();
        Period.Block[] blocks = Period.Block.class.getEnumConstants();

        for (Period.Day d : days) {
            for (Period.Block b: blocks) {
                periods.add(new Period(d, b));
            }
        }
    }

    public static void initStudents() {
        String[] firstNamesBoys = {"Liam", "Noah", "Oliver", "William", "Elijah", "James", "Benjamin", "Lucas", "Mason", "Ethan"};
        String[] firstNamesGirls = {"Olivia", "Emma", "Ava", "Sophia", "Isabella", "Charlotte", "Amelia", "Mia", "Harper", "Evelyn"};

        for (int i=0; i < 20; i++) {
            int birthYear = ThreadLocalRandom.current().nextInt(1996, 2001);
            Group group = i % 2 == 0 ? groups.get(0) : groups.get(1);
            String name = i < 10 ? firstNamesBoys[i] : firstNamesGirls[i-10];

            students.add(new Student(name, birthYear, group));
        }
    }

}
