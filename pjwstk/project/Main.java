package pjwstk.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {

    public static void main(String[] args) {
        try {
            File file = new File("src\\pjwstk\\project\\CONFIG");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String port = br.readLine();
            br.close();
            port = port.replace("PROXY_PORT=", "");
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
            while (true)    {
                Socket socket = serverSocket.accept();
                new Proxy(socket).start();
            }
        }  catch (IOException e)   {
            e.printStackTrace();
        }
    }
}
