package pjwstk.project;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HeaderHandler {
    private BufferedReader bfFromBrowser;
    private String line;
    private List<String> headers = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private String host;
    private String folderName;
    private String path;

    public HeaderHandler(BufferedReader bfFromBrowser)   {
        this.bfFromBrowser = bfFromBrowser;
        read();
    }

    public void read()  {
        try {
            if ((line = bfFromBrowser.readLine()) != null) {
                if(line.contains("GET ") && line.contains("HTTP/"))  {
                    headers.add(line);
                    path = line;
                    path = path.replace("GET ", "");
                    if(path.contains("HTTP/1.1"))   {
                        path = path.replace("HTTP/1.1", "");
                    }
                    if(path.contains("HTTP/1.0"))   {
                        path = path.replace("HTTP/1.0", "");
                    }
                }
                if((line = bfFromBrowser.readLine()).contains("Host: ") && line != "Host: ") {
                    setHost(line);
                    headers.add(line);
                }
            }
            headers.add("Accept-Encoding: identity");
            headers.add("Connection: close");
            headers.add("");
        }   catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setHost(String line)   {
        host = line;
        if(host.contains("Host: ")) {
            host = host.replace("Host: ", "");
        }
        if(host.contains(":"))  {
            host = host.substring(0, host.indexOf(":"));
        }
        if(host.contains("www."))   {
            host = host.replace("www.", "");
        }
        if(host.contains("http://"))    {
            host = host.replace("http://", "");
        }
        if(host.contains("/"))  {
            host = host.substring(0, host.indexOf("/"));
        }
    }
    public String getHost() {
        return host;
    }
    public List<String> getHeaders()   {
        return headers;
    }
    public List getImages() {
        return images;
    }
    public String getFolder()   {
        return folderName;
    }
    public String getPath() {
        return path;
    }
}
