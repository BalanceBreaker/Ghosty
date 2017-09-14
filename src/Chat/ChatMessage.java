package Chat;

/**
 * Created by Alexander Ressl on 09.05.2017 11:46.
 */

import java.io.*;

/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server.
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    // DATA to send files between clients.
    public static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, DATA = 3, KEY = 4, VERSION = 5, UPDATE = 6, REFRESH = 7;
    private int type;
    private String message;
    private byte[] messageByte;
    private String username;
    private String name;
    int key;
    double version;
    boolean press;
    byte[] b;

    // constructor
    ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;

    }

    public ChatMessage(int type, int key, boolean press) {
        this.type = type;
        this.key = key;
        this.press = press;
    }

    ChatMessage(int type, String message, String username) {
        this.type = type;
        this.message = message;
        this.username = username;
    }

    public ChatMessage(int type, byte[] b, String name) {
        this.type = type;
        this.b = b;
        this.name = name;
    }

    public ChatMessage(int type, byte[] b, String name, String username) {
        this.type = type;
        this.b = b;
        this.name = name;
        this.username = username;
    }

    public ChatMessage(int type, double version) {
        this.type = type;
        this.version = version;
    }

    public ChatMessage(int type, byte[] encrypt) {
        this.type = type;
        this.messageByte = encrypt;
    }

    // getters
    int getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    byte[] getB() {
        return b;
    }

    String getUsername() {
        return username;
    }

    String getName() {
        return name;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public int getKEY() {
        return key;
    }

    public boolean isPress() {
        return press;
    }

    public double getVersion() {
        return version;
    }

    public byte[] getMessageByte() {
        return messageByte;
    }
}

