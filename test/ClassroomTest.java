import com.company.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClassroomTest {

    Teacher teacher = new Teacher("Alexander H", 1757);
    Group group = new Group("Group 1", 10);
    Classroom classroom = new Classroom("1.1", ClassroomType.BASIC);
    Subject subject = new Subject("English", 3, ClassroomType.BASIC);
    Period period = new Period(Period.Day.MONDAY, Period.Block.FIRST);


    @Test
    public void testClassroom() {
        Classroom room1Basic = new Classroom("1.1", ClassroomType.BASIC);
        Classroom room2Basic = new Classroom("Room 2", ClassroomType.BASIC);
        Classroom roomScience = new Classroom("Science Room", ClassroomType.SCIENCE);
        Classroom roomGym = new Classroom("Gym 1.0", ClassroomType.GYM);

        assertEquals("1.1", room1Basic.getCode());
        assertEquals(ClassroomType.BASIC, room1Basic.getClassroomType());
        assertEquals(ClassroomType.SCIENCE, roomScience.getClassroomType());
        assertEquals(ClassroomType.GYM, roomGym.getClassroomType());
    }

    @Test
    public void testClassroomRoster() {
        assertTrue(classroom.getRoster().getLessonsList().isEmpty());

        Lesson l1 = new Lesson(teacher, group, classroom, subject, period);
        classroom.addToRoster(l1);

        assertSame(classroom.getRoster().getLessonsList().get(0), l1);
    }

    @Test
    public void testAddAndRemoveLessonFromRoster() {
        Lesson l1 = new Lesson(teacher, group, classroom, subject, period);
        l1.assignLessonToOthers();
        assertSame(classroom.getRoster().getLessonsList().get(0), l1);
        classroom.removeFromRoster(l1);
        assertSame(classroom.getRoster().getLessonsList().size(), 0);
    }

}
