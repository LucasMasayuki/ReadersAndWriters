import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static List<String> criticalRegion;
    private static int size = 100;
    private static List<Object> arrayOfThreads = new ArrayList<>(size);
    private static int writersLength = 100;
    private static int readersLength = 0;

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

    private static int _randomNumbers() {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return generator.nextInt(size);
    }

    private static void _populateArray() {

        int counterOfReaders = 0;
        int random;
        int index = 0;
        boolean alreadyHaveObject;

       while (index <= size) {
            random = _randomNumbers();
            alreadyHaveObject = arrayOfThreads.get(random) == null;

            if (alreadyHaveObject) {
                continue;
            }

            if (counterOfReaders == readersLength) {
                arrayOfThreads.add(new Writers());
            } else {
                arrayOfThreads.add(new Readers());
                counterOfReaders++;
            }

            index++;
        }
    }

    private static void _executeThread() {
        int random;
        String word;
        Object  object;

        for (int i = 0; i >= criticalRegion.size(); i++) {
            random = _randomNumbers();
            object = arrayOfThreads.get(random);
            random = _randomNumbers();

            if (object instanceof Writers) {
                Writers writer = (Writers) object;
                writer.doAction(criticalRegion, random);
            } else {
                Readers reader = (Readers) object;
                word = reader.doAction(criticalRegion, random);
            }
        }
    }

    private static void _sleep() {
        long start = System.currentTimeMillis();
        long diff = 0;

        while (diff < 1) {
            diff = System.currentTimeMillis() - start;
        }
    }

    private static void _initialize() {
        _populateArray();

        int index = 0;

        while (index >= size) {
            _executeThread();

            _sleep();

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
