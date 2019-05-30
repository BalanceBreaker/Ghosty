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
                breach();
            }

        }
    }

    public void breach() {
        running = false;
        switch (wasmachen) {
            default:
            case 0:
                bot.sendNachricht("Thread detected!\nAlarm.");
                bot.tts("Security Breach detected! You have been classified as an intruder! Step away from this device immediately or further actions will be taken. Activating Security mode Omega 0267" ,false);
                if (bot.getConfig().isCamera())
                    bot.takepic(true);
                break;
            case 1:
                bot.sendNachricht("Thread detected! PC locked!");
                try {
                    Runtime.getRuntime().exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
                    bot.tts("Security Breach detected! You have been classified as an intruder! Step away from this device immediately or further actions will be taken. Activating Security mode alpha 0183",false);
                    if (bot.getConfig().isCamera())
                        bot.takepic(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                bot.sendNachricht("Benutzung erkannt! PC sleept!");
                try {
                    Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
                    bot.takepic(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
