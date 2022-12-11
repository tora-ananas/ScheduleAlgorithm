import java.util.List;

public class AsymptoticSchedule {

    public static int[][] asymptSchedule(int n, List<List<Integer>> xyTime, int Tmax, int[] Bres){
        int Tcritical = TimeCritical.earlyMoment(xyTime, n);
        double epsilon = 1; //(double)Tmax / (double)(n*Tcritical);
        int L1 = Tcritical;
        int L2 = Tcritical + Tmax;
        int L = (L1 + L2) / 2;
        int[] lateMoments;
        int[][] result = new int[2][xyTime.size()];
        int Leps = 0;

        while(true){
            lateMoments = LateSchedule.lateMoments(L, xyTime, n);

            int[][] bRes = new int[Tcritical][4];
            int[][] SRes = new int[Tcritical][4];
            int[][] QRes = new int[Tcritical][4];
            int[][] BfinRes = new int[Tcritical][4];


            int[][] rt = new int[xyTime.size()][4];
            for (int i = 0; i < xyTime.size() ; i++){
                if (xyTime.get(i).get(2) == 0){
                    rt[i][0] = 0;
                    rt[i][1] = 0;
                    rt[i][2] = 0;
                    rt[i][3] = 0;
                    continue;
                }
                // как сделать с несколькими ресурсами?
                rt[i][0] = xyTime.get(i).get(3) / xyTime.get(i).get(2);
                rt[i][1] = xyTime.get(i).get(4) / xyTime.get(i).get(2);
                rt[i][2] = xyTime.get(i).get(5) / xyTime.get(i).get(2);
                rt[i][3] = xyTime.get(i).get(6) / xyTime.get(i).get(2);
            }

            //count bRes and Bres here.
            int[][] tMoments = new int[xyTime.size()][2+4];
            for (int i = 0; i < xyTime.size(); i++){
                tMoments[i][0] = lateMoments[i];
                tMoments[i][1] = lateMoments[i] + xyTime.get(i).get(2);
                tMoments[i][2] = rt[i][0];
                tMoments[i][3] = rt[i][1];
                tMoments[i][4] = rt[i][2];
                tMoments[i][5] = rt[i][3];
            }

            boolean sorted = false;
            int temp;

            while(!sorted) {
                sorted = true;
                for (int i = 0; i < tMoments.length - 1; i++) {
                    if (tMoments[i][0] > tMoments[i+1][0]) {
                        temp = tMoments[i][0];
                        tMoments[i][0] = tMoments[i+1][0];
                        tMoments[i+1][0] = temp;
                        sorted = false;
                    }
                }
            }

            for (int i = 0; i < Tcritical; i++){
                for (int j = 0; j < tMoments.length; j++){
                    if (tMoments[j][0] > i){
                        break;
                    }
                    if (tMoments[j][0] <= i && tMoments[j][1] >= i){
                        bRes[i][0] += tMoments[j][2];
                        bRes[i][1] += tMoments[j][3];
                        bRes[i][2] += tMoments[j][4];
                        bRes[i][3] += tMoments[j][5];
                    }
                }
            }

            for (int i = 0; i < Tcritical; i++){
                int sumb1 = 0;
                int sumb2 = 0;
                int sumb3 = 0;
                int sumb4 = 0;
                int sumB1 = 0;
                int sumB2 = 0;
                int sumB3 = 0;
                int sumB4 = 0;
                for (int j = 0; j <= i; j++){
                    sumb1 += bRes[j][0];
                    sumb2 += bRes[j][1];
                    sumb3 += bRes[j][2];
                    sumb4 += bRes[j][3];
                    sumB1 += Bres[0];
                    sumB2 += Bres[1];
                    sumB3 += Bres[2];
                    sumB4 += Bres[3];
                }
                SRes[i][0] = sumb1;
                SRes[i][1] = sumb2;
                SRes[i][2] = sumb3;
                SRes[i][3] = sumb4;
                QRes[i][0] = sumB1;
                QRes[i][1] = sumB2;
                QRes[i][2] = sumB3;
                QRes[i][3] = sumB4;
            }

            BfinRes[0][0] = Bres[0];
            BfinRes[0][1] = Bres[1];
            BfinRes[0][2] = Bres[2];
            BfinRes[0][3] = Bres[3];
            for (int i = 1; i < Tcritical; i++){
                BfinRes[i][0] = Bres[0] + (QRes[i-1][0] - SRes[i-1][0]);
                BfinRes[i][1] = Bres[1] + (QRes[i-1][1] - SRes[i-1][1]);
                BfinRes[i][2] = Bres[2] + (QRes[i-1][2] - SRes[i-1][2]);
                BfinRes[i][3] = Bres[3] + (QRes[i-1][3] - SRes[i-1][3]);
                //System.out.println(BfinRes[i][0] + " <--- BFINRES1 & bRES1 ---> " + bRes[i][0]);
                //System.out.println(BfinRes[i][1] + " <--- BFINRES2 & bRES2 ---> " + bRes[i][1]);
                //System.out.println(BfinRes[i][2] + " <--- BFINRES3 & bRES3 ---> " + bRes[i][2]);
                //System.out.println(BfinRes[i][3] + " <--- BFINRES4 & bRES4 ---> " + bRes[i][3]);
            }


            //check if Late Schedule is right for the resources.
            for (int i = 0; i < bRes.length; i++){
                if (bRes[i][0] > BfinRes[i][0] && bRes[i][1] > BfinRes[i][1] &&
                        bRes[i][2] > BfinRes[i][2] && bRes[i][3] > BfinRes[i][3]){
                    L1 = L;
                    break;
                }
            }

            if (L1 != L){
                L2 = L;
            }

            if (L2 - L1 == Leps){
                System.out.println(" LateScheduleTime: " + L);
                for (int i = 0; i < result[0].length; i++){
                    result[0][i] = lateMoments[i];
                }
                result[1][0] = L;
                return result;
            }
            else {
                Leps = L2 - L1;
                L = (L1+L2)/2;
            }
        }
    }
}
