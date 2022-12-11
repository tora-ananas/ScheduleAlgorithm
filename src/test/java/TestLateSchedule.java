import OldVersion.LateSchedule;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class TestLateSchedule extends TestCase{
    @Test
    void testLateMoments() {
        int[][] xyTime = {{1,2,1}, {1,3,2}, {2,4,5}, {3,4,3},
                {3,5,6}, {4,6,4}, {5,6,5}, {5,7,2}, {6,8,2}, {7,8,3}};
        int[] lateMoments = LateSchedule.lateMoments(15, xyTime, 8);
        for (int i = 0; i < 10; i++){
            System.out.println(lateMoments[i]);
        }
    }
}
