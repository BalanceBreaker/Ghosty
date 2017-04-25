package ProjectX;

import java.awt.*;
import java.io.IOException;

public class Detect implements Runnable {
    boolean running = true;
    int wasmachen = 0;
    Bot bot;

    public Detect(int wasmachen, Bot bot) {
        this.wasmachen = wasmachen;
        this.bot = bot;
    }

    public void run() {
        Point b = MouseInfo.getPointerInfo().getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();
        while (running) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Point c = MouseInfo.getPointerInfo().getLocation();
            if ((int) c.getX() != x || (int) c.getY() != y) {
                switch (wasmachen) {
                    default:
                    case 0:
                        bot.sendNachricht("Benutzung erkannt!\nAlarm.");
                        if (bot.camera)
                            bot.takepic();
                        break;
                    case 1:
                        bot.sendNachricht("Benutzung erkannt! PC wird gesperrt!");
                        try {
                            Runtime.getRuntime().exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
                            if (bot.camera)
                                bot.takepic();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        bot.sendNachricht("Benutzung erkannt! PC sleept!");
                        try {
                            Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
                            bot.takepic();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                running = false;
            }

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
