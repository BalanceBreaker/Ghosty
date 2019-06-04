package ProjectX;

import com.github.sarxos.webcam.Webcam;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alexander Ressl on 02.04.2017 16:17.
 */
public class DummyBot extends TelegramLongPollingBot {
    String token;
    String name;
    int random;
    Config config;
    GetAdmin admin;

    public DummyBot(String token){
        this.token = token;
        this.name = "ConnectionTest#1";
    }
    public DummyBot(String token, String name, int random, Config config, GetAdmin admin) {
        this.token = token;
        this.name = name;
        this.random = random;
        this.config = config;
        this.admin = admin;
        try {
            config.setName(this.getMe().getUserName());
        } catch (TelegramApiException e) {
            System.out.println(e);
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage())
            return;
        if (!update.getMessage().hasText())
            return;
        String nachricht = update.getMessage().getText();

        if (nachricht.equalsIgnoreCase("/start")) {
            sendNachricht("Please enter the Code your device is showing.", update.getMessage().getChatId());
            admin.next();
        }
        if (nachricht.equalsIgnoreCase(random + "")) {
            sendNachricht("Code correct! You have been verified, take a look at your device to finish the installation.", update.getMessage().getChatId());
            config.setAdmin(update.getMessage().getChatId() + "");
            admin.stopConnection();
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    void sendNachricht(String nachricht, long chatid) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(chatid + "");
        TestNachricht.setText(nachricht);
        try {
            execute(TestNachricht);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
