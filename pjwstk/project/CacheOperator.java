package pjwstk.project;

import java.awt.*;
import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheOperator {
    private List<String> content = new ArrayList<>();
    private List<String> cachedContent = new ArrayList<>();
    private String path;
    private String realpath;
    private File file;
    private HashMap<String, String> images = new HashMap<String, String>();
    private boolean contentType;
    private BrowserSender browserSender;
    private OutputStream outputStream;
    private String cachedData;

    public CacheOperator(List content, String path)  {
        this.content = content;
        this.path = path;
        this.realpath = path;
        contentType = true;
        replace();

    }
    public void replace()   {
        if(path != null) {
            if (path.contains("http://")) {
                path = path.replace("http://", "");
            }
            if (path.contains("/")) {
                path = path.replace("/", "$");
            }
            if (path.contains(".")) {
                path = path.replace(".", "#");
            }
            if (path.contains("?")) {
                path = path.replace("?", "^");
            }
        }
    }
    public void checkFile() {
        if (path != null) {
            if (path.contains("#jpg")) {
                path = path + ".jpg";
                contentType = false;
            }
            if (path.contains("#png")) {
                path = path + ".png";
                contentType = false;
            }
            if (path.contains("#gif")) {
                path = path + ".gif";
                contentType = false;
            }
            if (path.contains(" "))
                path = path.replace(" ", "");
            file = new File("src\\pjwstk\\project\\CACHE\\" + path);
            if (!file.exists()) {
                try {
                    if (contentType) {

                        file.createNewFile();
                        writeContent();
                    } else {
                        if (path.contains("#jpg")) {
                            images.put(realpath, path);
                        }
                        if (path.contains("#png")) {
                            images.put(realpath, path);
                        }
                        if (path.contains("#gif")) {
                            images.put(realpath, path);
                        }
                        new Downloader().downloadImages(images);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (contentType) {
                    browserSender.send();
                } else {
                    sendImage();
                }
            } else {
                if (contentType) {
                    List<String> tempList = new ArrayList<>();
                    createTempContent();
                    pickContent();
                } else {
                    sendImage();
                }
            }
        }
    }
    public void sendStream(OutputStream stream) {
        this.outputStream = stream;
        browserSender = new BrowserSender(content, stream);
    }
    public void sendImage() { 
        File tempFile = new File("src\\pjwstk\\project\\CACHE\\"+path);
        if(!path.contains("#css") && !path.contains("#js") && !path.contains("#woff"))  {
            byte[] bytearray = new byte[(int)tempFile.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempFile));
                bis.read(bytearray, 0, bytearray.length);
                outputStream.write(bytearray, 0, bytearray.length);
                outputStream.flush();
                bis.close();
            }   catch (Exception e) {
                e.printStackTrace();
            }
        }   else    {
            browserSender.send();
        }
    }
    public void createTempContent() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line;
            while((line = bf.readLine()) != null)   {
                if(line.contains("Last-Modified:")) {
                    cachedData = line;
                }
                if(line.contains("<header>"))   {
                    line = line.replace("<header>", "<header>!CACHED!");
                }
                cachedContent.add(line);
            }
            bf.close();
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void pickContent()   {
        String currentData = "";
        for(int i = 0; i<20; i++) {
            if (content.get(i) != null) {
                String temp = content.get(i);
                if (temp.contains("Last-Modified:")) {
                    currentData = temp;
                }
            }
            if (cachedData != null) {
                if (!cachedData.equals(currentData)) {
                    try {
                        PrintWriter pw = new PrintWriter(file);
                        pw.print("");
                        pw.close();
                        writeContent();
                        browserSender.setList(content);
                        browserSender.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Wysyłam cached content");
                    browserSender.setList(cachedContent);
                    browserSender.send();
                }
            }
        }
    }
    private void writeContent() {
        try {
            PrintWriter pw = new PrintWriter(file);
            for (String t : content) {
                if (t != null) {
                    if(t.contains("<title>"))   {
                        t = t.replace("<title>", "<title>CACHED");
                    }
                    pw.println(t);
                    pw.flush();
                }
            }
        }   catch (FileNotFoundException e) {
            System.err.println("Plik nie istnieje");
        }
    }

}


