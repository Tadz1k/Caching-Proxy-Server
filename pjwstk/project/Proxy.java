package pjwstk.project;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Proxy extends Thread   {
    private Socket fromBrowser;

    public Proxy(Socket fromBrowser)    {
        super();
        this.fromBrowser = fromBrowser;
        try {
            this.fromBrowser.setSoTimeout(20000);
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run()   {
        try {
            InputStream inputFromBrowser = fromBrowser.getInputStream();
            BufferedReader bfFromBrowser = new BufferedReader(new InputStreamReader(inputFromBrowser));
            HeaderHandler headerHandler = new HeaderHandler(bfFromBrowser);
            Socket toHttp = new Socket(headerHandler.getHost(), 80);
            OutputStream streamToHttp = toHttp.getOutputStream();
            RequestSender requestSender = new RequestSender(headerHandler.getHeaders(), streamToHttp);
            requestSender.send();
            InputStream inputFromHttp = toHttp.getInputStream();
            BufferedReader bfFromHttp = new BufferedReader(new InputStreamReader(inputFromHttp));
            ResponseHandler responseHandler = new ResponseHandler(bfFromHttp);
            responseHandler.read();
            CacheOperator cacheOperator = new CacheOperator(responseHandler.getContent(), headerHandler.getPath());
            OutputStream streamToBrowser = fromBrowser.getOutputStream();
            cacheOperator.sendStream(streamToBrowser);
            cacheOperator.checkFile();


            bfFromBrowser.close();
            inputFromBrowser.close();
            streamToHttp.close()
            bfFromHttp.close();
            inputFromHttp.close();
            streamToBrowser.close();
            fromBrowser.close();
            toHttp.close();
        }    catch (IOException e)   {
            e.printStackTrace();
        }
    }
}
