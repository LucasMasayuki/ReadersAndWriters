import java.util.List;

public class Readers extends Thread {
    private List<String> criticalRegion;
    private static RandomNumbers randomNumbers;
    private int range = 100;
    
    public Readers(List<String> criticalRegion, int range) {
        randomNumbers = new RandomNumbers(range);
        this.criticalRegion = criticalRegion;
        this.range = range;
    }
    
    public void run() {
        String word;
        int random;
        for (int i = 0; i <= range; i++) {
            random = randomNumbers.generate();
            word = criticalRegion.get(random);
        }
    }
}
