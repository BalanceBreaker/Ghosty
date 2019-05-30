package ProjectX;

public class LowRam implements Runnable {
    long last = 0;
    long time = 0;
    long lastc = 0;
    boolean running = true;
    boolean bat = false;
    boolean senden = false;
    Bot bot;
    Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();

    public LowRam(long last, Bot admin) {
        this.last = last;
        lastc = last;
        this.bot = admin;
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(5000);
                long elapsedTimeMillis = System.currentTimeMillis() - last;
                float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
                if(bot.changedC)
                    bot.writeRealConf();
                if (bot.changed)
                    bot.writeConf();
                if (System.currentTimeMillis() - lastc > 20000) {
                    bot.setSilent(true);
                    Thread.sleep(1000);
                    bot.retry();
                }
                lastc = System.currentTimeMillis();
                if (elapsedTimeMin - time > 60) {
                    time = (long) elapsedTimeMin;
                    Runtime.getRuntime().gc();
                }
                Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
                if (!batteryStatus.getBatteryLifePercent().startsWith("U")) {
                    int proz = Integer.parseInt(batteryStatus.getBatteryLifePercent().replace("%", ""));
                    if (proz <= 20 && !bat) {
                        bot.tts("Warning! Low battery detected. Less than " + batteryStatus.getBatteryLifePercent() + " left. I recommend recharging your device.", false);
                        bot.sendNachricht("Warning!\nBattery level is low! (" + batteryStatus.getBatteryLifePercent() + ")");
                        bat = true;
                    }
                    if (proz >= 21 && bat)
                        bat = false;
                }
            } catch (InterruptedException e) {
                bot.sendNachrichtAdmin(e.toString());
            }
        }
    }

    public void setRunning(boolean running) {
        this.senden = running;
    }

    public boolean isRunning() {
        return senden;
    }
}
