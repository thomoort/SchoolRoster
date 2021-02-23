package com.company;

import java.util.ArrayList;

public class Subject {

    public static int TOTAL_WEIGHT_DIST  = 0;

    private String name;
    private int weight;
    private ClassroomType requiredClassroomType;
    private ArrayList<Teacher> qualifiedTeachers = new ArrayList<Teacher>();

    public Subject(String name, int weight, ClassroomType requiredClassroomType) {
        this.name = name;
        this.weight = weight;
        this.requiredClassroomType = requiredClassroomType;
        TOTAL_WEIGHT_DIST += weight;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return weight;
    }

    public ClassroomType getRequiredClassroomType() {
        return requiredClassroomType;
    }

    public ArrayList<Teacher> getQualifiedTeachers() {
        return qualifiedTeachers;
    }

    /**
     * Get's called from Teacher when a teacher is assigned this subject. The teacher will add this subject to it's assigned list
     * and this subject will add the teacher to it's qualified list.
     * @throws IllegalArgumentException when the teacher is already qualified to teach this subject.
     * @param teacher The teacher qualified to teach this subject.
     */
    public void addQualifiedTeacher(Teacher teacher) {
        if (qualifiedTeachers.contains(teacher)) {
            throw new IllegalArgumentException("Teacher already qualified to teach subject");
        }
        qualifiedTeachers.add(teacher);
    }

    public void removeQualifiedTeacher(Teacher teacher) {
        qualifiedTeachers.remove(teacher);
    }


}
