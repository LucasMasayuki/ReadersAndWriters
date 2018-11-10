import java.util.List;

public class Writers extends Thread {
    private String word = "MODIFICADO";
    private List<String> criticalRegion;
    private int range;
    private static RandomNumbers randomNumbers;
    
    public Writers(List<String> criticalRegion, int range) {
        randomNumbers = new RandomNumbers(range);
        this.criticalRegion = criticalRegion;
        this.range = range;
    }

    public void run() {
        int random;

        for (int i = 0; i <= range; i++) {
            random = randomNumbers.generate();
            criticalRegion.set(random, word); 
        }
    }
}
