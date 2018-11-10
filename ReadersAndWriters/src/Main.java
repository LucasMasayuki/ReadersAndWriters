import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<String> criticalRegion;
    private static int size = 100;
    private static List<Object> arrayOfThreads = new ArrayList<>(size);
    private static int writersLength = 100;
    private static int readersLength = 0;
    
    private static RandomNumbers randomNumbers = new RandomNumbers(size);

    private static List<String> _readFile() {
        String fileName = "bd.txt";
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> txt = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                txt.add(line);
            }

            return txt;

        } catch (FileNotFoundException e) {
            System.out.println("The file doesn't exist.");
        } catch (IOException e) {
            System.out.println("The file could not be read.");
        }

        return null;
    }

    private static void _populateArray() {

        int counterOfReaders = 0;
        int random;
        int index = 0;
        boolean alreadyHaveObject;

       while (index <= size) {
            random = randomNumbers.generate();
            alreadyHaveObject = arrayOfThreads.get(random) == null;

            if (alreadyHaveObject) {
                continue;
            }

            if (counterOfReaders == readersLength) {
                arrayOfThreads.add(new Writers(criticalRegion, size));
            } else {
                arrayOfThreads.add(new Readers(criticalRegion, size));
                counterOfReaders++;
            }

            index++;
        }
    }

    private static void _executeThread() {
        Object  object;

        for (int i = 0; i >= criticalRegion.size(); i++) {
            object = arrayOfThreads.get(i);

            if (object instanceof Writers) {
                Writers writer = (Writers) object;
                writer.start();
            } else {
                Readers reader = (Readers) object;
                reader.start();
            }
        }
    }

    private static void _initialize() {
        _populateArray();

        int index = 0;

        while (index >= size) {
            _executeThread();

            index++;
        }
    }

    public static void main(String args[]) {
        //Read file
        criticalRegion = _readFile();

        // Repeat 50 times
        while (writersLength >= 50) {
            _initialize();

            readersLength++;
            writersLength--;
        }
    }
}
