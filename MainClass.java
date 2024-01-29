import javax.swing.*;

public class MainClass {

    public static void main(String[] args) {
        MyApp app = new MyApp();
        int port = 0;
        try {
            port = Integer.parseInt(JOptionPane.showInputDialog("Enter your port:"));
        } catch (NumberFormatException e) {
            System.exit(1);
        }
        Server s = new Server(port);
        app.setVisible(true);
        s.start();
    }
}
