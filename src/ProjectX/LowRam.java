package ProjectX;

public class LowRam implements Runnable {
    long last = 0;
    long time = 0;
    long lastc = 0;
    boolean running = true;
    boolean bat = false;
    boolean senden = false;
    Bot telegramBotsApi;
    Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();

    public LowRam(long last, Bot admin) {
        this.last = last;
        lastc = last;
        this.telegramBotsApi = admin;
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(60000);
                long elapsedTimeMillis = System.currentTimeMillis() - last;
                float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
                if (System.currentTimeMillis() - lastc > 200000) {
                    telegramBotsApi.setSilent(true);
                    telegramBotsApi.retry();
                }
                lastc = System.currentTimeMillis();
                if (elapsedTimeMin - time > 60) {
                    time = (long) elapsedTimeMin;
                    Runtime.getRuntime().gc();
                }
                Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
                if (!batteryStatus.getBatteryLifePercent().startsWith("U")) {
                    int proz = Integer.parseInt(batteryStatus.getBatteryLifePercent().replace("%", ""));
                    if (proz <= 25 && !bat) {
                        telegramBotsApi.sendNachricht("Warning!\nBattery level is low! (" + batteryStatus.getBatteryLifePercent() + ")");
                        bat = true;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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
