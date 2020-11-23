package mvc;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private int port;
    public static ArrayList<Socket> ListSK;

    public Server(int port) {
        this.port = port;
    }

    private void execute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        WriteServer write = new WriteServer();
        write.start();
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
    private Socket socket;
    public ReadServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (true) {
                String sms = dis.readUTF();
                for (Socket item : Server.ListSK) {
                    System.out.println(item.getPort());
                    System.out.println(sms);
                    DataOutputStream dos = new DataOutputStream(item.getOutputStream());
                    dos.writeUTF(sms);
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
                    dos = new DataOutputStream(item.getOutputStream());
                    dos.writeUTF(sms);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
