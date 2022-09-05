public class TimeCritical {
    public static int earlyMoment(int[][] xyTime, int n){
        int[] earlyMoments = new int[n];
        boolean flag = true;

        while(flag){
            flag = false;
            for (int i = 0; i < xyTime.length; i++){
                int a = earlyMoments[xyTime[i][1]-1];
                earlyMoments[xyTime[i][1]-1] = Math.max((earlyMoments[xyTime[i][0]-1]+xyTime[i][2]), earlyMoments[xyTime[i][1]-1]);
                if (a != earlyMoments[xyTime[i][1]-1]){
                    flag = true;
                }
            }
        }
        return earlyMoments[n-1];
    }
}

