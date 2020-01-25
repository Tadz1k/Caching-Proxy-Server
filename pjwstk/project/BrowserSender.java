package pjwstk.project;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class BrowserSender {
    private List<String> content = new ArrayList<>();
    private OutputStream toBrowser;
    private ContentParser contentParser;
    private boolean isText;

    public BrowserSender(List content, OutputStream toBrowser)  {
        this.content = content;
        this.toBrowser = toBrowser;
        this.contentParser = new ContentParser();
        this.isText = false;
    }

    public void send()  {
        DataOutputStream dts = new DataOutputStream(toBrowser);
        try {
            for (String t : content) {
                if(t != null) {
                    if(t.contains("Transfer-Encoding: "))    {
                        t = "Transfer-Encoding: identity";
                    }
                    if(t.contains("Content-Type: text/"))   {
                        isText = true;
                    }
                    if(isText)  {
                        t = contentParser.modify(t);
                    }
                    dts.writeBytes(t + "\r\n");
                }
                dts.flush();
            }
            dts.close();
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setList(List<String> newContent)   {
        this.content = newContent;
    }
}
