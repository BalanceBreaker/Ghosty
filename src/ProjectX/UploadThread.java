package ProjectX;

import Chat.ChatMessage;
import Chat.Client;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alexander Ressl on 23.05.2017 11:19.
 */
public class UploadThread implements Runnable {

    List<File> evt;
    Client client;
    JTextArea ta;
    public UploadThread(List<File> evt, Client dropTarget, JTextArea ta) {
        this.evt = evt;
        this.client = dropTarget;
        this.ta = ta;
    }

    @Override
    public void run() {
        try {
            for (File file : evt) {
                if(file.length() > 102776320){
                    ta.append("File too big!");
                    return;
                }
                byte[] b = new byte[(int) file.length()];
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    fileInputStream.read(b);
                } catch (FileNotFoundException e) {
                    ta.append("File Not Found.");
                    e.printStackTrace();
                } catch (IOException e1) {
                    ta.append("Error Reading The File.");
                    e1.printStackTrace();
                }
                client.sendMessage(new ChatMessage(ChatMessage.DATA, b, file.getName()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ta.append(ex.toString());
        }
    }
}
