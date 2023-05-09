
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestLateSchedule extends TestCase{
    /*@Test
    void testLateMoments() {
        int[][] xyTime = {{1,2,1}, {1,3,2}, {2,4,5}, {3,4,3},
                {3,5,6}, {4,6,4}, {5,6,5}, {5,7,2}, {6,8,2}, {7,8,3}};
        int[] lateMoments = LateSchedule.lateMoments(15, xyTime, 8);
        for (int i = 0; i < 10; i++){
            System.out.println(lateMoments[i]);
        }
    }*/

    /*@Test
    void testSecondLateMoments() {
        int [][] xyTime = {{1,2,2}, {1,3,1}, {1,4,5}, {2,4,2},
                {2,6,6}, {2,7,5}, {3,4,2}, {3,5,7}, {4,5,6}, {4,6,2},
                {5,8,4}, {6,8,7}, {6,7,2}, {7,8,4}};
        int[] lateMoments = LateSchedule.lateMoments(15, xyTime, 8);
        for (int i = 0; i < 14; i++){
            System.out.println(lateMoments[i]);
        }
    }*/

    @Test
    void testLateScheduleOne() {
        List<List<Integer>> xyTime = new ArrayList<>();
        List<Integer> elem;
        int[][] elements = {{1,2,2}, {2,3,3}, {2,4,5}, {3,4,4}, {3,5,9}, {4,6,8},
                {5,7,2}, {6,5,1}, {6,7,6}};
        for (int i = 0; i < elements.length; i ++){
            elem = new ArrayList<>();
            elem.add(elements[i][0]);
            elem.add(elements[i][1]);
            elem.add(elements[i][2]);
            xyTime.add(elem);
        }
        for (int j = 0; j < xyTime.size(); j ++){
            System.out.println(xyTime.get(j));
        }
        int L = TimeCritical.earlyMoment(xyTime, 7);
        System.out.println("Time Critical: " + L);
        int[] lateMoments = LateSchedule.lateMoments(L, xyTime, 7);
        for (int i = 0; i < lateMoments.length; i++){
            System.out.println(lateMoments[i]);
        }
    }
}
