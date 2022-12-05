import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

        //store addresses from file into vector
        List<Integer> addresses = new ArrayList<>();
        try {
            File file = new File("swim.trace");
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String hex = data.substring(4, 12);
                int addressInt = hexToInt(hex);
                addresses.add(addressInt);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not read from file");
        }

        //itterate through addresses determining if hit or not
        double totalCount = 0;
        double directMappedCacheHitCount = 0;
        double setAssociativeCacheFIFOHitCount2Way = 0;
        double setAssociativeCacheLRUHitCount2Way = 0;
        double setAssociativeCacheFIFOHitCount4Way = 0;
        double setAssociativeCacheLRUHitCount4Way = 0;
        double setAssociativeCacheFIFOHitCount8Way = 0;
        double setAssociativeCacheLRUHitCount8Way = 0;
        double fullyAssociativeCacheFIFOHitCount = 0;
        double fullyAssociativeCacheLRUHitCount = 0;
        DirectMappedCache directMappedCache = new DirectMappedCache(8192, 64);
        SetAssociativeCacheFIFO setAssociativeCacheFIFO2Way  = new SetAssociativeCacheFIFO(8192, 2, 64);
        SetAssociativeCacheLRU setAssociativeCacheLRU2Way  = new SetAssociativeCacheLRU(8192, 2, 64);
        SetAssociativeCacheFIFO setAssociativeCacheFIFO4Way  = new SetAssociativeCacheFIFO(8192, 4, 64);
        SetAssociativeCacheLRU setAssociativeCacheLRU4Way  = new SetAssociativeCacheLRU(8192, 4, 64);
        SetAssociativeCacheFIFO setAssociativeCacheFIFO8Way  = new SetAssociativeCacheFIFO(8192, 8, 64);
        SetAssociativeCacheLRU setAssociativeCacheLRU8Way  = new SetAssociativeCacheLRU(8192, 8, 64);
        SetAssociativeCacheFIFO fullyAssociativeCacheFIFO  = new SetAssociativeCacheFIFO(8192, 128, 64);
        SetAssociativeCacheLRU fullyAssociativeCacheLRU  = new SetAssociativeCacheLRU(8192, 128, 64);
        for (int i = 0; i < addresses.size(); i++) {
            int address = addresses.get(i);
            if (directMappedCache.getData(address)) {
                directMappedCacheHitCount++;
            }
            if (setAssociativeCacheFIFO2Way.getData(address)) {
                setAssociativeCacheFIFOHitCount2Way++;
            }
            if (setAssociativeCacheLRU2Way.getData(address)) {
                setAssociativeCacheLRUHitCount2Way++;
            }
            if (setAssociativeCacheFIFO4Way.getData(address)) {
                setAssociativeCacheFIFOHitCount4Way++;
            }
            if (setAssociativeCacheLRU4Way.getData(address)) {
                setAssociativeCacheLRUHitCount4Way++;
            }
            if (setAssociativeCacheFIFO8Way.getData(address)) {
                setAssociativeCacheFIFOHitCount8Way++;
            }
            if (setAssociativeCacheLRU8Way.getData(address)) {
                setAssociativeCacheLRUHitCount8Way++;
            }
            if (fullyAssociativeCacheFIFO.getData(address)) {
                fullyAssociativeCacheFIFOHitCount++;
            }
            if (fullyAssociativeCacheLRU.getData(address)) {
                fullyAssociativeCacheLRUHitCount++;
            }
            totalCount++;
        }
        System.out.println("Direct Mapped Cache Hit Rate: " + (directMappedCacheHitCount / totalCount));
        System.out.println("2-Way Set Associative Cache FIFO Hit Rate: " + (setAssociativeCacheFIFOHitCount2Way / totalCount));
        System.out.println("2-Way Set Associative Cache LRU Hit Rate: " + (setAssociativeCacheLRUHitCount2Way / totalCount));
        System.out.println("4-Way Set Associative Cache FIFO Hit Rate: " + (setAssociativeCacheFIFOHitCount4Way / totalCount));
        System.out.println("4-Way Set Associative Cache LRU Hit Rate: " + (setAssociativeCacheLRUHitCount4Way / totalCount));
        System.out.println("8-Way Set Associative Cache FIFO Hit Rate: " + (setAssociativeCacheFIFOHitCount8Way / totalCount));
        System.out.println("8-Way Set Associative Cache LRU Hit Rate: " + (setAssociativeCacheLRUHitCount8Way / totalCount));
        System.out.println("Fully Associative Cache FIFO Hit Rate: " + (fullyAssociativeCacheFIFOHitCount / totalCount));
        System.out.println("Fully Associative Cache LRU Hit Rate: " + (fullyAssociativeCacheLRUHitCount / totalCount));
    }

    public static int hexToInt(String hex) {
        int intVal = 0;
        char currentChar;
        int exponent = 0;
        int currentInt;
        for (int i = 0; i < hex.length(); i++) {
            currentChar = hex.charAt(hex.length() - i - 1);
            switch (currentChar) {
                case 'a' :
                    currentInt = 10;
                    break;
                case 'b' :
                    currentInt = 11;
                    break;
                case 'c' :
                    currentInt = 12;
                    break;
                case 'd' :
                    currentInt = 13;
                    break;
                case 'e' :
                    currentInt = 14;
                    break;
                case 'f' :
                    currentInt = 15;
                    break;
                default :
                    currentInt = Character.getNumericValue(currentChar);
            }
            intVal += currentInt * Math.pow(16, exponent);
            exponent++;
        }
        return intVal;
    }
}
