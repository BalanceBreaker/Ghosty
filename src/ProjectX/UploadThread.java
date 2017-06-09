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

/**
 * Created by Alexander Ressl on 23.05.2017 11:19.
 */
public class UploadThread implements Runnable {

    DropTargetDropEvent evt;
    Client client;
    JTextArea ta;
    public UploadThread(DropTargetDropEvent evt, Client dropTarget, JTextArea ta) {
        this.evt = evt;
        this.client = dropTarget;
        this.ta = ta;
    }

    @Override
    public void run() {
        try {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            java.util.List<File> droppedFiles = (java.util.List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles) {
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
