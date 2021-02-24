import com.company.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    Teacher t1 = new Teacher("John", 1980);
    Teacher t2 = new Teacher("Burr", 1760);

    Subject s1 = new Subject("English", 3, ClassroomType.BASIC);
    Subject s2 = new Subject("Science", 2, ClassroomType.SCIENCE);
    Subject s3 = new Subject("Gym", 1, ClassroomType.GYM);

    Group group = new Group("Group 1", 10);
    Classroom classroom = new Classroom("1.1", ClassroomType.BASIC);
    Period period = new Period(Period.Day.MONDAY, Period.Block.FIRST);
    Lesson lesson = new Lesson(t1, group, classroom, s1, period);

    @Test
    public void testTeacherGetters() {
        assertEquals(t1.getName(), "John");
        assertEquals(t1.getBirthYear(), 1980);
        assertEquals(t1.getCode(), "John1980");
        assertTrue(t1.getAssignedSubjects().isEmpty());
    }

    @Test
    public void testAddingAssignedSubject() {
        t1.addAssignedSubject(s1);
        assertSame(t1.getAssignedSubjects().get(0), s1);

        t1.addAssignedSubject(s2);
        assertSame(t1.getAssignedSubjects().get(0), s1);
        assertSame(t1.getAssignedSubjects().get(1), s2);
    }

    @Test
    public void testAddingAssignedSubjects() {
        t1.addAssignedSubjects(new Subject[]{s1, s2});

        assertFalse(t1.getAssignedSubjects().isEmpty());
        assertArrayEquals(t1.getAssignedSubjects().toArray(), new Subject[]{s1, s2});
    }

    @Test
    public void testAddingAssignedSubjectsIntegrationTest() {
        t1.addAssignedSubjects(new Subject[]{s1, s2});
        t2.addAssignedSubject(s2);
        t2.addAssignedSubject(s3);

        assertFalse(t1.getAssignedSubjects().isEmpty());
        assertArrayEquals(t1.getAssignedSubjects().toArray(), new Subject[]{s1, s2});
        assertTrue(t2.getAssignedSubjects().size() > 1);
        assertSame(t2.getAssignedSubjects().get(1), s3);

        assertSame(s1.getQualifiedTeachers().toArray()[0], t1);
        assertArrayEquals(s2.getQualifiedTeachers().toArray(), new Teacher[]{t1, t2});
    }

    @Test
    public void testRemovingAssignedSubject() {
        t1.addAssignedSubject(s1);
        assertSame(t1.getAssignedSubjects().get(0), s1);
        assertSame(s1.getQualifiedTeachers().get(0), t1);

        t1.removeAssignedSubject(s1);
        assertTrue(t1.getAssignedSubjects().isEmpty());
        assertTrue(s1.getQualifiedTeachers().isEmpty());
    }

    @Test
    public void testAddingAlreadyAssignedSubjectException() {
        t1.addAssignedSubject(s1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> t1.addAssignedSubject(s1));

        String expectedMessage = "Subject already assigned to teacher";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddingUnassignedSubjectWhenTeacherQualified() {
        s1.addQualifiedTeacher(t1);
        assertFalse(s1.getQualifiedTeachers().isEmpty());
        assertTrue(t1.getAssignedSubjects().isEmpty());

        t1.addAssignedSubject(s1);
        assertSame(t1.getAssignedSubjects().get(0), s1);
        assertSame(s1.getQualifiedTeachers().get(0), t1);
        assertEquals(s1.getQualifiedTeachers().size(), 1);
    }

    @Test
    public void testAssigningSubjectsOneAlreadyAssigned() {
        t1.addAssignedSubject(s1);
        assertSame(t1.getAssignedSubjects().get(0), s1);

        t1.addAssignedSubjects(new Subject[]{s1, s2});
        assertArrayEquals(t1.getAssignedSubjects().toArray(), new Subject[]{s1,s2});
        assertSame(s1.getQualifiedTeachers().get(0), t1);
    }

    @Test
    public void testAssigningSubjectsOneAlreadyQualified() {
        s1.addQualifiedTeacher(t1);
        assertFalse(s1.getQualifiedTeachers().isEmpty());
        assertTrue(t1.getAssignedSubjects().isEmpty());

        t1.addAssignedSubjects(new Subject[]{s1, s2});
        assertArrayEquals(t1.getAssignedSubjects().toArray(), new Subject[]{s1, s2});
        assertSame(s1.getQualifiedTeachers().get(0), t1);
    }

    @Test
    public void testAddLesson() {
        t1.addLesson(lesson);
        assertSame(t1.getLessonsList().get(0), lesson);
        t1.removeLesson(lesson);
        assertTrue(t1.getLessonsList().isEmpty());
    }

}
