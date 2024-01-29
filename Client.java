import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private final String ip;
    private final int port;
    private final String msg;
    public Client(String ip, int port, String msg) {
        this.ip = ip;
        this.port = port;
        this.msg = msg;
    }

    @Override
    public void run() {
        try (Socket stk = new Socket(ip, port)) {
            PrintWriter ps = new PrintWriter(stk.getOutputStream(), true);
            ps.println(msg);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
