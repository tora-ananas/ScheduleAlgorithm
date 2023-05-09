import OldVersion.LateSchedule;
import OldVersion.TimeCritical;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class TestTimeCritical extends TestCase{
    @Test
    void testTimeCritical(){
        int[][] xyTime = {{1,2,1}, {1,3,2}, {2,4,5}, {3,4,3},
                {3,5,6}, {4,6,4}, {5,6,5}, {5,7,2}, {6,8,2}, {7,8,3}};
        int earlyMoment = TimeCritical.earlyMoment(xyTime, 8);
        System.out.println(earlyMoment);

    }

    @Test
    void testSecondTimeCritical() {
        int [][] xyTime = {{1,2,2}, {1,3,1}, {1,4,5}, {2,4,2},
                {2,6,6}, {2,7,5}, {3,4,2}, {3,5,7}, {4,5,6}, {4,6,2},
                {5,8,4}, {6,8,7}, {6,7,2}, {7,8,4}};
        int earlyMoment = TimeCritical.earlyMoment(xyTime, 8);
        System.out.println(earlyMoment);
    }

}
