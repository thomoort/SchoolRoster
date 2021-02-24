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
        for (Period p : periods) {
            for (Group g : groups) {

                Roster roster = g.getRoster();
                ArrayList<Teacher> avlbTeachers = getAvailableTeachersForPeriod(p);
                ArrayList<Classroom> avlbClassrooms = getAvailableClassroomsForPeriod(p);
                HashMap<Subject, Float[]> subjectRepresentationPercentages = getSubjectRepresentationPercentages(g);

                try {
                    Subject selectedSubject = selectSubjectForGroup(g, subjectRepresentationPercentages, avlbTeachers, avlbClassrooms);
                    Teacher selectedTeacher = selectedSubject.getQualifiedTeachers().stream().filter(avlbTeachers::contains).collect(Collectors.toList()).get(0);
                    List<Classroom> possibleClassrooms = avlbClassrooms.stream().filter(c -> c.getClassroomType() == selectedSubject.getRequiredClassroomType()).collect(Collectors.toList());
                    Classroom selectedClassroom = possibleClassrooms.get(0);
                    Lesson generatedLesson = new Lesson(selectedTeacher, g, selectedClassroom, selectedSubject, p);
                    generatedLesson.assignLessonToOthers();
                } catch (NullPointerException e) {
                    System.out.println("No lesson could be created for " + g.getName() + " on " + p.getDayString() +  ", " + p.getBlockString());
                }

            }
        }
    }

    /**
     * A (recursive) method which chooses the next subject based on what's available, and given the current lessons, which one needs more representation in the roster.
     * Uses other groups to find if they need the subject more, in which case the teacher and classroom are removed from the available pool, and the method is called again without them
     * @param group The group for which the subject is requested
     * @param subjectRepMap A hashmap of subject representation with current rep, desired rep
     * @param avlbTeachers A list of available teachers
     * @param avlbClassrooms A list of available classrooms
     * @return The chosen subject
     */
    public Subject selectSubjectForGroup(Group group, HashMap<Subject, Float[]> subjectRepMap,
                                         List<Teacher> avlbTeachers, List<Classroom> avlbClassrooms) {
        Subject selectedSubject = null;
        ArrayList<Teacher> avlbTeachersCopy = new ArrayList<>(avlbTeachers);
        ArrayList<Classroom> avlbClassroomsCopy = new ArrayList<>(avlbClassrooms);

        float repDiff = 0;
        for (Subject s : subjectRepMap.keySet()) {
            //Continues if there's no teacher or classroom available for the current looped subject.
            if (s.getQualifiedTeachers().stream().noneMatch(avlbTeachersCopy::contains) ||
                    avlbClassroomsCopy.stream().noneMatch(c -> c.getClassroomType() == s.getRequiredClassroomType())) {
                continue;
            }

            /*Note on representation: current rep is a percentage float between 0.0 and 1.0
            The desired rep is based on subject weight / weight of all subjects, so 3 / 12 = .25
            currRepDiff is the current rep - the desired, so the lowest value (usually negative) is the subject most in need of another lesson.
            OthersMaxRepDiff does the same calc for all other groups, but returns only the greatest difference, so we know if another group needs the lesson more */
            float currentRep = subjectRepMap.get(s)[0];
            float desireRep = subjectRepMap.get(s)[1];
            float currRepDiff = currentRep - desireRep;
            float othersMaxRepDiff = getOtherGroupsMaxDiffForSubject(group, s);

            //If others need the subject more, and the list of availalbe teachers & classrooms is not 1, then remove the teacher and classroom and look again
            if ((othersMaxRepDiff < currRepDiff) && !(avlbTeachersCopy.size() <= 1 || avlbClassroomsCopy.size() <= 1)) {
                avlbTeachersCopy.remove(s.getQualifiedTeachers().get(0));
                List<Classroom> possibleUsed = avlbClassroomsCopy.stream().filter(c -> c.getClassroomType() == s.getRequiredClassroomType()).collect(Collectors.toList());
                avlbClassroomsCopy.remove(possibleUsed.get(0));
                selectedSubject = selectSubjectForGroup(group, subjectRepMap, avlbTeachersCopy, avlbClassroomsCopy);
                repDiff = currRepDiff;
            }

            //If no subject yet, or this one has a bigger difference in current representation vs desired representation
            if (selectedSubject == null || currRepDiff < repDiff) {
                selectedSubject = s;
                repDiff = currRepDiff;
            }
        }
        if (selectedSubject == null) {
            throw new NullPointerException("No suitable subject found!");
        }
        return selectedSubject;
    }

    /**
     * Get's called by a group to see if there's another group (doesn't matter which) which has a higher difference in representation,
     * if it does then the subject will not be given to the calling group.
     * @param excludedGroup the group for which the method is called, excluding them so they don't compare to themselves.
     * @param subject The subject for which the greatest representation difference is requested
     * @return the biggest difference in curr rep vs des rep for all groups.
     */
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

    /**
     * This method assigns the current and desired representation to all subjects for a group.
     * The representation is based on the how often the subject is given during the week, and it's desired rep is derived from it's weight and the total weight of subjects
     * This is used in {@link RosterGenerator#generateRostersForGroups()} & {@link RosterGenerator#getOtherGroupsMaxDiffForSubject(Group, Subject)}
     * @param group The group for which to get the subject rep map
     * @return A HashMap containing a key value pair of subject, [current rep, desired rep]
     */
    public HashMap<Subject, Float[]> getSubjectRepresentationPercentages(Group group) {
        HashMap<Subject, Float[]> subjectRepresentationPercentages = new HashMap<>();
        Roster roster = group.getRoster();
        for (Subject s : currentSubjects) {
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


}
