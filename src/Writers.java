import java.util.List;

public class Writers extends Thread {
    private String word = "MODIFICADO";
    private CriticalRegion criticalRegion;
    private int range;
    private static RandomNumbers randomNumbers;
    
    public Writers(CriticalRegion criticalRegion, int range) {
        randomNumbers = new RandomNumbers(range);
        this.criticalRegion = criticalRegion;
        this.range = range;
    }

    public void run() {
        int random;

        for (int i = 0; i <= range; i++) {
            random = randomNumbers.generate();
            criticalRegion.write(random, word);
        }

        try {
            currentThread().sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
