import java.util.*;

class ComparatorByWeightFunc implements Comparator<List<Double>> {

    @Override
    public int compare(List<Double> o1, List<Double> o2) {
        return Double.compare(o1.get(1), o2.get(1));
    }
}

public class GreedyAlgorithm {

    //в массиве asymptoticSchedule распишем за что отвечают порядковые элементы:
    //0 - начальная вершина работы
    //1 - концевая вершина работы
    //2 - длительность работы
    //3 - начальное время выполнения работы по позднему расписанию
    //4 - время окончания работы по позднему расписанию
    //5 - первый тип ресурса
    //6 - второй тип ресурса
    //7 - третий тип ресурса
    //8 - четвертый тип ресурса

    public static int[][] greedyAlgorithm(int[][] asymptoticSchedule, int n, int Tmax, int[] Bres){
        int len = asymptoticSchedule.length;
        int[][] returnGreedy = new int[2][len];
        int[] start_job = new int[len];
        int[] time_ei = new int[n+1];

        //посчитаем горизонт планирования просуммировав длительности работ
        int TmaxJobDuration = 0;
        for (int i = 0; i < asymptoticSchedule.length; i++){
            TmaxJobDuration = TmaxJobDuration + asymptoticSchedule[i][2];
        }

        //выбираем горизонт планирования: Tmax - из тестовых данных/ TMAX - наш посчитанный
        int TMAX = TmaxJobDuration;
        //int TMAX = Tmax;

        int[] resourceAvailable_type1_time = new int[TMAX];
        int[] resourceAvailable_type2_time = new int[TMAX];
        int[] resourceAvailable_type3_time = new int[TMAX];
        int[] resourceAvailable_type4_time = new int[TMAX];

        double[] weightCoefficients = weightFunction(asymptoticSchedule);

        //в листе хранятся номера работ, которые требуется наложить на календарь.
        //после наложения работы на календарь мы ее из листа удалим.
        List<List<Double>> D_jobsNotOnCalendar = new ArrayList<>();
        List<Integer> S_jobsOnCalendar = new ArrayList<>();

        for (int i = 0; i < len; i++){
            List<Double> listOfJobWeightFunc = new ArrayList<>();
            double weightElement = weightCoefficients[i];
            listOfJobWeightFunc.add((double) i);
            listOfJobWeightFunc.add(weightElement);
            D_jobsNotOnCalendar.add(listOfJobWeightFunc);
        }

        //отсортируем лист по возрастанию весовых функций
        Collections.sort(D_jobsNotOnCalendar, new ComparatorByWeightFunc());
        //System.out.println(D_jobsNotOnCalendar);

        for (int i = 0; i < TMAX; i++){
            resourceAvailable_type1_time[i] = Bres[0];
            resourceAvailable_type2_time[i] = Bres[1];
            resourceAvailable_type3_time[i] = Bres[2];
            resourceAvailable_type4_time[i] = Bres[3];
        }

        //предварительно занесем в Sg одну работу с номером 1 начального события
        List<Integer> listProbabilityJobs = new ArrayList<>();
        for (int i = 0; i < D_jobsNotOnCalendar.size(); i++){
            double numOfJob = D_jobsNotOnCalendar.get(i).get(0);
            int indexJob = (int)numOfJob;
            if (asymptoticSchedule[indexJob][0] == 1){
                listProbabilityJobs.add(indexJob);
            }
        }

        int indexJobToTheCalendar = -1;
        for (int elem: listProbabilityJobs){
            double probability = Math.random();
            if (probability >= 0.5){
                indexJobToTheCalendar = elem;
                //удаляет по нужному нам элементу
                for (int i = 0; i < listProbabilityJobs.size(); i++){
                    if (listProbabilityJobs.get(i) == indexJobToTheCalendar){
                        listProbabilityJobs.remove(i);
                    }
                }
                break;
            } else {
                continue;
            }
        }
        if (indexJobToTheCalendar == -1){
            Random rand = new Random();
            indexJobToTheCalendar = listProbabilityJobs.get(rand.nextInt(listProbabilityJobs.size()));
            //удаляет по нужному нам элементу
            for (int i = 0; i < listProbabilityJobs.size(); i++){
                if (listProbabilityJobs.get(i) == indexJobToTheCalendar){
                    listProbabilityJobs.remove(i);
                }
            }
        }

        int startMomentTimeJob = asymptoticSchedule[indexJobToTheCalendar][0];
        int endMomentTimeJob = asymptoticSchedule[indexJobToTheCalendar][1];
        int durationTimeJob = asymptoticSchedule[indexJobToTheCalendar][2];
        int res1 = asymptoticSchedule[indexJobToTheCalendar][5];
        int res2 = asymptoticSchedule[indexJobToTheCalendar][6];
        int res3 = asymptoticSchedule[indexJobToTheCalendar][7];
        int res4 = asymptoticSchedule[indexJobToTheCalendar][8];

        int timeStartJob = time_ei[startMomentTimeJob];
        boolean flag = true;

        while (flag) {
            int duration = timeStartJob + durationTimeJob;
            for (int t = timeStartJob; t < duration; t++){
                if (res1 <= resourceAvailable_type1_time[t] && res2 <= resourceAvailable_type2_time[t]
                        && res3 <= resourceAvailable_type3_time[t] && res4 <= resourceAvailable_type4_time[t]) {
                    flag = false;
                } else {
                    timeStartJob = timeStartJob + 1;
                    flag = true;
                    break;
                }
            }
            if(duration == 0){
                flag = false;
            }
        }

        start_job[0] = timeStartJob;
        S_jobsOnCalendar.add(indexJobToTheCalendar);
        time_ei[endMomentTimeJob] = Math.max(time_ei[endMomentTimeJob], timeStartJob + durationTimeJob);

        //пересчитать доступные ресурсы
        int duration = timeStartJob + durationTimeJob;
        for (int t = timeStartJob; t < duration; t++){
            resourceAvailable_type1_time[t] =  resourceAvailable_type1_time[t] - res1;
            resourceAvailable_type2_time[t] =  resourceAvailable_type2_time[t] - res2;
            resourceAvailable_type3_time[t] =  resourceAvailable_type3_time[t] - res3;
            resourceAvailable_type4_time[t] =  resourceAvailable_type4_time[t] - res4;
        }

        for (int i = 0; i < D_jobsNotOnCalendar.size(); i++){
            double index = D_jobsNotOnCalendar.get(i).get(0);
            int idx = (int)index;
            if (idx == indexJobToTheCalendar){
                D_jobsNotOnCalendar.remove(i);
                break;
            }
        }

        System.out.printf("%2s|", indexJobToTheCalendar);
        System.out.printf(" %2s ", startMomentTimeJob);
        System.out.printf(" %2s ", endMomentTimeJob);
        System.out.printf(" %2s |", durationTimeJob);
        System.out.printf(" %2s ", timeStartJob);
        System.out.printf(" %2s ", time_ei[endMomentTimeJob]);
        System.out.println();


        //start main loop
        for (int step = 1; step < len; step++){


            for (int i = 0; i < D_jobsNotOnCalendar.size(); i++){
                double numOfJob = D_jobsNotOnCalendar.get(i).get(0);
                int indexJob = (int)numOfJob;
                for (int j = 0; j < S_jobsOnCalendar.size(); j++) {
                    int numberOfJobOnCalendar = S_jobsOnCalendar.get(j);
                    if (asymptoticSchedule[indexJob][0] == asymptoticSchedule[numberOfJobOnCalendar][1] ||
                            asymptoticSchedule[indexJob][0] == asymptoticSchedule[numberOfJobOnCalendar][0]){
                        if (!listProbabilityJobs.contains(indexJob)){
                            listProbabilityJobs.add(indexJob);
                        }
                    }
                }
            }

            indexJobToTheCalendar = -1;
            for (int elem: listProbabilityJobs){
                double probability = Math.random();
                if (probability >= 0.5){
                    indexJobToTheCalendar = elem;
                    //удаляет по нужному нам элементу
                    for (int i = 0; i < listProbabilityJobs.size(); i++){
                        if (listProbabilityJobs.get(i) == indexJobToTheCalendar){
                            listProbabilityJobs.remove(i);
                        }
                    }
                    break;
                } else {
                    continue;
                }
            }

            if (indexJobToTheCalendar == -1){
                Random rand = new Random();
                indexJobToTheCalendar = listProbabilityJobs.get(rand.nextInt(listProbabilityJobs.size()));
                //удаляет по нужному нам элементу
                for (int i = 0; i < listProbabilityJobs.size(); i++){
                    if (listProbabilityJobs.get(i) == indexJobToTheCalendar){
                        listProbabilityJobs.remove(i);
                    }
                }
            }

            startMomentTimeJob = asymptoticSchedule[indexJobToTheCalendar][0];
            endMomentTimeJob = asymptoticSchedule[indexJobToTheCalendar][1];
            durationTimeJob = asymptoticSchedule[indexJobToTheCalendar][2];
            res1 = asymptoticSchedule[indexJobToTheCalendar][5];
            res2 = asymptoticSchedule[indexJobToTheCalendar][6];
            res3 = asymptoticSchedule[indexJobToTheCalendar][7];
            res4 = asymptoticSchedule[indexJobToTheCalendar][8];

            timeStartJob = time_ei[startMomentTimeJob];
            flag = true;

            while (flag) {
                duration = timeStartJob + durationTimeJob;
                if(duration >= TMAX){
                    System.out.println("   !!!THERE IS NO AVAILABLE SCHEDULE!!!");
                    return returnGreedy;
                }
                for (int t = timeStartJob; t < duration; t++){
                    if (res1 <= resourceAvailable_type1_time[t] && res2 <= resourceAvailable_type2_time[t]
                            && res3 <= resourceAvailable_type3_time[t] && res4 <= resourceAvailable_type4_time[t]) {
                        flag = false;
                    } else {
                        timeStartJob = timeStartJob + 1;

                        flag = true;
                        break;
                    }
                }
                if(duration == 0){
                    flag = false;
                }
            }

            start_job[indexJobToTheCalendar] = timeStartJob;
            S_jobsOnCalendar.add(indexJobToTheCalendar);
            //endMomenntTimeJob-1 потому что время мы начинаем с 0 момента. поэтому мы на единицу меньше берем.
            time_ei[endMomentTimeJob] = Math.max(time_ei[endMomentTimeJob], timeStartJob + durationTimeJob);

            System.out.printf("%2s|", indexJobToTheCalendar);
            System.out.printf(" %2s ", startMomentTimeJob);
            System.out.printf(" %2s ", endMomentTimeJob);
            System.out.printf(" %2s |", durationTimeJob);
            System.out.printf(" %2s ", timeStartJob);
            System.out.printf(" %2s ", time_ei[endMomentTimeJob]);
            System.out.println();

            //пересчитать доступные ресурсы
            duration = timeStartJob + durationTimeJob;
            for (int t = timeStartJob; t < duration; t++){
                resourceAvailable_type1_time[t] =  resourceAvailable_type1_time[t] - res1;
                resourceAvailable_type2_time[t] =  resourceAvailable_type2_time[t] - res2;
                resourceAvailable_type3_time[t] =  resourceAvailable_type3_time[t] - res3;
                resourceAvailable_type4_time[t] =  resourceAvailable_type4_time[t] - res4;
            }

            for (int i = 0; i < D_jobsNotOnCalendar.size(); i++){
                double index = D_jobsNotOnCalendar.get(i).get(0);
                int idx = (int)index;
                if (idx == indexJobToTheCalendar){
                    D_jobsNotOnCalendar.remove(i);
                    break;
                }
            }

        }

        for (int i = 0; i < len; i++){
            returnGreedy[0][i] = start_job[i];
        }

        int max = 0;
        for (int i = 0; i < n; i++){
            if (time_ei[i] > max){
                max = time_ei[i];
            }
        }
        returnGreedy[1][0] = max;
        //checkIfCorrectSchedule(returnGreedy, asymptoticSchedule, Bres, n);
        //drawSchedule(returnGreedy, asymptoticSchedule);
        System.out.println();
        return returnGreedy;
    }

