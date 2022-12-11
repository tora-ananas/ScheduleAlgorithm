package OldVersion;

import java.util.Arrays;

public class LateSchedule {

    public static int[] lateMoments(int L, int[][] xyTime, int n){
        int[] Tlate = new int[n];
        Arrays.fill(Tlate, L);
        int[] lateMoments = new int[xyTime.length];
        boolean flag = true;

        while (flag){
            flag = false;
            for (int[] ints : xyTime) {
                int a = Tlate[ints[0] - 1];
                Tlate[ints[0] - 1] = Math.min((Tlate[ints[1] - 1] - ints[2]), Tlate[ints[0] - 1]);
                if (a != Tlate[ints[0] - 1]) {
                    flag = true;
                }
            }
        }

        for (int i = 0; i < xyTime.length; i++){
            lateMoments[i] = Tlate[xyTime[i][1]-1] - xyTime[i][2];
        }
        return lateMoments;
    }

}
