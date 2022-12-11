package OldVersion;

public class TimeCritical {
    public static int earlyMoment(int[][] xyTime, int n){
        int[] earlyMoments = new int[n];
        boolean flag = true;

        while(flag){
            flag = false;
            System.out.println("len: " + xyTime.length);
            for (int[] ints : xyTime) {
                int a = earlyMoments[ints[1] - 1];
                earlyMoments[ints[1] - 1] = Math.max((earlyMoments[ints[0] - 1] + ints[2]),
                        earlyMoments[ints[1] - 1]);
                if (a != earlyMoments[ints[1] - 1]) {
                    flag = true;
                }
            }
        }
        return earlyMoments[n-1];
    }
}

