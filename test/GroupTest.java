import com.company.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    public Group group1 = new Group("Group 1", 10);
    public Group group2 = new Group("Group 2", 10);
    public Group group3 = new Group("Group 2", 10);
    Student s1 = new Student("Thom", 1998);
    Student s2 = new Student("Thom", 1998, group3);

    Teacher teacher = new Teacher("Alexander H", 1757);
    Classroom classroom = new Classroom("1.1", ClassroomType.BASIC);
    Subject subject = new Subject("English", 3, ClassroomType.BASIC);
    Period period = new Period(Period.Day.MONDAY, Period.Block.FIRST);
    Lesson lesson = new Lesson(teacher, group3, classroom, subject, period);

    @Test
    public void testGroupGetters() {
            assertEquals(group1.getName(), "Group 1");
            assertEquals(group1.getMaxCapacity(), 10);
            assertTrue(group1.getStudents().isEmpty());
    }

    @Test
    public void testAddStudentToGroup() {
        group1.addStudentToGroup(s1);

        assertFalse(group1.getStudents().isEmpty());
        assertEquals(group1.getStudents().get(0).getName(), "Thom");

//        Testing is student is now assigned to this group
        assertEquals(group1, s1.getGroup());

    }

    @Test
    public void getStudentByCode() {
        group1.addStudentToGroup(s1);
        assertSame(group1.getStudentByCode(s1.getCode()), s1);
    }


    @Test
    public void addingStudentWithExistingGroupException() {
        Student s2 = new Student("Jimmy", 2000, group1);
        assertSame(s2.getGroup(), group1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> group2.addStudentToGroup(s2));

        String expectedMessage = "Student already assigned to a group, remove them first";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRemoveStudentFromGroup() {
//        Test effect in group and student obj
        group1.addStudentToGroup(s1);
        assertFalse(group1.getStudents().isEmpty());
        assertSame(group1, s1.getGroup());

        group1.removeStudentFromGroup(s1);
        assertTrue(group1.getStudents().isEmpty());
        assertNull(s1.getGroup());

    }

    @Test
    public void removingStudentWithNoGroupException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> group1.removeStudentFromGroup(s1));

        String expectedMessage = "Student not in a group, can't remove.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddAndRemoveLessonInGroup() {
        assertSame(group3.getRoster().getLessonsList().size(), 0);
        group3.addToRoster(lesson);
        assertSame(group3.getRoster().getLessonsList().size(), 1);
        assertSame(group3.getRoster().getLessonsList().get(0), s2.getRoster().getLessonsList().get(0));
        group3.removeFromRoster(lesson);
        assertSame(group3.getRoster().getLessonsList().size(), 0);
        assertArrayEquals(group3.getRoster().getLessonsList().toArray(), s2.getRoster().getLessonsList().toArray());
    }

}
