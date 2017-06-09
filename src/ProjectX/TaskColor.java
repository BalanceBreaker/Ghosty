package ProjectX;

/**
 * Created by Alexander Ressl on 20.04.2017 17:19.
 */
public class TaskColor implements Runnable {
    Bot bot;
    boolean running = false;
    boolean active = false;

    public TaskColor(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        running = false;
        active = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        if (!bot.updating)
            if (running)
                run();
            else {
                if (bot.isUnread())
                    bot.setImage(4);
                else
                    bot.setImage(0);
            }
        active = false;

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isActive() {
        return active;
    }
}