    public static void drawSchedule(int[][] greedyAlgorithm, int[][] asymptoticSchedule, int[] Bres){
        int lenghtJobs = greedyAlgorithm[0].length;
        System.out.println();
        for (int i = 0; i < lenghtJobs; i++){
            int start = greedyAlgorithm[0][i];
            int duration = asymptoticSchedule[i][2];
            int end = start + duration;

            int startIndex = asymptoticSchedule[i][0];
            int finalIndex = asymptoticSchedule[i][1];
            int res1 = asymptoticSchedule[i][5];
            int res2 = asymptoticSchedule[i][6];
            int res3 = asymptoticSchedule[i][7];
            int res4 = asymptoticSchedule[i][8];

            System.out.printf("%4s|%4s%4s%4s|%4s%4s%4s%4s|", i, startIndex, finalIndex, duration,
                    res1, res2, res3, res4);

            for (int j = 0; j < start; j++){
                System.out.printf("%3s", "_");
            }
            for (int k = start; k < end; k++){
                System.out.printf("%3s", k);
            }
            System.out.printf("%3s", end);
            System.out.println();
        }

        int[] SumResourcesType1 = new int[greedyAlgorithm[1][0]];
        int[] SumResourcesType2 = new int[greedyAlgorithm[1][0]];
        int[] SumResourcesType3 = new int[greedyAlgorithm[1][0]];
        int[] SumResourcesType4 = new int[greedyAlgorithm[1][0]];
        for (int i = 0; i < lenghtJobs; i++){
            int startJobNumber = greedyAlgorithm[0][i];
            int durationJobNumber = asymptoticSchedule[i][2];
            for (int j = startJobNumber; j < startJobNumber + durationJobNumber; j ++){
                SumResourcesType1[j] += asymptoticSchedule[i][5];
                SumResourcesType2[j] += asymptoticSchedule[i][6];
                SumResourcesType3[j] += asymptoticSchedule[i][7];
                SumResourcesType4[j] += asymptoticSchedule[i][8];
            }
        }

        int length = greedyAlgorithm[1][0];

        System.out.printf("%34s|", "Available resources");
        System.out.print(Bres[0]);
        System.out.println();

        System.out.printf("%34s|", "resource 1");
        for (int i = 0; i < length; i++){
            System.out.printf("%3s", SumResourcesType1[i]);
        }
        System.out.println();

        System.out.printf("%34s|", "Available resources");
        System.out.print(Bres[1]);
        System.out.println();

        System.out.printf("%34s|", "resource 2");
        for (int i = 0; i < length; i++){
            System.out.printf("%3s", SumResourcesType2[i]);
        }
        System.out.println();

        System.out.printf("%34s|", "Available resources");
        System.out.print(Bres[2]);
        System.out.println();

        System.out.printf("%34s|", "resource 3");
        for (int i = 0; i < length; i++){
            System.out.printf("%3s", SumResourcesType3[i]);
        }
        System.out.println();

        System.out.printf("%34s|", "Available resources");
        System.out.print(Bres[3]);
        System.out.println();

        System.out.printf("%34s|", "resource 4");
        for (int i = 0; i < length; i++){
            System.out.printf("%3s", SumResourcesType4[i]);
        }
        System.out.println();

    }

