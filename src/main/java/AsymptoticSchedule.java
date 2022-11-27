public class AsymptoticSchedule {

    public static int[] asymptSchedule(int n, int[][] xyTime, int Tmax, int[][] Rt, int[] Bres){
        int Tcritical = TimeCritical.earlyMoment(xyTime, n);
        double epsilon = 1;//(double)Tmax / (double)(n*Tcritical);
        int L1 = Tcritical;
        int L2 = Tcritical + Tmax;
        int L = (L1 + L2) / 2;
        int[] lateMoments;
        int Leps = 0;

        while(true){
            lateMoments = LateSchedule.lateMoments(L, xyTime, n);

            int[] bRes = new int[Tcritical];
            int[] SRes = new int[Tcritical];
            int[] QRes = new int[Tcritical];
            int[] BfinRes = new int[Tcritical];


            int[] rt = new int[xyTime.length];
            for (int i = 0; i < Rt.length ; i++){
                rt[i] = Rt[i][0] / xyTime[i][2];
            }


            //count bRes and Bres here.
            int[][] tMoments = new int[xyTime.length][2+1];
            for (int i = 0; i < xyTime.length; i++){
                tMoments[i][0] = lateMoments[i];
                tMoments[i][1] = lateMoments[i] + xyTime[i][2];
                tMoments[i][2] = rt[i];
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
                        bRes[i] += tMoments[j][2];
                    }
                }
            }

            for (int i = 0; i < Tcritical; i++){
                int sumb = 0;
                int sumB = 0;
                for (int j = 0; j <= i; j++){
                    sumb += bRes[j];
                    sumB += Bres[j];
                }
                SRes[i] = sumb;
                QRes[i] = sumB;
            }

            BfinRes[0] = Bres[0];
            for (int i = 1; i < Tcritical; i++){
                BfinRes[i] = Bres[i] + (QRes[i-1] - SRes[i-1]);
            }
            System.out.println(BfinRes + "" + bRes);

            //check if Late Schedule is right for the resources.
            for (int i = 0; i < bRes.length; i++){
                if (bRes[i] > BfinRes[i]){
                    L1 = L;
                    break;
                }
            }

            if (L1 != L){
                L2 = L;
            }

            if (L2 - L1 == Leps){
                return lateMoments;
            }
            else {
                Leps = L2 - L1;
                L = (L1+L2)/2;
            }
        }
    }
}
