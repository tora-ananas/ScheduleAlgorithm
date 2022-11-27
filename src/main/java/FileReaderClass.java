import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderClass {
    static int n = 0;
    static int Tmax = 0;
    static List<List<Integer>> xyTime = new ArrayList<>();
    static int[] Bres = new int[4];

    public static void readLines(){
        File file = new File("j30.sm");

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            boolean project = false;
            // когда нашли строчку с проджектом, тогда заходим в цикл, считываем информацию,
            // ЗАНОСИМ ДАННЫЕ Во все переменные и считаем расписание. только после того как посчитали,
            // считываем дальше из файла
            //TODO
            for (int i = 0; i < 25; i++){

                if ((line = br.readLine()) != null){

                    if (line.startsWith("*")) continue;

                    if (line.startsWith("jobs (incl. supersource/sink )")) {
                        String[] arrSt = line.split("  ");
                        System.out.println(arrSt[1]);
                        n = Integer.parseInt(arrSt[1]);
                    }

                    if (line.startsWith("horizon")) {
                        String[] arrSt = line.split("\s+");
                        System.out.println(arrSt[2]);
                        Tmax = Integer.parseInt(arrSt[2]);
                    }

                    if (line.startsWith("PRECEDENCE RELATIONS:")) {
                        String nextLine = br.readLine();
                        for (int j = 0; j < 32; j++){
                            nextLine = br.readLine();
                            String[] arrSt = nextLine.split("\s+");
                            System.out.println("len: " + arrSt.length);
                            int len = arrSt.length;
                            for (int n = 4; n < len; n++){
                                List<Integer> element = new ArrayList<>();
                                element.add(Integer.parseInt(arrSt[1]));
                                element.add(Integer.parseInt(arrSt[n]));
                                System.out.println("add elem: "+Integer.parseInt(arrSt[1])+ " "
                                        + Integer.parseInt(arrSt[n]));
                                xyTime.add(element);
                            }
                        }
                        System.out.println("--->");
                        for (List<Integer> elem: xyTime){
                            System.out.println(elem);
                        }
                    }

                    if (line.startsWith("REQUESTS/DURATIONS:")){
                        String nextLine = br.readLine();
                        br.readLine();
                        for (int j = 0; j < 32; j++){
                            nextLine = br.readLine();
                            String[] arrSt = nextLine.split("\s+");
                            System.out.println(arrSt[1]);
                            for (List<Integer> elem: xyTime){
                                if (elem.get(0) == Integer.parseInt(arrSt[1]) ){
                                    elem.add(Integer.parseInt(arrSt[3]));
                                    elem.add(Integer.parseInt(arrSt[4]));
                                    elem.add(Integer.parseInt(arrSt[5]));
                                    elem.add(Integer.parseInt(arrSt[6]));
                                    elem.add(Integer.parseInt(arrSt[7]));
                                    System.out.println("added elements to list");
                                }
                                if (elem.get(0) > Integer.parseInt(arrSt[1]) ) {
                                    break;
                                }
                            }
                        }
                        System.out.println("--->");
                        for (List<Integer> elem: xyTime){
                            System.out.println(elem);
                        }
                    }

                    if (line.startsWith("RESOURCEAVAILABILITIES:")) {
                        br.readLine();
                        String nextLine = br.readLine();
                        String[] arrSt = nextLine.split("\s+");
                        Bres[0] = Integer.parseInt(arrSt[1]);
                        Bres[1] = Integer.parseInt(arrSt[2]);
                        Bres[2] = Integer.parseInt(arrSt[3]);
                        Bres[3] = Integer.parseInt(arrSt[4]);
                        System.out.println("END");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        readLines();
    }
}


