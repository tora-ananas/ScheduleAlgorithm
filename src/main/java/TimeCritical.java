import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeCritical {
    public static int earlyMoment(List<List<Integer>> xyTime, int n){
        int[] earlyMoments = new int[n];
        boolean flag = true;


        while(flag){
            flag = false;

            for (int i = 0; i < xyTime.size(); i++) {
                List<Integer> integers = xyTime.get(i);

                int a = earlyMoments[integers.get(1) - 1];
                int first = earlyMoments[integers.get(0) - 1] + integers.get(2);
                int second = earlyMoments[integers.get(1) - 1];
                int elementMax = Math.max(first, second);
                earlyMoments[integers.get(1) - 1] = elementMax;

                if (a != earlyMoments[integers.get(1) - 1]) {
                    flag = true;
                }
            }
        }
        //System.out.println(" time critical: " + earlyMoments[n-1]);
        return earlyMoments[n-1];
    }

    public static void main(String[] args) {
        List<List<Integer>> xyTime = new ArrayList<>();
        List<Integer> elem;
        int[] elements = {1,2,2, 1,3,1, 1,4,5, 2,4,2,
                2,6,6, 2,7,5, 3,4,2, 3,5,7, 4,5,6, 4,6,2,
                5,8,4, 6,8,7, 6,7,2, 7,8,4};
        for (int i = 0; i < 42; i += 3){
            elem = new ArrayList<>();
            elem.add(elements[i]);
            elem.add(elements[i+1]);
            elem.add(elements[i+2]);
            xyTime.add(elem);
        }
        earlyMoment(xyTime, 8);
    }
}

