import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileReaderClass30 {
    static int n = 0;
    static int Tmax = 0;
    static List<List<Integer>> xyTime;
    static int[] Bres = new int[4];
    static int[][] lateMomentsRes;
    static int[][] greedyAlgorithm;
    static int MPMtime = 0;
    static int countMpm = 0;
    static int countNotEq = 0;
    static int countEq = 0;
    static int count = 0;
    static long longCountTime = 0;


    public static List<List<Integer>> readLines(){
        List<List<Integer>> listGreedyMPMTime = new ArrayList<>();
        List<Integer> listTimeMPM = new ArrayList<>();
        List<Integer> listTimeGreedy = new ArrayList<>();

        File file = new File("src/main/resources/j30.sm");

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            boolean project = false;

            //читаем из файла до самого конца
            line = br.readLine();
            while(line != null){
                //нашли строчку с проджектом и зашли в след цикл.
                if (line.startsWith("projects")) {
                    //System.out.println(" NEW PROJECT!");
                    xyTime = new ArrayList<>();
                    boolean precedence = false;
                    boolean requests = false;
                    boolean resourses = false;
                    line = br.readLine();
                    while (line != null && !line.startsWith("file with basedata")) {
                        if (line.startsWith("jobs (incl. supersource/sink )")) {
                            String[] arrSt = line.split("  ");
                            //System.out.println(arrSt[1]);
                            n = Integer.parseInt(arrSt[1]);
                            //читаем дальше..
                            line = br.readLine();
                        }
                        if (line.startsWith("horizon")) {
                            String[] arrSt = line.split("\s+");
                            //System.out.println(arrSt[2]);
                            Tmax = Integer.parseInt(arrSt[2]);
                            line = br.readLine();
                        }
                        if (line.startsWith("pronr.  #jobs rel.date duedate tardcost  MPM-Time")){
                            line = br.readLine();
                            String[] arrSt = line.split("\s+");
                            MPMtime = Integer.parseInt(arrSt[6]);
                        }
                        if (line.startsWith("PRECEDENCE RELATIONS:") && !precedence) {
                            precedence = true;
                            String nextLine = br.readLine();
                            for (int j = 0; j < 32; j++) {
                                nextLine = br.readLine();
                                String[] arrSt = nextLine.split("\s+");
                                //System.out.println("len: " + arrSt.length);
                                int len = arrSt.length;
                                for (int n = 4; n < len; n++) {
                                    List<Integer> element = new ArrayList<>();
                                    //System.out.println("add elem: " + Integer.parseInt(arrSt[1]) + " "
                                    //        + Integer.parseInt(arrSt[n]));
                                    element.add(Integer.parseInt(arrSt[1]));
                                    element.add(Integer.parseInt(arrSt[n]));

                                    xyTime.add(element);
                                }
                            }
                        }
                        if (line.startsWith("REQUESTS/DURATIONS:") && !requests) {
                            requests = true;
                            String nextLine = br.readLine();
                            br.readLine();
                            for (int j = 0; j < 32; j++) {
                                nextLine = br.readLine();
                                String[] arrSt = nextLine.split("\s+");
                                //System.out.println(arrSt[1]);
                                for (List<Integer> elem : xyTime) {
                                    if (elem.get(0) == Integer.parseInt(arrSt[1])) {
                                        elem.add(Integer.parseInt(arrSt[3]));
                                        elem.add(Integer.parseInt(arrSt[4]));
                                        elem.add(Integer.parseInt(arrSt[5]));
                                        elem.add(Integer.parseInt(arrSt[6]));
                                        elem.add(Integer.parseInt(arrSt[7]));
                                        //System.out.println("added elements to list");
                                    }
                                    if (elem.get(0) > Integer.parseInt(arrSt[1])) {
                                        break;
                                    }
                                }
                            }
                            /*System.out.println("--->");
                            for (List<Integer> elem : xyTime) {
                                System.out.println(elem);
                            }*/
                        }

                        if (line.startsWith("RESOURCEAVAILABILITIES:") && !resourses) {
                            resourses = true;
                            br.readLine();
                            String nextLine = br.readLine();
                            String[] arrSt = nextLine.split("\s+");
                            Bres[0] = Integer.parseInt(arrSt[1]);
                            Bres[1] = Integer.parseInt(arrSt[2]);
                            Bres[2] = Integer.parseInt(arrSt[3]);
                            Bres[3] = Integer.parseInt(arrSt[4]);
                            //System.out.println("END");
                        }
                        line = br.readLine();
                        if (precedence && requests && resourses){

                            //do some algorithm
                            long start = System.currentTimeMillis();
                            //System.out.println("Print one");
                            lateMomentsRes = AsymptoticSchedule.asymptSchedule(n, xyTime, Tmax, Bres);

                            greedyAlgorithm = GreedyAlgorithm.greedyAlgorithm(lateMomentsRes, n, Tmax, Bres);
                            count++;

                            listTimeGreedy.add(greedyAlgorithm[1][0]);
                            listTimeMPM.add(MPMtime);

                            if (greedyAlgorithm[1][0] == MPMtime){
                                countEq++;
                            } else if (greedyAlgorithm[1][0] < MPMtime){
                                //System.out.println(" ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR!!!");
                                //System.out.println(greedyAlgorithm[1][0] + "<-- & --> " + TCritical);
                                //GreedyAlgorithm.greedyAlgorithm(lateMomentsRes, n, Tmax, Bres);
                                System.out.println("MPMtime" + MPMtime);
                                System.out.println("greedyAlgorithm[1][0]" + greedyAlgorithm[1][0]);
                                GreedyAlgorithm.greedyAlgorithm(lateMomentsRes, n, Tmax, Bres);
                                GreedyAlgorithm.drawSchedule(greedyAlgorithm, lateMomentsRes, Bres);
                                GreedyAlgorithm.checkPredecessors(greedyAlgorithm,lateMomentsRes);
                                countMpm++;
                                countNotEq++;
                            } else {
                                //System.out.println(" ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR!!!");
                                //System.out.println(greedyAlgorithm[1][0] + "<-- & --> " + TCritical);
                                countNotEq++;
                            }
                            long finish = System.currentTimeMillis();
                            long elapsed = finish - start;
                            //System.out.println(" Прошло времени, мс: " + elapsed);
                            longCountTime += elapsed;
                            //System.out.println("  DONE ASYMPTOTIC SCHEDULE");
                            break;
                        }
                    }
                } else {
                    line = br.readLine();
                }

            }
            listGreedyMPMTime.add(listTimeGreedy);
            listGreedyMPMTime.add(listTimeMPM);

            /*System.out.println(" END OF FILE");
            System.out.println(" ВСЕГО ПОСТРОЕНО РАСПИСАНИЙ: " + count);
            System.out.println(" ИЗ НИХ ПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countEq);
            System.out.println(" НЕПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countNotEq);
            System.out.println(" MPM COUNT: " + countMpm);*/
            //System.out.println(" Среднее время выполнения одного проекта в мс: " + longCountTime / count);
            System.out.println(" MPM COUNT: " + countMpm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listGreedyMPMTime;
    }

    public static void main(String[] args) {
        LocalTime startTime = LocalTime.now();
        List<Integer> listBestResults = new ArrayList<>();
        List<List<Integer>> listResult = new ArrayList<>();

        listResult = readLines();

        listBestResults.addAll(listResult.get(0));

        int iterations = 1;

        for (int j = 1; j < iterations; j++){
            listResult = readLines();
            for (int i = 0; i < listResult.get(0).size(); i++){
                if (listResult.get(0).get(i) < listBestResults.get(i)){
                    listBestResults.set(i, listResult.get(0).get(i));
                    //System.out.println("REPLACE ELEMENT " + i);
                }
            }
        }

        int count = 0;
        int relativeError = 0;
        int averageRelativeError = 0;
        int countErrors = 0;

        for (int j = 0; j < listBestResults.size(); j++){
            relativeError = (Math.abs(listBestResults.get(j) - listResult.get(1).get(j)) / listResult.get(1).get(j)) * 100;
            countErrors = countErrors + relativeError;
            count++;
            //System.out.println("Relative error = " + relativeError);
        }

        averageRelativeError = countErrors / count;

        System.out.println(" 30 JOBS ---> ");
        System.out.println(" ВСЕГО ПОСТРОЕНО РАСПИСАНИЙ: " + count);
        System.out.println(" ВСЕГО ИТЕРАЦИЙ: " + iterations);
        System.out.println("Средняя относительная ошибка: " + averageRelativeError);



        LocalTime finishTime = LocalTime.now();

        System.out.println(" Начало работы алгоритма: " + startTime);
        System.out.println(" Конец работы алгоритма: " + finishTime);


    }
}


