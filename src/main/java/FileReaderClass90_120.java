import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileReaderClass90_120 {
    static int n = 0;
    static int Tmax = 0;
    static List<List<Integer>> xyTime;
    static int[] Bres = new int[4];
    static int[][] lateMomentsRes;
    static int MPMtime = 0;
    static int countNotEq = 0;
    static int countEq = 0;
    static int count = 0;
    static long longCountTime = 0;


    public static void readLines(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean project = false;

            //читаем из файла до самого конца
            line = br.readLine();
            boolean precedence = false;
            boolean requests = false;
            boolean resourses = false;
            //System.out.println(" NEW PROJECT!");
            xyTime = new ArrayList<>();
            while (line != null) {

                line = br.readLine();
                if (line.startsWith("jobs (incl. supersource/sink )")) {
                    String[] arrSt = line.split("  ");
                    //System.out.println("JOBS : " + arrSt[1]);
                    n = Integer.parseInt(arrSt[1]);
                    //читаем дальше..
                    //line = br.readLine();
                }
                if (line.startsWith("horizon")) {
                    String[] arrSt = line.split("\s+");
                    //System.out.println(" HORIZON : " + arrSt[2]);
                    Tmax = Integer.parseInt(arrSt[2]);
                    //line = br.readLine();
                }
                if (line.startsWith("pronr.  #jobs rel.date duedate tardcost  MPM-Time")) {
                    line = br.readLine();
                    String[] arrSt = line.split("\s+");
                    MPMtime = Integer.parseInt(arrSt[6]);
                }
                if (line.startsWith("PRECEDENCE RELATIONS:") && !precedence) {
                    //System.out.println("PRECEDENCE RELATIONS:");
                    precedence = true;
                    String nextLine;
                    br.readLine();
                    for (int j = 0; j < n; j++) {
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
                    /*System.out.println("--->");
                    for (List<Integer> elem : xyTime) {
                        System.out.println(elem);
                    }*/
                }

                if (line.startsWith("REQUESTS/DURATIONS:") && !requests) {
                    //System.out.println("REQUESTS/DURATIONS:");
                    requests = true;
                    String nextLine;
                    br.readLine();
                    br.readLine();
                    for (int j = 0; j < n; j++) {
                        nextLine = br.readLine();
                        String[] arrSt = nextLine.split("\s+");
                        //System.out.println(arrSt[1]);
                        if (j >= 99){
                            for (int i = 0; i < xyTime.size(); i++) {
                                List<Integer> elem = xyTime.get(i);
                                if (elem.get(0) == Integer.parseInt(arrSt[0])) {
                                    elem.add(Integer.parseInt(arrSt[2]));
                                    elem.add(Integer.parseInt(arrSt[3]));
                                    elem.add(Integer.parseInt(arrSt[4]));
                                    elem.add(Integer.parseInt(arrSt[5]));
                                    elem.add(Integer.parseInt(arrSt[6]));
                                    //System.out.println("added elements to list");
                                }
                                if (elem.get(0) > Integer.parseInt(arrSt[0])) {
                                    break;
                                }
                            }
                        } else {
                            for (int i = 0; i < xyTime.size(); i++) {
                                List<Integer> elem = xyTime.get(i);
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
                    }
                    /*System.out.println("---> не выводится почему????");
                    for (List<Integer> elem : xyTime) {
                        System.out.println(elem);
                    }*/
                }
                if (line.startsWith("RESOURCEAVAILABILITIES:") && !resourses) {
                    //System.out.println("RESOURCEAVAILABILITIES");
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
                if (precedence && requests && resourses) {
                    //do some algorithm
                    long start = System.currentTimeMillis();
                    lateMomentsRes = AsymptoticSchedule.asymptSchedule(n, xyTime, Tmax, Bres);
                    count++;
                    //System.out.println(" LATE SCHEDULE");
                    /*for (int i = 0; i < lateMomentsRes[0].length; i++){
                        System.out.println("lateSchedule " + i +" : " + lateMomentsRes[0][i]);
                    }*/
                    if (lateMomentsRes[1][0] == MPMtime) {
                        //System.out.println(" ПРОВЕРКА РАСПИСАНИЯ ПРОШЛА УСПЕШНО");
                        countEq++;
                    } else {
                        System.out.println(" ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR!!!");
                        System.out.println(lateMomentsRes[1][0] + "<-- & --> " + MPMtime);
                        lateMomentsRes = AsymptoticSchedule.asymptSchedule(n, xyTime, Tmax, Bres);
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
            //System.out.println(" END OF FILE");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(" Среднее время выполнения одного проекта в мс: " + longCountTime / count);
    }

    public static void main(String[] args) throws IOException {
        LocalTime startTime = LocalTime.now();
        List<File> filesInFolder = Files.list(Paths.get("src/main/resources/j90.sm"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for (File fileElem : filesInFolder){
            //System.out.println("FILE : " + fileElem.getName());
            readLines(fileElem);
        }
        System.out.println(" 90 JOBS ------->");
        System.out.println(" ВСЕГО ПОСТРОЕНО РАСПИСАНИЙ: " + count);
        System.out.println(" ИЗ НИХ ПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countEq);
        System.out.println(" НЕПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countNotEq);
        LocalTime finishTime = LocalTime.now();
        System.out.println(" Начало работы алгоритма: " + startTime);
        System.out.println(" Конец работы алгоритма: " + finishTime);
        /*LocalTime startTime1 = LocalTime.now();
        List<File> filesInFolder120 = Files.list(Paths.get("src/main/resources/j120.sm"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for (File fileElem : filesInFolder120){
            //System.out.println("FILE : " + fileElem.getName());
            readLines(fileElem);
        }
        System.out.println(" 120 JOBS ------->");
        System.out.println(" ВСЕГО ПОСТРОЕНО РАСПИСАНИЙ: " + count);
        System.out.println(" ИЗ НИХ ПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countEq);
        System.out.println(" НЕПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countNotEq);
        //System.out.println(" ВРЕМЯ РАБОТЫ АЛГОРИТМА С УЧЕТОМ ЧТЕНИЯ ИЗ ФАЙЛА: " + );
        LocalTime finishTime1 = LocalTime.now();
        System.out.println(" Начало работы алгоритма: " + startTime1);
        System.out.println(" Конец работы алгоритма: " + finishTime1);*/
    }
}



