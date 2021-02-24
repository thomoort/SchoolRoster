import com.company.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LessonTest {

    Teacher teacher = new Teacher("Alexander H", 1757);
    Group group = new Group("Group 1", 10);
    Classroom classroom = new Classroom("1.1", ClassroomType.BASIC);
    Subject subject = new Subject("English", 3, ClassroomType.BASIC);
    Period period = new Period(Period.Day.MONDAY, Period.Block.FIRST);

    @Test
    public void createLessonAndAssign() {
        Lesson lesson = new Lesson(teacher, group, classroom, subject, period);
        assertTrue(teacher.getLessonsList().isEmpty());
        lesson.assignLessonToOthers();
        assertSame(lesson, teacher.getLessonsList().get(0));
        assertSame(lesson.getTeacher(), teacher);
        assertSame(lesson.getClassroom(), classroom);
        assertSame(lesson.getSubject(), subject);
        assertSame(lesson.getPeriod(), period);
        assertSame(lesson.getGroup(), group);
    }

    @Test
    public void removeLessonFromOthers() {
        Lesson lesson = new Lesson(teacher, group, classroom, subject, period);
        lesson.assignLessonToOthers();
        assertSame(lesson, teacher.getLessonsList().get(0));
        lesson.removeLessonFromOthers();
        assertFalse(teacher.getLessonsList().contains(lesson));
        assertFalse(classroom.getLessonsList().contains(lesson));
        assertFalse(group.getLessonsList().contains(lesson));
    }

    @Test
    public void testAddAndRemoveWithoutGroup() {
        Lesson lesson = new Lesson(teacher, classroom, subject, period);
        lesson.assignLessonToOthers();
        assertFalse(group.getLessonsList().contains(lesson));
        assertTrue(teacher.getLessonsList().contains(lesson));

        lesson.removeLessonFromOthers();
        assertFalse(teacher.getLessonsList().contains(lesson));
        assertNull(lesson.getTeacher());

    }

}
