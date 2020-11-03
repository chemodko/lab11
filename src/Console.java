package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Console {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private List<String> history = new ArrayList<>();

    public void saveToHistory(String msg) {
        history.add(msg);
    }

    public synchronized String read() throws IOException {
        var msg = reader.readLine();
        return msg;
    }

    public List<String> dump() {
        return history;
    }
}