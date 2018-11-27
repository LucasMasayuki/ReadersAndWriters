import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNumbers {
    int range = 0;
    List<Integer> numbers;
    Random generator = new Random();

    private void _populate() {
        for (int i = 0; i < range; i++) {
            numbers.add(i);
        }
    }

    public RandomNumbers(int range) {
        this.range = range;
        numbers = new ArrayList<>();

        _populate();
    }

    public int generate() {
        return generator.nextInt(range);
    }

    public int generateAndDontRepeat() {
        return numbers.remove(generator.nextInt(numbers.size()));
    }
}
