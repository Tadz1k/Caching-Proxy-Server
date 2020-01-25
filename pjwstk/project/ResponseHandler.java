package pjwstk.project;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ResponseHandler {
    private BufferedReader inputReader;
    List<String> content = new ArrayList<>();

    public ResponseHandler(BufferedReader inputReader) {
        this.inputReader = inputReader;
    }

    public void read()  {
        String line;
        try {
            while ((line = inputReader.readLine()) != null) {
                content.add(line);
            }
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List getContent()    {
        return content;
    }
}
