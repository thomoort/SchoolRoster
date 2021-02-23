import com.company.Subject;
import com.company.ClassroomType;
import com.company.Teacher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectTest {

    Teacher t1 = new Teacher("John", 1980);
    Teacher t2 = new Teacher("Burr", 1760);

    Subject s1 = new Subject("English", 3, ClassroomType.BASIC);
    Subject s2 = new Subject("Science", 2, ClassroomType.SCIENCE);
    Subject s3 = new Subject("Gym", 1, ClassroomType.GYM);

    @Test
    public void testSubjectGetters() {
        assertEquals(s1.getName(), "English");
        assertEquals(s1.getRequiredClassroomType(), ClassroomType.BASIC);

        assertEquals(s2.getName(), "Science");
        assertEquals(s2.getRequiredClassroomType(), ClassroomType.SCIENCE);
    }

//    Should be done in teacher test suite
    @Test
    public void testAssigningTeacher() {
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
    public void testQualifyingTeacher() {
        assertTrue(s1.getQualifiedTeachers().isEmpty());
        s1.addQualifiedTeacher(t1);
        s1.addQualifiedTeacher(t2);
        assertFalse(s1.getQualifiedTeachers().isEmpty());
        assertSame(s1.getQualifiedTeachers().get(0), t1);
    }

    @Test
    public void testExceptionWhenQualifyingAlreadyQualified(){
        s1.addQualifiedTeacher(t1);
        assertSame(s1.getQualifiedTeachers().get(0), t1);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
           s1.addQualifiedTeacher(t1);
        });

        String expectedMessage = "Teacher already qualified to teach subject";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRemoveQualifiedTeacher() {
        s1.addQualifiedTeacher(t1);
        assertSame(s1.getQualifiedTeachers().get(0), t1);

        s1.removeQualifiedTeacher(t1);
        assertTrue(s1.getQualifiedTeachers().isEmpty());
        assertFalse(t1.getAssignedSubjects().contains(s1));
    }

}
