import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static CriticalRegion criticalRegion;
    private static int size = 101;
    private static List<Thread> arrayOfThreads = new ArrayList<>(Collections.nCopies(size, null));
    private static int writersLength = 100;
    private static int readersLength = 0;
    private static int timesForProportion = 50;
    private static long average;
    private static Lock lock = null;

    private static void _populateArray() {
        int counterOfReaders = 0;
        int random;
        int index = 0;

        RandomNumbers randomNumbers = new RandomNumbers(size);

        arrayOfThreads = new ArrayList<>(Collections.nCopies(size, null));

        while (index < size) {
            random = randomNumbers.generateAndDontRepeat();

            if (counterOfReaders == readersLength) {
                arrayOfThreads.set(random, new Writers(criticalRegion, size, lock));
            } else {
                arrayOfThreads.set(random, new Readers(criticalRegion, size, lock));
                counterOfReaders++;
            }

            index++;
        }
    }

    private static void _executeThreads() {
        Thread thread;

        for (int i = 0; i < size; i++) {
            thread = arrayOfThreads.get(i);
            thread.start();
        }
    }

    private static void waitFinishThreads() throws InterruptedException {
        Thread thread;
        for (int i = 0; i < size; i++) {
            thread = arrayOfThreads.get(i);
            thread.join();
        }
    }

    private static void _run() {
        long start_implemantation;
        long end_implemantation;
        long start;
        long end;

        //Read file
        criticalRegion = new CriticalRegion("bd.txt");

        start_implemantation = System.currentTimeMillis();

        // Repeat 50 times
        while (writersLength >= 0 && readersLength <= 100) {
            average = 0;

            for (int i = 0; i < timesForProportion; i++) {
                _populateArray();

                start = System.currentTimeMillis();

                _executeThreads();

                try {
                    waitFinishThreads();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                end = System.currentTimeMillis();

                average += (end - start);
            }

            average /= timesForProportion;

            System.out.println("Average - " + writersLength + " writers and " + (100 - writersLength) + " readers - " + average + " ms");

            readersLength++;
            writersLength--;
        }

        end_implemantation = System.currentTimeMillis();
        System.out.println("End of " + (end_implemantation - start_implemantation) + "ms");

    }

    private static void _reset() {
        writersLength = 100;
        readersLength = 0;
    }

    public static void main(String args[])  {
        Long start;
        Long end;

        System.out.println("Implamentation with readers and writers");
        start = System.currentTimeMillis();

        _run();

        end = System.currentTimeMillis();

        System.out.println("Duration of 1 implamentation = " + (end - start) + " ms ");

        _reset();

        // Instance lock for the second implemantation without writer and readers
        lock = new Lock();

        System.out.println("Implamentation without readers and writers");
        start = System.currentTimeMillis();

        _run();

        end = System.currentTimeMillis();

        System.out.println("Duration of 2 implamentation = " + (end - start) + " ms ");
    }
}
