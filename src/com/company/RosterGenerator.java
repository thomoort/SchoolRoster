package com.company;
import com.company.*;

import java.util.*;
import java.util.stream.Collectors;

public class RosterGenerator {

    private ArrayList<Classroom> classrooms;
    private ArrayList<Subject> currentSubjects;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;
    private ArrayList<Lesson> lessons;
    private ArrayList<Period> periods;
    private ArrayList<Student> students;

//    private Roster roster = new Roster();

    public RosterGenerator(ArrayList<Classroom> classrooms, ArrayList<Subject> currentSubjects, ArrayList<Teacher> teachers, ArrayList<Group> groups, ArrayList<Lesson> lessons, ArrayList<Period> periods, ArrayList<Student> students) {
        this.classrooms = classrooms;
        this.currentSubjects = currentSubjects;
        this.teachers = teachers;
        this.groups = groups;
        this.lessons = lessons;
        this.periods = periods;
        this.students = students;
    }

    public void generateRostersForGroups() {
        int periodCounter = 0;

        for (Period p : periods) {
            periodCounter++;

            for (Group g : groups) {
                Roster roster = g.getRoster();
                int groupWeight = g.getRoster().getRosterLessonsTotalWeight();
                int desiredWeight = groupWeight / periodCounter;

                ArrayList<Integer> otherWeights = otherWeights(g);

                if (groupWeight > Collections.max(otherWeights)) {
                    int weightDiff = groupWeight - Collections.max(otherWeights);
                    desiredWeight = Math.min(3, Math.max(desiredWeight - weightDiff, 1));
                }

                ArrayList<Teacher> avlbTeachers = getAvailableTeachersForPeriod(p);
                ArrayList<Classroom> avlbClassrooms = getAvailableClassroomsForPeriod(p);

                /*
                Creates a hashmap of subjects and a float array, existing of the current percentage representation and desired representation
                The representation is based on the how often the subject is given during the week, and it's desired rep is derived from it's weight and the total weight of subjects
                It only checks for the current group
                 */
                HashMap<Subject, Float[]> subjectRepresentationPercentages = getSubjectRepresentationPercentages(g);


                Subject selectedSubject = selectSubjectForGroup(g, subjectRepresentationPercentages, avlbTeachers, avlbClassrooms);

                Teacher selectedTeacher = selectedSubject.getQualifiedTeachers().stream().filter(avlbTeachers::contains).collect(Collectors.toList()).get(0);
                List<Classroom> possibleClassrooms = avlbClassrooms.stream().filter(c -> c.getClassroomType() == selectedSubject.getRequiredClassroomType()).collect(Collectors.toList());
                Classroom selectedClassroom = possibleClassrooms.get(0);

                Lesson generatedLesson = new Lesson(selectedTeacher, g, selectedClassroom, selectedSubject, p);
            }
        }
    }

    public Subject selectSubjectForGroup(Group group, HashMap<Subject, Float[]> subjectRepMap,
                                         List<Teacher> avlbTeachers, List<Classroom> avlbClassrooms) {
        Subject selectedSubject = null;
        ArrayList<Teacher> avlbTeachersCopy = new ArrayList<>(avlbTeachers);
        ArrayList<Classroom> avlbClassroomsCopy = new ArrayList<>(avlbClassrooms);

        float repDiff = 0;
        for (Subject s : subjectRepMap.keySet()) {

            if (s.getQualifiedTeachers().stream().noneMatch(avlbTeachersCopy::contains) ||
                    avlbClassroomsCopy.stream().noneMatch(c -> c.getClassroomType() == s.getRequiredClassroomType())) {
                continue;
            }

            float currentRep = subjectRepMap.get(s)[0];
            float desireRep = subjectRepMap.get(s)[1];
            float currRepDiff = currentRep - desireRep;
            float othersMaxRepDiff = getOtherGroupsMaxDiffForSubject(group, s);

            if ((othersMaxRepDiff < currRepDiff) && !(avlbTeachersCopy.size() <= 1 || avlbClassroomsCopy.size() <= 1)) {
                avlbTeachersCopy.remove(s.getQualifiedTeachers().get(0));
                List<Classroom> possibleUsed = avlbClassroomsCopy.stream().filter(c -> c.getClassroomType() == s.getRequiredClassroomType()).collect(Collectors.toList());
                avlbClassroomsCopy.remove(possibleUsed.get(0));
                selectedSubject = selectSubjectForGroup(group, subjectRepMap, avlbTeachersCopy, avlbClassroomsCopy);
                repDiff = currRepDiff;
            }

            if (selectedSubject == null || currRepDiff < repDiff) {
                selectedSubject = s;
                repDiff = currRepDiff;
            }
        }
        return selectedSubject;
    }

    public ArrayList<Integer> otherWeights(Group excludedGroup) {
        ArrayList<Integer> others = new ArrayList<>();
        for (Group g : groups) {
            if (g != excludedGroup) {
                others.add(g.getRoster().getRosterLessonsTotalWeight());
            }
        }
        return others;
    }

    public float getOtherGroupsMaxDiffForSubject(Group excludedGroup, Subject subject) {
        float maxDiff = 0;
        for (Group g : groups.stream().filter(gr -> gr != excludedGroup).collect(Collectors.toList())) {
            HashMap<Subject, Float[]> subjectRepMap = getSubjectRepresentationPercentages(g);
            float currentRep = subjectRepMap.get(subject)[0];
            float desireRep = subjectRepMap.get(subject)[1];
            float currRepDiff = currentRep - desireRep;

            maxDiff = Math.min(maxDiff, currRepDiff);
        }
        return maxDiff;
    }

    public HashMap<Subject, Float[]> getSubjectRepresentationPercentages(Group group) {
        HashMap<Subject, Float[]> subjectRepresentationPercentages = new HashMap<>();
        Roster roster = group.getRoster();
        for (Subject s : currentSubjects) {
            //Filter out subjects with no current available teacher or classroom

            float desiredSubjectRep = (float) s.getWeight() / Subject.TOTAL_WEIGHT_DIST;
            if (roster.getLessonsList().stream().anyMatch(l -> l.getSubject() == s)) {
                float currSubjectRep = (float) roster.getSubjectOccurrence(s) / Roster.LESSONS_PER_WEEK;
                subjectRepresentationPercentages.put(s, new Float[]{currSubjectRep, desiredSubjectRep});
            } else {
                subjectRepresentationPercentages.put(s, new Float[]{0.0f, desiredSubjectRep});
            }
        }
        return subjectRepresentationPercentages;
    }

    public ArrayList<Teacher> getAvailableTeachersForPeriod(Period period) {
        ArrayList<Teacher> availableTeachers = new ArrayList<>();
        for (Teacher t : teachers) {
            if (t.getRoster().getLessonsList().stream().noneMatch(l -> l.getPeriod() == period)) {
                availableTeachers.add(t);
            } else {
                System.out.printf("%s not available%n", t.getName() + " on " + period.getDayString() + ", " + period.getBlockString());
            }
        }
        return availableTeachers;
    }

    public ArrayList<Classroom> getAvailableClassroomsForPeriod(Period p) {
        ArrayList<Classroom> availableClassrooms = new ArrayList<>();
        for (Classroom c : classrooms) {
            if (c.getRoster().getLessonsList().stream().noneMatch(l -> l.getPeriod() == p)) {
                availableClassrooms.add(c);
            }
        }
        return availableClassrooms;
    }


    public Roster generateRoster() {
        return null;
    }

}
