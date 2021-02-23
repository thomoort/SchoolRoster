import com.company.Student;
import com.company.Group;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    Group g1 = new Group("Group 1", 10);

    @Test
    public void testStudentCreateNoGroup() {
        Student s1 = new Student("Thom", 1998);

        assertEquals(s1.getName(), "Thom");
        assertEquals(s1.getBirthYear(), 1998);
        assertEquals(s1.getCode(), "Thom1998");
        assertNull(s1.getGroup());
        assertTrue(g1.getStudents().isEmpty());
    }

    @Test
    public void testStudentCreateWithGroup() {
        Student s = new Student("Thom", 1998, g1);

        assertEquals(s.getName(), "Thom");
        assertEquals(s.getBirthYear(), 1998);
        assertEquals(s.getCode(), "Thom1998");
        assertSame(s.getGroup(), g1);
        assertFalse(g1.getStudents().isEmpty());
        assertSame(g1.getStudents().get(0), s);
    }

    @Test
    public void testAddStudentToGroup() {
        Student s = new Student("Billy", 1996);
        assertNull(s.getGroup());

        g1.addStudentToGroup(s);
        assertSame(s.getGroup(), g1);
        assertSame(g1.getStudents().get(0), s);

    }

    @Test
    public void testRemoveStudentFromGroup() {
        Student s = new Student("Hamilton", 1757, g1);
        assertSame(s.getGroup(), g1);
        
        g1.removeStudentFromGroup(s);
        assertTrue(g1.getStudents().isEmpty());
        assertNull(s.getGroup());
    }

}
