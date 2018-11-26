public class Writers extends Thread {
    private String word = "MODIFICADO";
    private CriticalRegion criticalRegion;
    private int range;
    private static RandomNumbers randomNumbers;
    private Lock lock;
    private int index;

    public Writers(CriticalRegion criticalRegion, int range, Lock lock, int index) {
        randomNumbers = new RandomNumbers(range);
        this.criticalRegion = criticalRegion;
        this.range = range;
        this.lock = lock;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    private void _accessRandomIndexInBd() {
        int random;

        for (int i = 0; i <= range; i++) {
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
            System.out.println("second start");
            try {
                lock.block();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            _accessRandomIndexInBd();
            lock.unblock();
        } else {
            criticalRegion.tryWrite();

            _accessRandomIndexInBd();

            criticalRegion.stopWrite();
        }
    }
}
