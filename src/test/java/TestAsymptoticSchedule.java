import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestAsymptoticSchedule extends TestCase {

    @Test
    void testAsympt() {
        List<List<Integer>> xyTime = new ArrayList<>();
        int[][] arr = {{1,2,1,3,2,4,4}, {1,3,2,2,3,4,5}, {2,4,5,5,1,2,1}, {3,4,3,4,1,1,1},
                {3,5,6,1,1,2,3}, {4,6,4,2,2,2,2}, {5,6,5,3,2,1,2}, {5,7,2,2,1,2,1}, {6,8,2,1,1,1,1}, {7,8,3,1,2,1,1}};
        for (int i = 0; i < 10; i ++){
            List<Integer> elements = new ArrayList<>();
            for (int j = 0; j < 7; j ++){
                elements.add(arr[i][j]);
            }
            xyTime.add(elements);
        }

        int[] Bres = {13, 8, 7, 8};
        int[][] asymptSchedule;
        asymptSchedule = AsymptoticScheduleDouble.asymptSchedule(8 , xyTime, 20, Bres);
        System.out.println(asymptSchedule);
    }
}

