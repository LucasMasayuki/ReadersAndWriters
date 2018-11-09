import java.util.List;

public class Writers extends Thread {
    private String word = "MODIFICADO";

    public void doAction(List<String> criticalRegion, int index) {
        criticalRegion.set(index, word);
    }
}
