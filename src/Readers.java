import java.util.List;

public class Readers extends Thread {
    private CriticalRegion criticalRegion;
    private static RandomNumbers randomNumbers;
    private int range = 100;
    
    public Readers(CriticalRegion criticalRegion, int range) {
        randomNumbers = new RandomNumbers(range);
        this.criticalRegion = criticalRegion;
        this.range = range;
    }

    @Override
    public void run() {
        String word;
        int random;
        criticalRegion.readersInBase(true);
        for (int i = 0; i <= range; i++) {
            random = randomNumbers.generate();
            word = criticalRegion.read(random);
        }

        try {
            currentThread().sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        criticalRegion.readersInBase(false);
    };
}
