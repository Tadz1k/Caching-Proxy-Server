package pjwstk.project;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RequestSender {
    private List<String> headers = new ArrayList<>();
    private OutputStream sToHttp;

    public RequestSender(List headers, OutputStream sToHttp) {
        this.headers = headers;
        this.sToHttp = sToHttp;
    }
    public void send()  {
        PrintWriter pw = new PrintWriter(sToHttp);
        for(String t : headers) {
            System.out.println(t);
            pw.println(t);
        }
        pw.flush();
    }
}
