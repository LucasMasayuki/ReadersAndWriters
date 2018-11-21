public class Lock {
    boolean isBlocked = false;
    Thread blockedBy = null;
    int countBlockeds = 0;

    /*enquanto a região crítica estiver bloqueada e estiver bloqueado pela thread atual, espera. Quando sai desse loop, bloqueia*/
    public synchronized void block() throws InterruptedException {
        Thread currentThread = Thread.currentThread();

        while (isBlocked && blockedBy != currentThread) {
            wait();
        }

        isBlocked = true;
        countBlockeds++;
        blockedBy = currentThread;
    }

    /*tira 1 do contador de bloqueados e verifica se a região crítica ainda está bloqueada*/
    public synchronized void unblock() {
        if (Thread.currentThread() == this.blockedBy) {
            countBlockeds--;

            if (countBlockeds == 0) {
                isBlocked = false;
                notify();
            }
        }
    }
}
