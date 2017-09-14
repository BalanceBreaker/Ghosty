package Chat;

/**
 * Created by Alexander Ressl on 09.05.2017 14:35.
 */

import ProjectX.Bot;
import ProjectX.UploadThread;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    // will first hold "Username:", later on "Enter message"
    private JLabel label;
    private JLabel label1;
    // to hold the Username and later on the messages
    private JTextField tf;
    private JTextField text;
    // to hold the server address an the port number
    private JTextField tfServer, tfPort;
    // to Logout and get the list of the users
    private JButton login, logout, whoIsIn;
    // for the chat room
    private JTextArea ta;
    // if it is for connection
    private boolean connected;
    // the Client object
    private Client client;
    // the default port number
    private int defaultPort;
    private String defaultHost;
    Bot bot;

    // Constructor connection receiving a socket number
    public ClientGUI(String host, int port, Bot bot) {
        super("Ghosty Chat");
        this.bot = bot;
        defaultPort = port;
        defaultHost = host;
        setResizable(false);
        // The NorthPanel with:
        JPanel northPanel = new JPanel(new GridLayout(3, 1));

        // the server name anmd the port number
        JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 3));
        // the two JTextField with default value for server address and port number
        tfServer = new JTextField(host);
        tfPort = new JTextField("" + port);
        tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

        serverAndPort.add(new JLabel("Server Address:  "));
        serverAndPort.add(tfServer);
        serverAndPort.add(new JLabel("Port Number:  "));
        serverAndPort.add(tfPort);
        serverAndPort.add(new JLabel(""));
        // adds the Server an port field to the GUI


        // the Label and the TextField
        label = new JLabel("Enter your username below", SwingConstants.CENTER);
        northPanel.add(label);
        tf = new JTextField("");
        tf.setBackground(Color.WHITE);
        tf.addActionListener(this);
        northPanel.add(tf);
        add(northPanel, BorderLayout.NORTH);

        // The CenterPanel which is the chat room
        ta = new JTextArea("Welcome to the Ghosty Chat!\n", 25, 52);
        JPanel centerPanel = new JPanel(new FlowLayout());
        ta.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    Thread run = new Thread(new UploadThread(droppedFiles, client, ta));
                    run.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ta.append(ex.toString());
                }

            }
        });
        centerPanel.add(new JScrollPane(ta));
        label1 = new JLabel("You need to be logged in!", SwingConstants.CENTER);
        centerPanel.add(label1);
        text = new JTextField("");
        text.setBackground(Color.WHITE);
        text.setColumns(53);
        text.addActionListener(this);
        text.setEditable(false);

        centerPanel.add(text);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        add(centerPanel, BorderLayout.CENTER);

        // the 3 buttons
        login = new JButton("Login");
        login.addActionListener(this);
        logout = new JButton("Logout");
        logout.addActionListener(this);
        logout.setEnabled(false);        // you have to login before being able to logout
        whoIsIn = new JButton("List users");
        whoIsIn.addActionListener(this);
        whoIsIn.setEnabled(false);        // you have to login before being able to Who is in


        JPanel southPanel = new JPanel();
        southPanel.add(login);
        southPanel.add(logout);
        southPanel.add(whoIsIn);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
            }
        });

        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent arg0) {
                frame__windowStateChanged(arg0);
                setState(Frame.NORMAL);
            }
        });
        //this.setVisible(true);
        setSize(600, 600);
        //setVisible(true);
        tf.requestFocus();

    }

    public void showGui() {
        this.setVisible(true);
        bot.setUnread(false);
    }

    // called by the Client to append text in the TextArea
    void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
        if (!this.isVisible()) {
            bot.setUnread(true);
        }
    }

    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    void connectionFailed() {
        login.setEnabled(true);
        logout.setEnabled(false);
        whoIsIn.setEnabled(false);
        label.setText("Enter your username below");
        tf.setEditable(true);
        text.setEditable(false);
        // reset port number and host name as a construction time
        tfPort.setText("" + defaultPort);
        tfServer.setText(defaultHost);
        // let the user change them
        tfServer.setEditable(false);
        tfPort.setEditable(false);
        // don't react to a <CR> after the username
        tf.removeActionListener(this);
        connected = false;
    }

    /*
    * Button or JTextField clicked
    */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        // if it is the Logout button
        if (o == logout) {
            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
            tf.setEditable(true);
            text.setEditable(false);
            return;
        }
        // if it the who is in button
        if (o == whoIsIn) {
            client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
            return;
        }

        // ok it is coming from the JTextField
        if (connected) {
            // just have to send the message
            if (text.getText().equalsIgnoreCase(""))
                return;
            client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, client.encrypt(text.getText())));
            text.setText("");
            text.setEditable(true);
            return;
        }


        if (o == login || o == tf) {
            // ok it is a connection request
            String username = tf.getText().trim();

            // empty username ignore it
            if (username.length() == 0) {
                ta.append("Blank Username not allowed!\n");
                return;
            }
            if (username.length() >= 20) {
                ta.append("Username is too long!");
                return;
            }
            tf.setEditable(false);
            text.setEditable(true);
            // empty serverAddress ignore it
            String server = tfServer.getText().trim();
            if (server.length() == 0)
                return;
            // empty or invalid port numer, ignore it
            String portNumber = tfPort.getText().trim();
            if (portNumber.length() == 0)
                return;
            int port = 0;
            try {
                port = Integer.parseInt(portNumber);
            } catch (Exception en) {
                return;   // nothing I can do if port number is not valid
            }

            // try creating a new Client with GUI
            client = new Client(server, port, username, this, bot);
            // test if we can start the Client
            if (!client.start())
                return;
            bot.setClient(client);
            tf.setText(username);
            label.setText("Hello " + username);
            label1.setText("Enter your message below");
            connected = true;

            // disable login button
            login.setEnabled(false);
            // enable the 2 buttons
            logout.setEnabled(true);
            whoIsIn.setEnabled(true);
            // disable the Server and Port JTextField
            tfServer.setEditable(false);
            tfPort.setEditable(false);
            // Action listener for when the user enter a message
            tf.addActionListener(this);
        }

    }

    // to start the whole thing the server
    public static void main(String[] args) {
        new ClientGUI("piboti.zapto.org", 4269, null);
    }

    public void frame__windowStateChanged(WindowEvent e) {
        // minimized
        if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
            setVisible(false);
        }
        // maximized
        else if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {

        }
    }
}

