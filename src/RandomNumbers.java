import java.util.concurrent.ThreadLocalRandom;

public class RandomNumbers {
    int range = 0;
    public RandomNumbers(int range) {
        this.range = range;
    }
 
    public int generate() {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return generator.nextInt(range);
    }
}
