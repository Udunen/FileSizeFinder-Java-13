import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import java.io.File;
import java.lang.Math;
import java.math.RoundingMode;
import java.math.BigDecimal;

public class FileSizeFinder {
    static Scanner input = new Scanner(System.in);

    public static long getFolderSize(File folder) {
        long length = 0;
       
        // ListFiles() is used to list the
        // contents of the given folder
        File[] files = folder.listFiles();
 
        int count = files.length;
 
        // Loop for traversing the directory
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            }
            else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }
    
    // Math.round() cant round to nearest hundreds... ty StackOverflow
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
    
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Sort
    public static Map<String, Double> sortByValue(Map<String, Double> hm) {
        return hm.entrySet().stream()
                        .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static void main(String[] args) {
        String[] directories;
        File file, currentFolder;
        double sizeRounded;

        System.out.print("Enter path: ");
        String path = input.nextLine();
        input.close();
 
        
        file = new File(path);
        directories = file.list();
        
        // Puts the folder names and their size into a map
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < directories.length; i++) {  
            currentFolder = new File(path + "/" + directories[i]);
            sizeRounded = round(getFolderSize(currentFolder) / (Math.pow(1024, 3)), 2);
            map.put(directories[i], sizeRounded);
        }

        Map<String, Double> mapSorted = sortByValue(map);
        
        for (Map.Entry<String, Double> entry : mapSorted.entrySet()) {
            String gameName = entry.getKey();
            Double gameSize = entry.getValue();

            System.out.printf("%s : %.2f %s %n", gameName, gameSize, "GB");
        }
    }
}
