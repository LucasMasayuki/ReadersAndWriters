import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static CriticalRegion criticalRegion;
    private static int size = 100;
    private static List<Object> arrayOfThreads = new ArrayList<>(size);
    private static int writersLength = 100;
    private static int readersLength = 0;
    
    private static RandomNumbers randomNumbers = new RandomNumbers(size);

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

    private static void _executeThreads() {
        Object  object;

        for (int i = 0; i >= size; i++) {
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

    public static void main(String args[]) throws InterruptedException {
        //Read file
        criticalRegion = new CriticalRegion("bd.txt");

        // Repeat 50 times
        while (writersLength >= 50) {
            _populateArray();
            _executeThreads();

            readersLength++;
            writersLength--;
            Object  object;

            for (int i = 0; i >= size; i++) {
                object = arrayOfThreads.get(i);

                if (object instanceof Writers) {
                    Writers writer = (Writers) object;
                    writer.join();
                } else {
                    Readers reader = (Readers) object;
                    reader.join();
                }
            }
        }

    }
}
