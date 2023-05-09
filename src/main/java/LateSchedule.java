import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LateSchedule {

    public static int[] lateMoments(int LateMoment, List<List<Integer>> xyTime, int n){
        int[] Tlate = new int[n];
        Arrays.fill(Tlate, LateMoment);
        int[] lateMoments = new int[xyTime.size()];
        boolean flag = true;

        while (flag){
            flag = false;
            for (List<Integer> integers : xyTime) {
                int a = Tlate[integers.get(0) - 1];
                int first = Tlate[integers.get(1) - 1] - integers.get(2);
                int second = Tlate[integers.get(0) - 1];
                int elementMin = Math.min(first, second);
                Tlate[integers.get(0) - 1] = elementMin;

                if (a != Tlate[integers.get(0) - 1]) {
                    flag = true;
                }
            }
        }

        for (int i = 0; i < xyTime.size(); i++){
            lateMoments[i] = Tlate[xyTime.get(i).get(1)-1] - xyTime.get(i).get(2);
        }
        return lateMoments;
    }

    public static void main(String[] args) {
        List<List<Integer>> xyTime = new ArrayList<>();
        List<Integer> elem;
        int[] elements = {1,2,2, 1,3,1, 1,4,5, 2,4,2,
                2,6,6, 2,7,5, 3,4,2, 3,5,7, 4,5,6, 4,6,2,
                5,8,4, 6,8,7, 6,7,2, 7,8,4};
        for (int i = 0; i < elements.length; i += 3){
            elem = new ArrayList<>();
            elem.add(elements[i]);
            elem.add(elements[i+1]);
            elem.add(elements[i+2]);
            xyTime.add(elem);
        }
        for (int j = 0; j < xyTime.size(); j ++){
            System.out.println(xyTime.get(j));
        }
        int[] late = lateMoments(15, xyTime, 8);
        for (int l: late){
            System.out.println(l);
        }
    }

}
