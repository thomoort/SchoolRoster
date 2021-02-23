import com.company.Period;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeriodTest {

    Period initPeriod = new Period(Period.Day.FRIDAY, Period.Block.FOURTH);;

    @Test
    public void TestCreatePeriod() {

        Period mondayBlock1 = new Period(Period.Day.MONDAY, Period.Block.FIRST);
        Period wednesdayBlock4 = new Period(Period.Day.WEDNESDAY, Period.Block.FOURTH);

        assertEquals(mondayBlock1.getDay(), Period.Day.MONDAY);
        assertEquals(mondayBlock1.getBlock(), Period.Block.FIRST);

        assertEquals(wednesdayBlock4.getDay(), Period.Day.WEDNESDAY);
        assertEquals(wednesdayBlock4.getBlock(), Period.Block.FOURTH);

    }

    @Test
    public void testDay() {
        assertSame(initPeriod.getDay(), Period.Day.FRIDAY);
    }

    @Test
    public void testBlock() {
        assertSame(initPeriod.getBlock(), Period.Block.FOURTH);
    }

    @Test
    public void testPrintDay() {
        assertEquals(initPeriod.getDayString(), "Friday");
    }

    @Test
    public void testPrintBlock() {
        assertEquals(initPeriod.getBlockString(), "14:30 - 16:00");
    }

    @Test
    public void testToString() {
        String expected = "Period is from 14:30 - 16:00 on friday.";
        assertEquals(initPeriod.toString(), expected);
    }

}
