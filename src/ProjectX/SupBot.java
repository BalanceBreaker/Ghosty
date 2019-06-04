package ProjectX;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Alexander Ressl on 11.04.2017 10:07.
 */
public class SupBot extends TelegramLongPollingBot {
    public SupBot(String name, boolean autoupdate) {
        String send = name + "\n";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            send += addr.getHostName();
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        sendNachricht(send, autoupdate);
    }

    public SupBot(String nachricht){
        String send = nachricht + "\n";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            send += addr.getHostName();
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        sendNachricht(send);
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "Support";
    }

    @Override
    public String getBotToken() {
        return "328725309:AAFvkfv6DmtThx-3wVNrHmxVq5hTEqap4qc";
    }

    void sendNachricht(String nachricht, boolean autoupdate) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId("162922263");
        TestNachricht.setText("@" + nachricht.replaceAll("(?=[]\\[+|`'{}^_~*\\\\])", "\\\\") + "\nAutoupdate: " + autoupdate);
        try {
            execute(TestNachricht);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendNachricht(String nachricht) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId("162922263");
        TestNachricht.setText(nachricht.replaceAll("(?=[]\\[+|`'{}^_~*\\\\])", "\\\\"));
        try {
            execute(TestNachricht);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
