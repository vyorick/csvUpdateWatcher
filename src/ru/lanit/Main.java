package ru.lanit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        CsvUpdateReader cur = new CsvUpdateReader("C:\\Users\\danilishe\\Documents\\csvIncReader\\src\\ru\\lanit\\example.csv");
        while (true) {
            List<HashMap<String, String>> data = cur.read();
            if (!data.isEmpty()) {
                printData(data);
            } else {
                System.out.println("No changes.\n");
            }

            System.out.println("Waiting...");
            Thread.sleep(15000);
            System.out.println("Starting now!");
            Thread.sleep(1500);
        }
    }

    private static void printData(List<HashMap<String, String>> data) {
        System.out.println("-- start -------------------------------");
        for (HashMap<String, String> map : data) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        System.out.println("--------------------------------- end --\n");
    }
}
