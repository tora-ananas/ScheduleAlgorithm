import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderClass {
    static int n = 0;
    static int Tmax = 0;
    static List<List<Integer>> xyTime;
    static int[] Bres = new int[4];
    static int[][] lateMomentsRes;
    static int MPMtime = 0;
    static int countNotEq = 0;
    static int countEq = 0;
    static int count = 0;


    public static void readLines(){
        File file = new File("src/main/resources/j30.sm");

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            boolean project = false;

            //читаем из файла до самого конца
            line = br.readLine();
            while(line != null){
                //нашли строчку с проджектом и зашли в след цикл.
                if (line.startsWith("projects")) {
                    System.out.println(" NEW PROJECT!");
                    xyTime = new ArrayList<>();
                    boolean precedence = false;
                    boolean requests = false;
                    boolean resourses = false;
                    line = br.readLine();
                    while (line != null && !line.startsWith("file with basedata")) {
                        if (line.startsWith("jobs (incl. supersource/sink )")) {
                            String[] arrSt = line.split("  ");
                            System.out.println(arrSt[1]);
                            n = Integer.parseInt(arrSt[1]);
                            //читаем дальше..
                            line = br.readLine();
                        }
                        if (line.startsWith("horizon")) {
                            String[] arrSt = line.split("\s+");
                            System.out.println(arrSt[2]);
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
                            System.out.println("END");
                        }
                        line = br.readLine();
                        if (precedence && requests && resourses){
                            //do some algorithm
                            lateMomentsRes = AsymptoticSchedule.asymptSchedule(n, xyTime, Tmax, Bres);
                            count++;
                            System.out.println(" LATE SCHEDULE");
                            for (int i = 0; i < lateMomentsRes[0].length; i++){
                                System.out.println("lateSchedule " + i +" : " + lateMomentsRes[0][i]);
                            }
                            if (lateMomentsRes[1][0] == MPMtime ){
                                System.out.println(" ПРОВЕРКА РАСПИСАНИЯ ПРОШЛА УСПЕШНО");
                                countEq++;
                            } else {
                                System.out.println(" ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR!!!");
                                countNotEq++;
                            }
                            System.out.println("  DONE ASYMPTOTIC SCHEDULE");
                        }
                    }
                } else {
                    line = br.readLine();
                }

            }
            System.out.println(" END OF FILE");
            System.out.println(" ВСЕГО ПОСТРОЕНО РАСПИСАНИЙ: " + count);
            System.out.println(" ИЗ НИХ ПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countEq);
            System.out.println(" НЕПРАВИЛЬНЫХ РАСПИСАНИЙ: " + countNotEq);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readLines();
    }
}


