package Chat;

/**
 * Created by Alexander Ressl on 09.05.2017 11:47.
 */

import ProjectX.Bot;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

    // for I/O
    private ObjectInputStream sInput;        // to read from the socket
    private ObjectOutputStream sOutput;        // to write on the socket
    private Socket socket;
    private SimpleDateFormat sdf;
    // if I use a GUI or not
    private ClientGUI cg;

    // the server, the port and the username
    private String server, username;
    private int port;
    private boolean gone = false;
    Bot bot;
    Robot robot;

    /*
     *  Constructor called by console mode
     *  server: the server address
     *  port: the port number
     *  username: the username
     */
    Client(String server, int port, String username) {
        // which calls the common constructor with the GUI set to null
        this(server, port, username, null, null);
        sdf = new SimpleDateFormat("HH:mm:ss");
    }

    /*
     * Constructor call when used from a GUI
     * in console mode the ClienGUI parameter is null
     */
    Client(String server, int port, String username, ClientGUI cg, Bot bot) {
        this.server = server;
        this.port = port;
        this.username = username;
        // save if we are in GUI mode or not
        this.cg = cg;
        sdf = new SimpleDateFormat("HH:mm:ss");
        this.bot = bot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println(e);
        }
    }

    /*
     * To start the dialog
     */
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        }
        // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connected!";
        display(msg);

		/* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(username);
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    /*
     * To send a message to the console or the GUI
     */
    private void display(String msg) {
        if (cg == null)
            System.out.println(msg);      // println in console mode
        else
            cg.append(msg + "\n");        // append to the ClientGUI JTextArea (or whatever)
    }

    /*
     * To send a message to the server
     */
    public void sendMessage(ChatMessage msg) {
        try {
            if (msg.getType() != 1)
                sOutput.writeObject(msg);
            else if (!msg.getMessage().equalsIgnoreCase(""))
                sOutput.writeObject(msg);


        } catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        gone = true;
        try {
            if (sInput != null) sInput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) sOutput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        } // not much else I can do

        // inform the GUI
        if (cg != null)
            cg.connectionFailed();

    }

    /*
     * To start the Client in console mode use one of the following command
     * > java Client
     * > java Client username
     * > java Client username portNumber
     * > java Client username portNumber serverAddress
     * at the console prompt
     * If the portNumber is not specified 1500 is used
     * If the serverAddress is not specified "localHost" is used
     * If the username is not specified "Anonymous" is used
     * > java Client
     * is equivalent to
     * > java Client Anonymous 1500 localhost
     * are eqquivalent
     *
     * In console mode, if an error occurs the program simply stops
     * when a GUI id used, the GUI is informed of the disconnection
     */
    public static void main(String[] args) {
        // default values
        int portNumber = 1500;
        String serverAddress = "localhost";
        String userName = "Ghosty";

        // depending of the number of arguments provided we fall through
        switch (args.length) {
            // > javac Client username portNumber serverAddr
            case 3:
                serverAddress = args[2];
                // > javac Client username portNumber
            case 2:
                try {
                    portNumber = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
                    return;
                }
                // > javac Client username
            case 1:
                userName = args[0];
                // > java Client
            case 0:
                break;
            // invalid number of arguments
            default:
                System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
                return;
        }
        // create the Client object
        Client client = new Client(serverAddress, portNumber, userName);
        // test if we can start the connection to the Server
        // if it failed nothing we can do
        if (!client.start())
            return;

        // wait for messages from user
        Scanner scan = new Scanner(System.in);
        // loop forever for message from the user
        while (true) {
            System.out.print("> ");
            // read message from user
            String msg = scan.nextLine();
            // logout if message is LOGOUT
            if (msg.equalsIgnoreCase("LOGOUT")) {
                client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                // break to do the disconnect
                break;
            }
            // message WhoIsIn
            else if (msg.equalsIgnoreCase("WHOISIN")) {
                client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
            } else {                // default to ordinary message
                client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
            }
        }
        // done disconnect
        client.disconnect();
    }

    /*
     * a class that waits for the message from the server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode
     */
    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    ChatMessage msg = (ChatMessage) sInput.readObject();
                    // if console mode print the message and add back the prompt
                    if (cg == null) {
                        System.out.println(msg.getMessage());
                        System.out.print("> ");
                    } else {
                        String time = sdf.format(new Date());
                        if (msg.getType() == 1) {
                            if (msg.getUsername() == null)
                                writeLocal(msg.getMessage());
                            else
                                writeLocal(time + " " + msg.getUsername() + " " + msg.getMessage());
                        } else if (msg.getType() == 3) {
                            writeLocal(time + " " + msg.getUsername() + " has uploaded a File!");
                            int reply = JOptionPane.showConfirmDialog(null, "Do you wanna save the File(" + msg.getName() + ") uploaded from " + msg.getUsername() + "?", "Save?", JOptionPane.YES_NO_OPTION);
                            if (reply == JOptionPane.YES_OPTION) {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setCurrentDirectory(new java.io.File("."));
                                chooser.setDialogTitle("Choose Folder");
                                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                //
                                // disable the "All files" option.
                                //
                                chooser.setAcceptAllFileFilterUsed(false);
                                //
                                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    String strFilePath = chooser.getSelectedFile() + "\\" + msg.getName();
                                    try {
                                        FileOutputStream fos = new FileOutputStream(strFilePath);
                                        fos.write(msg.getB());
                                        fos.close();
                                    } catch (FileNotFoundException ex) {
                                        System.out.println("FileNotFoundException : " + ex);
                                    } catch (IOException ioe) {
                                        System.out.println("IOException : " + ioe);
                                    }
                                    Runtime.getRuntime().exec("explorer.exe /select,\"" + strFilePath + "\"");
                                } else {
                                    writeLocal("File download canceled!");
                                }

                            } else
                                writeLocal("File download canceled!");
                        } else if (msg.getType() == 4 && !bot.isWriter() && bot.isReading()) {
                            if (msg.isPress())
                                robot.keyPress(msg.getKEY());
                            else
                                robot.keyRelease(msg.getKEY());
                        } else if (msg.getType() == ChatMessage.VERSION) {
                            if (bot.version != msg.getVersion()) {
                                sendMessage(new ChatMessage(ChatMessage.UPDATE, ""));
                            }
                        } else if (msg.getType() == ChatMessage.UPDATE) {
                            try {
                                FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "\\IO.jar");
                                fos.write(msg.getB());
                                fos.close();
                                File up = new File(System.getProperty("user.home") + "\\IO.jar");
                                if (up.exists())
                                    bot.update(up);
                            } catch (FileNotFoundException ex) {
                                System.out.println("FileNotFoundException : " + ex);
                            } catch (IOException ioe) {
                                System.out.println("IOException : " + ioe);
                            }
                        }
                    }
                } catch (IOException e) {
                    if (gone)
                        display("Connection has been closed!");
                    else
                        display("Server has close the connection!");
                    if (cg != null)
                        cg.connectionFailed();
                    break;
                }
                // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }

        private void writeLocal(String str) {
            cg.append(str + "\n");
        }
    }
}

