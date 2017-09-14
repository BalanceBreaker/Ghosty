package ProjectX;/**
 * Created by Alexander Ressl on 02.04.2017 20:58.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadLocalRandom;

public class GetAdmin extends Application {

    static private BotSession bum;
    static private BotSession sup;
    static int randomNum = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
    static Config config;
    Label text = new Label();
    ProgressBar bar = new ProgressBar();
    FlowPane progressPane = new FlowPane(Orientation.HORIZONTAL);
    Button but = new Button("Finish");
    boolean closing = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin");
        FlowPane gesamt = new FlowPane(Orientation.VERTICAL);
        FlowPane textPane = new FlowPane(Orientation.HORIZONTAL);
        bar.setPrefWidth(200);
        text.setText("Please wait.");
        textPane.getChildren().add(text);
        progressPane.getChildren().add(bar);
        gesamt.getChildren().addAll(textPane, progressPane);
        textPane.setPadding(new Insets(10, 0, 50, 0));
        Scene scene = new Scene(gesamt, 200, 100);
        scene.getStylesheets().add("ProjectX/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        ApiContextInitializer.init();
        bar.setProgress(0.3);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            bum = telegramBotsApi.registerBot(new DummyBot(config.getToken(), config.getName(), randomNum, config, this));
        } catch (TelegramApiRequestException e) {
        }
        text.setText("Please text Ghosty now \"/start\"");
        but.setOnAction(e -> {
            closing = true;
            primaryStage.close();
            try {
                FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "\\botconf.bot");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(config);
                oos.close();
                sup = telegramBotsApi.registerBot(new SupBot(config.getName(), config.isAutoupdate()));
                sup.close();
                Thread.sleep(1000);
                try {
                    Runtime.getRuntime().exec("cmd /c attrib +s +h \"" + System.getProperty("user.home") + "\\botconf.bot" + "\"");
                } catch (IOException ek) {
                    System.out.println(ek + " HIDE");
                }
            } catch (Exception ex) {
                System.out.println(ex + " Generell");
            }
            try {
                ApiContextInitializer.init();
                Bot bot = new Bot(new String[0]);
                starten(bot);
            } catch (InterruptedException e1) {
                System.out.println(e1);
            }
        });
    }

    public static void los() {
        launch();
    }

    private static void starten(Bot bot) throws InterruptedException {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(bot);
        } catch (Exception e) {
            bot.setImage(3);
            Thread.sleep(5000);
            starten(bot);
        }
        bot.setImage(0);
    }

    public void stopConnection() {
        bum.stop();
        Platform.runLater(
                () -> {
                    bar.setProgress(1);
                    text.setText("You have been acceptet!");
                    progressPane.getChildren().remove(0);
                    progressPane.getChildren().add(but);
                }
        );
    }

    public void next() {
        Platform.runLater(
                () -> {
                    bar.setProgress(0.7);
                    text.setText("CODE: " + randomNum);
                }
        );

    }


    @Override
    public void stop() {
        if (!closing)
            System.exit(0);
    }

}
