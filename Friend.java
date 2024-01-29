public class Friend {
    private final String name;
    private final String ip;
    private final int port;

    public Friend(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