    public static boolean checkPredecessors(int[][] greedyAlgorithm, int[][] asymptoticSchedule) {
        int lengthGreedyJobs = greedyAlgorithm[0].length;
        for (int i = 0; i < lengthGreedyJobs; i++){
            int jobStartMomentFirst = asymptoticSchedule[i][0];
            int jobTimeStartFirst = greedyAlgorithm[0][i];
            for (int j = 0; j < lengthGreedyJobs; j++){
                int jobEndMomentSecond = asymptoticSchedule[j][1];
                int jobTimeFinishSecond = greedyAlgorithm[0][j] + asymptoticSchedule[j][2];
                if (jobStartMomentFirst == jobEndMomentSecond){
                    if (jobTimeStartFirst < jobTimeFinishSecond){
                        System.out.println(" ERROR WITH PREDECESSORS!");
                        System.out.println(" PREDECESSORS: first job - " + i + " second job - " + j);
                        System.out.println(" TIME START FIRST JOB: " + jobTimeStartFirst);
                        System.out.println(" TIME FINISH SECOND JOB: " + jobTimeFinishSecond);
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    public static boolean checkIfCorrectSchedule(int[][] greedyAlgorithm, int[][] asymptoticSchedule, int[] Bres, int n){
        //количество ребер работ
        int lenghtJobs = greedyAlgorithm[0].length;
        int[] SumResourcesType1 = new int[greedyAlgorithm[1][0]];
        int[] SumResourcesType2 = new int[greedyAlgorithm[1][0]];
        int[] SumResourcesType3 = new int[greedyAlgorithm[1][0]];
        int[] SumResourcesType4 = new int[greedyAlgorithm[1][0]];
        for (int i = 0; i < lenghtJobs; i++){
            int startJobNumber = greedyAlgorithm[0][i];
            int durationJobNumber = asymptoticSchedule[i][2];
            for (int j = startJobNumber; j < startJobNumber + durationJobNumber; j ++){
                SumResourcesType1[j] += asymptoticSchedule[i][5];
                SumResourcesType2[j] += asymptoticSchedule[i][6];
                SumResourcesType3[j] += asymptoticSchedule[i][7];
                SumResourcesType4[j] += asymptoticSchedule[i][8];
            }
        }


        int[] order = new int[n+1];
        order[1] = 1;

        for (int i = 0; i < lenghtJobs; i++){
            for (int j = 0; j < lenghtJobs; j++){
                int startJobNumber = greedyAlgorithm[0][j];
                if (startJobNumber == i){
                    int finishIndex = asymptoticSchedule[j][1];
                    int startIndex = asymptoticSchedule[j][0];
                    if (order[startIndex] == 1){
                        order[finishIndex] = 1;
                    } else {
                        System.out.println(" ERROR WITH ORDER ON SCHEDULE!");
                    }
                }
            }
        }

        for (int i = 0; i < SumResourcesType1.length; i++){
            if (SumResourcesType1[i] > Bres[0] || SumResourcesType2[i] > Bres[1] || SumResourcesType3[i] > Bres[2] ||
                    SumResourcesType4[i] > Bres[3]){
                System.out.println("   ERROR WITH RESOURCES!");
            }
            //System.out.println(i + " Resources available: " + Bres[0] + " res: " + SumResourcesType1[i]);
            //System.out.println(i + " Resources available: " + Bres[1] + " res: " + SumResourcesType2[i]);
            //System.out.println(i + " Resources available: " + Bres[2] + " res: " + SumResourcesType3[i]);
            //System.out.println(i + " Resources available: " + Bres[3] + " res: " + SumResourcesType4[i]);
        }
        //System.out.println();
        return true;
    }

    public static double[] weightFunction(int[][] asymptoticSchedule){
        int len = asymptoticSchedule.length;
        double[] weightCoefficient = new double[len];
        int c1 = 1;
        double c2 = 0.1;
        int w1 = 1;
        int w2 = 1;
        int w3 = 1;
        int w4 = 2;
        for (int i = 0; i < len; i++){
            int startTimeJob = asymptoticSchedule[i][3];
            int durationTimeJob = asymptoticSchedule[i][2];
            int r1 = asymptoticSchedule[i][5];
            int r2 = asymptoticSchedule[i][6];
            int r3 = asymptoticSchedule[i][7];
            int r4 = asymptoticSchedule[i][8];
            int max1 = Math.max( (w1 * r1 * durationTimeJob),
                    (w2 * r2 * durationTimeJob) );
            int max2 = Math.max( (w3 * r3 * durationTimeJob),
                    (w4 * r4 * durationTimeJob) );
            weightCoefficient[i] = c1 * startTimeJob - c2 * Math.max(max1,max2);
        }
        return weightCoefficient;
    }

    public static void main(String[] args) {
        /*List<List<Integer>> xyTime = new ArrayList<>();
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
        greedyAlgorithm(asymptSchedule, 8, 20, Bres);*/

    }
}
