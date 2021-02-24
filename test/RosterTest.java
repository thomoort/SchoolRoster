import com.company.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class RosterTest {

    Teacher teacher = new Teacher("Alexander H", 1757);

    Group group = new Group("Group 1", 10);

    Student student = new Student("Thom", 1998, group);
    Student student2 = new Student("Tim", 1998, group);

    Classroom classroom = new Classroom("1.1", ClassroomType.BASIC);
    Classroom classroomS = new Classroom("Science", ClassroomType.SCIENCE);

    Subject subject = new Subject("English", 3, ClassroomType.BASIC);
    Subject subject2 = new Subject("Science", 2, ClassroomType.SCIENCE);

    Period period = new Period(Period.Day.MONDAY, Period.Block.FIRST);
    Period period2 = new Period(Period.Day.MONDAY, Period.Block.SECOND);
    Period period3 = new Period(Period.Day.TUESDAY, Period.Block.SECOND);

    Lesson l1 = new Lesson(teacher, group, classroom, subject, period);
    Lesson l2 = new Lesson(teacher, classroomS, subject2, period2);
    Lesson l3 = new Lesson(teacher, classroomS, subject2, period3);


    Roster roster = new Roster();

    @Test
    public void testAddLessonToRoster() {
        assertArrayEquals(roster.getLessons(), new Lesson[5][4]);
        assertEquals(l1.getSubject().getName(), "English");
        roster.addLesson(l1);
        assertSame(roster.getLessons()[0][0], l1);
    }


    @Test
    public void testRosterOfStudentsUpdatingWithGroupAndNotDuplicating() {
        /*
         * Bit much here, but first student and student2 in group one are assigned lesson 1
         * then student2 adds their own lesson, lesson 2
         * so their groups' roster is equal, but student two has another lesson where student does not.
         * then the group is assigned to lesson 2, and therefore the students get updated rosters
         * The students should then have identical rosters, because student now also has lesson 2, and student 2 keeps it.
         */

        group.addLesson(l1);
        student2.addLesson(l2);

        assertNotSame(student.getRoster(), student2.getRoster());
        assertSame(student.getGroup().getRoster(), student2.getGroup().getRoster());

        assertEquals(student.getLessonsForDayList(Period.Day.MONDAY).size(), 1);
        assertEquals(student2.getLessonsForDayList(Period.Day.MONDAY).size(),2);

        assertNull(l2.getGroup());
        l2.setGroup(group);
        assertSame(student.getGroup(), l2.getGroup());
        assertNotNull(l2.getGroup());
        group.addLesson(l2);

        assertEquals(group.getLessonsForDayList(Period.Day.MONDAY).size(), 2);
        assertArrayEquals(student.getLessons(), student2.getLessons());
        assertNotSame(student.getRoster(), student2.getRoster());
        assertSame(student.getGroup().getRoster(), student2.getGroup().getRoster());

    }

    @Test
    public void testLessonsForDay() {
        roster.addLesson(l1);
        roster.addLesson(l2);
        roster.addLesson(l3);

        assertSame(roster.getLessonsList().size(), 3);
        assertSame(roster.getLessonsForDay(period3.getDay()).length, 4);
        assertSame(roster.getLessonsForDayList(period.getDay()).size(), 2);
        assertSame(roster.getLessonsForDayList(period3.getDay()).size(), 1);
        assertSame(roster.getLessonsForDayList(Period.Day.FRIDAY).size(), 0);
    }

    @Test
    public void testSubjectOccurrence() {
        roster.addLesson(l1);
        roster.addLesson(l2);
        roster.addLesson(l3);

        assertSame(roster.getSubjectOccurrence(subject), 1);
        assertSame(roster.getSubjectOccurrence(subject2), 2);
    }

    @Test
    public void testRemoveLessons() {
        roster.addLesson(l1);
        roster.addLesson(l2);
        roster.addLesson(l3);

        assertSame(roster.getLessonsList().size(),3);
        roster.removeLesson(l1);
        assertSame(roster.getLessonsList().size(),2);
        roster.removeLesson(l2);
        assertSame(roster.getLessonsList().size(), 1);
    }

}
