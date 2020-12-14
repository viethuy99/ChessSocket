package mvc;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Server {
    private final int port;
    public static ArrayList<Socket> ListSK;
    public static List<String> ListUser;


    public Server(int port) {
        this.port = port;
    }

    private void execute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        WriteServer write = new WriteServer();
        write.start();
        ListUser = new ArrayList<String>();
        Server.ListUser = Collections.synchronizedList(Server.ListUser);
        System.out.println("Server is listening ...");
        while (true) {
            Socket socket = server.accept();
            System.out.println("Da ket noi voi " + socket);
            Server.ListSK.add(socket);
            ReadServer read = new ReadServer(socket);
            read.start();
        }
    }

    public static void main(String[] args) throws IOException {
        Server.ListSK = new ArrayList<>();
        Server server = new Server(8888);
        server.execute();
    }
}

class ReadServer extends Thread {
    private final Socket socket;
    public ReadServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (true) {
                String sms = dis.readUTF();
                System.out.println(sms);
                if (sms.charAt(0) == 'm') {
                    for (Socket item : Server.ListSK) {
                        System.out.println(item.getPort());
                        System.out.println(sms);
                        DataOutputStream dos = new DataOutputStream(item.getOutputStream());
                        dos.writeUTF(sms);
                    }
                }
                if (sms.charAt(0) == 'a') {
                    Server.ListUser.add(sms.substring(1));
                    System.out.println(Server.ListUser.size());
                    // we must use synchronize block to avoid non-deterministic behavior
                    synchronized (Server.ListUser) {
                        Iterator<String> itr = Server.ListUser.iterator();
                        while (itr.hasNext()) {
                            System.out.println(itr.next());
                        }
                    }
                    System.out.println();
                }
                if (sms.charAt(0) == 'l') {
                    synchronized (Server.ListUser) {
                        StringBuffer sbr = new StringBuffer("");
                        Iterator<String> itr = Server.ListUser.iterator();
                        while (itr.hasNext()) {
                            sbr.append("," + itr.next());
                        }

                        for (Socket item : Server.ListSK) {
                            DataOutputStream dos = new DataOutputStream(item.getOutputStream());
                            dos.writeUTF(new String(sbr));
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}

class WriteServer extends Thread {

    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = new Scanner(System.in);
        while(true) {
            String sms = sc.nextLine();
            try {
                for (Socket item : Server.ListSK) {
//                    item.get
                    dos = new DataOutputStream(item.getOutputStream());
                    dos.writeUTF(sms);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
