import java.util.ArrayList;
import java.util.List;

public class AsymptoticScheduleDouble {

    public static int[][] asymptSchedule(int n, List<List<Integer>> xyTime, int Tmax, int[] Bres){
        int Tcritical = TimeCritical.earlyMoment(xyTime, n);
        double epsilon = 1; //(double)Tmax / (double)(n*Tcritical);
        int L1 = Tcritical;
        int L2 = Tcritical + Tmax;
        int L = (L1 + L2) / 2;
        int[] lateMoments;
        int[][] result = new int[2][xyTime.size()];
        int Leps = 0;
        int tCtriticalStart = 0;
        int tCtriticalFinish = 0;

        while(true){
            lateMoments = LateSchedule.lateMoments(L, xyTime, n);



            double[][] rt = new double[xyTime.size()][4];
            for (int i = 0; i < xyTime.size() ; i++){
                if (xyTime.get(i).get(2) == 0){
                    rt[i][0] = 0;
                    rt[i][1] = 0;
                    rt[i][2] = 0;
                    rt[i][3] = 0;
                    continue;
                }
                rt[i][0] = (double) xyTime.get(i).get(3)/ xyTime.get(i).get(2);
                rt[i][1] = (double) xyTime.get(i).get(4)/ xyTime.get(i).get(2);
                rt[i][2] = (double) xyTime.get(i).get(5)/ xyTime.get(i).get(2);
                rt[i][3] = (double) xyTime.get(i).get(6)/ xyTime.get(i).get(2);
            }

            //count bRes and Bres here.
            double[][] tMoments = new double[xyTime.size()][2+4];
            for (int i = 0; i < xyTime.size(); i++){
                tMoments[i][0] = lateMoments[i];
                tMoments[i][1] = lateMoments[i] + xyTime.get(i).get(2);
                tMoments[i][2] = rt[i][0];
                tMoments[i][3] = rt[i][1];
                tMoments[i][4] = rt[i][2];
                tMoments[i][5] = rt[i][3];
            }

            boolean sorted = false;
            double[] temp = new double[6];

            while(!sorted) {
                sorted = true;
                for (int i = 0; i < tMoments.length - 1; i++) {
                    if (tMoments[i][0] > tMoments[i+1][0]) {
                        for (int k = 0; k < 6; k++){
                            temp[k] = tMoments[i][k];
                        }
                        for (int l = 0; l < 6; l++){
                            tMoments[i][l] = tMoments[i+1][l];
                        }
                        for (int m = 0; m < 6; m++){
                            tMoments[i+1][m] = temp[m];
                        }
                        sorted = false;
                    }
                }
                tCtriticalStart = (int) tMoments[0][0];
                tCtriticalFinish = (int) tMoments[tMoments.length-1][1];
            }

            double[][] bRes = new double[Tcritical][4];
            double[][] SRes = new double[Tcritical][4];
            double[][] QRes = new double[Tcritical][4];
            double[][] BfinRes = new double[Tcritical][4];


            for (int i = 0; i < Tcritical; i++){
                for (int j = 0; j < tMoments.length; j++){
                    if (tMoments[j][0] > i+tCtriticalStart){
                        break;
                    }
                    if (tMoments[j][0] <= i+tCtriticalStart && tMoments[j][1] > i+tCtriticalStart){
                        bRes[i][0] += tMoments[j][2];
                        bRes[i][1] += tMoments[j][3];
                        bRes[i][2] += tMoments[j][4];
                        bRes[i][3] += tMoments[j][5];
                    }
                }
            }

            for (int i = 0; i < Tcritical; i++){
                double sumb1 = 0;
                double sumb2 = 0;
                double sumb3 = 0;
                double sumb4 = 0;
                double sumB1 = 0;
                double sumB2 = 0;
                double sumB3 = 0;
                double sumB4 = 0;
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
            for (int i = 0; i < Tcritical; i++){
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
                //System.out.println(" LateScheduleTime: " + L);
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

    public static void main(String[] args) {
        List<List<Integer>> xyTime = new ArrayList<>();
        List<Integer> elem;
        int Tmax = 135;
        int[] Bres = {12,15,15,16};
        int MPM_time = 37;
        String parceStr =
                "1, 2, 0, 0, 0, 0, 0\n" +
                "1, 3, 0, 0, 0, 0, 0\n" +
                "1, 4, 0, 0, 0, 0, 0\n" +
                "2, 6, 5, 5, 0, 8, 3\n" +
                "2, 9, 5, 5, 0, 8, 3\n" +
                "3, 11, 4, 6, 7, 8, 0\n" +
                "4, 5, 7, 8, 0, 4, 5\n" +
                "4, 7, 7, 8, 0, 4, 5\n" +
                "4, 17, 7, 8, 0, 4, 5\n" +
                "5, 8, 3, 3, 9, 2, 8\n" +
                "5, 13, 3, 3, 9, 2, 8\n" +
                "5, 22, 3, 3, 9, 2, 8\n" +
                "6, 12, 10, 5, 3, 6, 9\n" +
                "7, 10, 4, 1, 8, 1, 8\n" +
                "7, 19, 4, 1, 8, 1, 8\n" +
                "7, 28, 4, 1, 8, 1, 8\n" +
                "8, 29, 2, 5, 5, 0, 3\n" +
                "9, 13, 7, 2, 3, 0, 0\n" +
                "9, 18, 7, 2, 3, 0, 0\n" +
                "9, 20, 7, 2, 3, 0, 0\n" +
                "10, 18, 7, 1, 0, 0, 8\n" +
                "11, 16, 3, 5, 9, 0, 6\n" +
                "11, 31, 3, 5, 9, 0, 6\n" +
                "12, 15, 2, 2, 10, 4, 0\n" +
                "13, 14, 4, 0, 4, 7, 4\n" +
                "13, 19, 4, 0, 4, 7, 4\n" +
                "14, 26, 2, 3, 8, 2, 0\n" +
                "15, 24, 2, 0, 1, 0, 2\n" +
                "16, 28, 7, 3, 9, 5, 6\n" +
                "17, 21, 5, 9, 0, 0, 10\n" +
                "18, 23, 3, 6, 10, 9, 0\n" +
                "19, 27, 6, 5, 6, 0, 8\n" +
                "20, 22, 4, 5, 3, 9, 0\n" +
                "20, 25, 4, 5, 3, 9, 0\n" +
                "21, 26, 6, 8, 8, 10, 8\n" +
                "21, 27, 6, 8, 8, 10, 8\n" +
                "22, 26, 5, 1, 4, 0, 2\n" +
                "23, 27, 7, 0, 0, 10, 2\n" +
                "23, 30, 7, 0, 0, 10, 2\n" +
                "24, 25, 3, 5, 8, 0, 6\n" +
                "24, 30, 3, 5, 8, 0, 6\n" +
                "25, 31, 2, 0, 10, 8, 4\n" +
                "26, 28, 6, 4, 9, 10, 2\n" +
                "27, 29, 1, 1, 7, 3, 2\n" +
                "28, 29, 4, 0, 4, 6, 5\n" +
                "29, 32, 4, 5, 0, 8, 0\n" +
                "30, 32, 9, 0, 6, 4, 3\n" +
                "31, 32, 1, 0, 5, 1, 6";
        String[] strings = parceStr.split("\n");
        for (String str : strings) {
            String[] st = str.split(",");
            elem = new ArrayList<>();
            for (int i = 0; i < st.length; i++){
                elem.add(Integer.parseInt(st[i].replaceAll("\\s+","")));
            }
            xyTime.add(elem);
        }

        int[][] res = asymptSchedule(32, xyTime, Tmax, Bres);
        System.out.println(res[1][0]);
    }
}
