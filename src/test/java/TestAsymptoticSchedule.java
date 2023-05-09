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
        asymptSchedule = AsymptoticSchedule.asymptSchedule(8 , xyTime, 20, Bres);
        double[] weightCoeff = GreedyAlgorithm.weightFunction(asymptSchedule);
        System.out.println(asymptSchedule);
        int[][] greedyAlgorithm = GreedyAlgorithm.greedyAlgorithm(asymptSchedule, 8, 20, Bres);
        System.out.println(greedyAlgorithm);
    }

    @Test
    void testOneGreedy() {
        List<List<Integer>> xyTime = new ArrayList<>();
        int[][] arr = {{1,2,1,1,1,1,1}, {1,3,1,1,1,1,1}, {2,6,1,1,1,1,1}, {3,4,1,1,1,1,1},
                {3,5,1,1,1,1,1}, {4,8,1,1,1,1,1}, {5,8,1,1,1,1,1}, {6,8,1,1,1,1,1},
                {6,7,1,1,1,1,1}, {5,9,1,1,1,1,1}, {8,10,1,1,1,1,1}, {7,10,1,1,1,1,1}, {9,10,1,1,1,1,1}};
        for (int i = 0; i < arr.length; i ++){
            List<Integer> elements = new ArrayList<>();
            for (int j = 0; j < arr[0].length; j ++){
                elements.add(arr[i][j]);
            }
            xyTime.add(elements);
        }

        int[] Bres = {1,1,1,1};
        int[][] asymptSchedule;
        asymptSchedule = AsymptoticSchedule.asymptSchedule(10 , xyTime, 15, Bres);
        double[] weightCoeff = GreedyAlgorithm.weightFunction(asymptSchedule);
        System.out.println(asymptSchedule);
        int[][] greedyAlgorithm = GreedyAlgorithm.greedyAlgorithm(asymptSchedule, 10, 15, Bres);
        System.out.println(greedyAlgorithm);
    }

    @Test
    void testTwoGreedy() {
        List<List<Integer>> xyTime = new ArrayList<>();
        int[][] arr = {{1,2,2,1,1,1,1}, {1,3,2,1,1,1,1}, {2,6,2,1,1,1,1}, {3,4,2,1,1,1,1},
                {3,5,2,1,1,1,1}, {4,8,2,1,1,1,1}, {5,8,2,1,1,1,1}, {6,8,2,1,1,1,1},
                {6,7,2,1,1,1,1}, {5,9,2,1,1,1,1}, {8,10,2,1,1,1,1}, {7,10,2,1,1,1,1}, {9,10,2,1,1,1,1}};
        for (int i = 0; i < arr.length; i ++){
            List<Integer> elements = new ArrayList<>();
            for (int j = 0; j < arr[0].length; j ++){
                elements.add(arr[i][j]);
            }
            xyTime.add(elements);
        }

        int[] Bres = {2,2,2,2};
        int[][] asymptSchedule;
        asymptSchedule = AsymptoticSchedule.asymptSchedule(10 , xyTime, 30, Bres);
        double[] weightCoeff = GreedyAlgorithm.weightFunction(asymptSchedule);
        System.out.println(asymptSchedule);
        int[][] greedyAlgorithm = GreedyAlgorithm.greedyAlgorithm(asymptSchedule, 10, 30, Bres);
        System.out.println(greedyAlgorithm);
    }
}

