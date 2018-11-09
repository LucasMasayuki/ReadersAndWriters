import java.util.List;

public class Readers extends Thread {
    public String doAction(List<String> criticalRegion, int index) {
        return criticalRegion.get(index);
    }
}
