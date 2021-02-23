import com.company.Classroom;
import com.company.ClassroomType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClassroomTest {

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

}
