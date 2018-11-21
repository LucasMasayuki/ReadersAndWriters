import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CriticalRegion {
    private List<String> data;
    private int numbersOfReaders = 0;
    private boolean haveWriters = false;

    private List<String> _readFile(String fileName) {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> txt = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                txt.add(line);
            }

            return txt;

        } catch (FileNotFoundException e) {
            System.out.println("The file doesn't exist.");
        } catch (IOException e) {
            System.out.println("The file could not be read.");
        }

        return null;
    }

    public CriticalRegion(String fileName) {
        data = _readFile(fileName);
    }

    public boolean canWrite() {
        return numbersOfReaders > 0 && !haveWriters;
    }

    public boolean canRead() {
        return !haveWriters;
    }

    public void write(int index, String word) {
        data.set(index, word);
    }

    public String read(int index) {
        return data.get(index);
    }

    synchronized void tryWrite() {
        while (!canWrite()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        haveWriters = true;
    }

    synchronized void tryRead() {
        while (!canRead()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.numbersOfReaders++;
    }

    synchronized void stopWrite() {
        haveWriters = false;
        notifyAll();
    }

    synchronized void stopRead() {
        this.numbersOfReaders--;
        notifyAll();
    }
}
