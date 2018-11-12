import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CriticalRegion {
    private List<String> data;
    private boolean haveReaders = false;

    public CriticalRegion(String fileName) {
        data = _readFile(fileName);
    }

    public void readersInBase(boolean haveReaders) {
        this.haveReaders = haveReaders;
    }

    public void write(int index, String word) {
        data.set(index, word);
    }

    public String read(int index) {
        return data.get(index);
    }

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
}
