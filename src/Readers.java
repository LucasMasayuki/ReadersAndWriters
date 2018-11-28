import java.util.List;

public class Readers extends Thread {
    private CriticalRegion criticalRegion;
    private static RandomNumbers randomNumbers;
    private int range;
    private Lock lock;
    
    public Readers(CriticalRegion criticalRegion, int range, Lock lock) {
        randomNumbers = new RandomNumbers(range);
        this.criticalRegion = criticalRegion;
        this.range = range;
        this.lock = lock;
    }

    private void _accessRandomIndexInBd() {
        String word;
        int random;

        for (int i = 0; i < range; i++) {
            random = randomNumbers.generate();
            word = criticalRegion.read(random);
        }

        try {
            currentThread().sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (lock != null) {
            try {
                lock.block();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            _accessRandomIndexInBd();
            lock.unblock();
        } else {
            criticalRegion.tryRead();

            _accessRandomIndexInBd();

            criticalRegion.stopRead();
        }
    };
}
