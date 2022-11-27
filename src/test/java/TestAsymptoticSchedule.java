import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestAsymptoticSchedule extends TestCase {

    @Test
    void testAsympt() {
        int[][] xyTime = {{1,2,1}, {1,3,2}, {2,4,5}, {3,4,3},
                {3,5,6}, {4,6,4}, {5,6,5}, {5,7,2}, {6,8,2}, {7,8,3}};
        //an array with a graph of resource use for each job for different types of resources
        int[][] Rt = {{1,1} , {2,2}, {5,5}, {3,3}, {6,6}, {4,4}, {5,5}, {2,2}, {2,2}, {3,3}};
        int[] Bres = new int[15];
        Arrays.fill(Bres, 5);
        int[] asymptSchedule;
        asymptSchedule = AsymptoticSchedule.asymptSchedule(8 , xyTime, 30, Rt, Bres);
        System.out.println(asymptSchedule);
    }
}
