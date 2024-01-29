import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class MyApp extends JFrame implements ActionListener {
    private final HashMap<String, Friend> friendList;
    private static HashMap<String, String> ipList;

    JList<String> friendHistory;
    JTextField nameField, ipField, portField;
    JButton addBtn, sendBtn;
    static JPanel chatHistory;

    public MyApp() {
        setTitle("My Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        init();
        friendList = new HashMap<>();
        ipList = new HashMap<>();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints;

        JPanel chatPanel = new JPanel(new GridBagLayout());
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 8));
        JLabel chatLabel = new JLabel("Chat History");

        JScrollPane jsp = new JScrollPane();
        jsp.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        chatHistory = new JPanel();
        chatHistory.setLayout(new BoxLayout(chatHistory, BoxLayout.Y_AXIS));
        chatHistory.setBackground(Color.WHITE);

        jsp.setViewportView(chatHistory);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        chatPanel.add(chatLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.8;
        constraints.weighty = 0.8;
        chatPanel.add(jsp, constraints);

        JPanel friendPanel = new JPanel(new GridBagLayout());
        friendPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 20, 15));
        JLabel friendLabel = new JLabel("Friends");

        friendHistory = new JList<>(new DefaultListModel<>());
        friendHistory.setBackground(Color.white);
        friendHistory.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        friendHistory.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        friendHistory.setPreferredSize(new Dimension(10, 10));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        friendPanel.add(friendLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.8;
        constraints.weighty = 0.8;
        friendPanel.add(friendHistory, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.7;
        constraints.weighty = 1;
        mainPanel.add(chatPanel, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.3;
        constraints.weighty = 1;
        mainPanel.add(friendPanel, constraints);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel name = new JLabel("Name");
        JLabel IP = new JLabel("IP");
        JLabel port = new JLabel("Port");

        name.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        IP.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        port.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        nameField = new JTextField(30);
        ipField = new JTextField(15);
        portField = new JTextField(5);

        nameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        ipField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        portField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        addBtn = new JButton("Add");
        sendBtn = new JButton("Send");

        addBtn.addActionListener(this);
        sendBtn.addActionListener(this);

        bottomPanel.add(name);
        bottomPanel.add(nameField);
        bottomPanel.add(IP);
        bottomPanel.add(ipField);
        bottomPanel.add(port);
        bottomPanel.add(portField);
        bottomPanel.add(addBtn);
        bottomPanel.add(sendBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void addMessageToChat(String senderIp, String msg, boolean isSent) {
        String senderName = ipList.get(senderIp);
        if (senderName == null) {
            senderName = "Unknown(" + senderIp + ")";
        }
        String text;
        if (isSent) {
            text = "<html>To:<b>" + senderName + "</b><br>" + msg;
        } else {
            text = "<html>From:<b>" + senderName + "</b><br>" + msg;
        }

        System.out.println(senderName + " " + msg);
        JLabel l = new JLabel(text);
        l.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        chatHistory.add(l);
        chatHistory.add(Box.createRigidArea(new Dimension(0, 10)));

        chatHistory.revalidate();
        chatHistory.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("add")) {
            String name = nameField.getText();
            String ip = ipField.getText();
            int port;
            if (name.isBlank() || ip.isBlank()) {
                JOptionPane.showMessageDialog(this, "No Fields should be empty!");
                return;
            }
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid port");
                return;
            }
            nameField.setText("");
            ipField.setText("");
            portField.setText("");
            Friend friend = new Friend(name, ip, port);
            friendList.put(name, friend);
            ipList.put(ip, name);

            DefaultListModel<String> model = (DefaultListModel<String>) friendHistory.getModel();
            model.addElement(name);
        } else if (e.getActionCommand().equalsIgnoreCase("send")) {
            if (friendHistory.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No friend selected");
                return;
            }

            String msg = JOptionPane.showInputDialog(this, "Enter the message");
            if (msg == null || msg.isBlank()) {
                JOptionPane.showMessageDialog(this, "No message entered");
                return;
            }

            List<String> selectedFriends = friendHistory.getSelectedValuesList();
            for (String s : selectedFriends) {
                Friend f = friendList.get(s);
                String ip = f.getIp();
                int port = f.getPort();
                Client c = new Client(ip, port, msg);
                c.start();
                addMessageToChat(ip, msg, true);
            }
        }
    }
}
