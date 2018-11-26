import java.util.ArrayList;
import java.util.List;

public class Main {
    private static CriticalRegion criticalRegion;
    private static int size = 100;
    private static List<Object> arrayOfThreads = new ArrayList<>();
    private static int writersLength = 100;
    private static int readersLength = 0;
    private static int timesForProportion = 50;
    private static long average;
    private static Lock lock = null;
    private static RandomNumbers randomNumbers = new RandomNumbers(size);

    private static void _populateArray() {
        int counterOfReaders = 0;
        int random;
        int index = 0;
        boolean alreadyHaveObject;

        arrayOfThreads.clear();

        while (index <= size) {
            random = randomNumbers.generate();

            try {
                alreadyHaveObject = arrayOfThreads.get(random) == null;
            } catch (IndexOutOfBoundsException ex) {
                alreadyHaveObject = false;
            }

            if (alreadyHaveObject) {
                continue;
            }

            if (counterOfReaders == readersLength) {
                arrayOfThreads.add(new Writers(criticalRegion, size, lock, index));
            } else {
                arrayOfThreads.add(new Readers(criticalRegion, size, lock, index));
                counterOfReaders++;
            }

            index++;
        }
    }

    private static void _executeThreads() {
        Object object;

        for (int i = 0; i <= size; i++) {
            object = arrayOfThreads.get(i);

            if (object instanceof Writers) {
                Writers writer = (Writers) object;
//                int index = writer.getIndex();
//                System.out.println("thread number:" + index);
                writer.start();
            } else {
                Readers reader = (Readers) object;
//                int index = reader.getIndex();
//                System.out.println("thread number:" + index);
                reader.start();
            }
        }
    }

    private static void waitFinishThreads() throws InterruptedException {
        Object object;
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

    private static void _run() {
        long start;
        long end;

        //Read file
        criticalRegion = new CriticalRegion("bd.txt");

        // Repeat 50 times
        while (writersLength >= timesForProportion) {
            start = System.currentTimeMillis();

            _populateArray();
            _executeThreads();

            try {
                waitFinishThreads();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            readersLength++;
            writersLength--;

            end = System.currentTimeMillis();

            average += (end - start);
        }

        average /= timesForProportion;

        System.out.println("Average - " + writersLength + " writers and " + (100 - writersLength) + " readers - " + average + " ms");
    }

    private static void _reset() {
        readersLength = 100;
        writersLength = 0;
    }

    public static void main(String args[])  {
        Long start;
        Long end;

        start = System.currentTimeMillis();

        _run();

        end = System.currentTimeMillis();

        System.out.println("Duration of 1 implamentation = " + (end - start) + " ms ");

        _reset();

        // Instance lock for the second implemantation without writer and readers
        lock = new Lock();
        System.out.println("first finish");

        _run();
    }
}
