package pjwstk.project;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Downloader {
    public Downloader() {

    }

    public void downloadImages(HashMap<String, String> images) {
        String realpath;
        String name;
        for(Map.Entry<String, String> entry : images.entrySet())    {
            realpath = entry.getKey();
            name = entry.getValue();
            boolean http = false;
            boolean www = false;
            try {
                String host = realpath;
                if(host.contains("http://"))    {
                    host = host.replace("http://", "");
                    http = true;
                }
                if(host.contains("www."))   {
                    host = host.replace("www.", "");
                    www = true;
                }
                String tempString = "";
                host = host.substring(0, host.indexOf("/"));
                Socket downloader = new Socket(host, 80);
                try {
                    DataOutputStream dts = new DataOutputStream(downloader.getOutputStream());
                    if(http) tempString = tempString+"http://";
                    if(www) tempString = tempString+"www.";
                    tempString = tempString+host;
                    String tempString2 = realpath;
                    tempString2 = tempString2.replace(tempString, "");
                    dts.writeBytes("GET "+realpath+" HTTP/1.1\r\n");
                    dts.writeBytes("Host: "+host+":80\r\n\r\n");
                    String name2 = "";
                    if(name.contains("#jpg"))   {
                        name2 = name+".jpg";
                    }
                    if(name.contains("#png"))   {
                        name2 = name+".png";
                    }
                    if(name.contains("#gif"))   {
                        name2 = name+".gif";
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream("src\\pjwstk\\project\\CACHE\\"+name);
                    InputStream input = downloader.getInputStream();
                    boolean end = false;

                    byte[] bytes = new byte[2048];
                    int length;
                    while ((length = input.read(bytes)) != -1) {
                        if (end)
                            fileOutputStream.write(bytes, 0, length);
                        else {
                            for (int i = 0; i < 2045; i++) {
                                if (bytes[i] == 13 && bytes[i + 1] == 10 && bytes[i + 2] == 13 && bytes[i + 3] == 10) {
                                    end = true;
                                    fileOutputStream.write(bytes, i+4 , 2048-i-4);
                                    break;
                                }
                            }
                        }
                    }
                    //downloader.getOutputStream().close();
                    //dts.flush();

                    fileOutputStream.close();
                    input.close();
                }catch (Exception e)    {
                    e.printStackTrace();
                }
            }   catch (Exception e) {
                System.out.println(realpath);
                e.printStackTrace();
            }
        }

    }
}
