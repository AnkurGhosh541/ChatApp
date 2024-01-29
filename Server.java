import javax.swing.text.BadLocationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final int port;
    public Server(int port) {
        this.port = port;
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started at port "+port);
            while (true) {
                Socket stk = serverSocket.accept();
                System.out.println("Client connected at " + stk.getInetAddress().getHostAddress());
                BufferedReader br = new BufferedReader(new InputStreamReader(stk.getInputStream()));
                String msg = br.readLine();
                MyApp.addMessageToChat(stk.getInetAddress().getHostAddress(), msg, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
