import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static CriticalRegion criticalRegion;
    private static int size = 100;
    private static List<Object> arrayOfThreads;
    private static int writersLength = 100;
    private static int readersLength = 0;
    private static int timesForProportion = 50;
    private static long average;
    private static Lock lock = null;

    private static void _populateArray() {
        int counterOfReaders = 0;
        int random;
        int index = 0;
        boolean alreadyHaveObject;

        RandomNumbers randomNumbers = new RandomNumbers(size);
        arrayOfThreads = new ArrayList<>(Collections.nCopies(size, null));

        while (index < size) {
            random = randomNumbers.generateAndDontRepeat();

            if (counterOfReaders == readersLength) {
                arrayOfThreads.set(random, new Writers(criticalRegion, size, lock, index));
            } else {
                arrayOfThreads.set(random, new Readers(criticalRegion, size, lock, index));
                counterOfReaders++;
            }

            index++;
        }
//
//        for (int i = 0; i < size; i++) {
//            System.out.println(arrayOfThreads.get(i));
//        }
    }

    private static void _executeThreads() {
        Object object;

        for (int i = 0; i < size; i++) {
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
        long start_implemantation;
        long end_implemantation;
        long start;
        long end;

        //Read file
        criticalRegion = new CriticalRegion("bd.txt");

        System.out.println("Implamentation with readers and writers");

        start_implemantation = System.currentTimeMillis();

        // Repeat 50 times
        while (writersLength != 0 && readersLength != 100) {
            average = 0;

            for (int i = 0; i < timesForProportion; i++) {
                _populateArray();

                start = System.currentTimeMillis();

                _executeThreads();

                try {
                    waitFinishThreads();
                    System.out.println(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(i);

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
