package ProjectX;

import Chat.ChatMessage;
import Chat.Client;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

/**
 * Created by Alexander Ressl on 23.05.2017 15:51.
 */
public class PressKey implements Runnable{
    boolean run = true;
    static Client client;
    static Bot bot;

    public PressKey(Client client,Bot bot) {
        PressKey.client = client;
        PressKey.bot = bot;
    }

    @Override
    public void run() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown.");
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                PressKey.client.sendMessage(new ChatMessage(4, event.getVirtualKeyCode(),true));
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
                    run = false;
                    bot.changeWriting();
                }
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                PressKey.client.sendMessage(new ChatMessage(4, event.getVirtualKeyCode(),false));
            }
        });

        try {
            while (run) Thread.sleep(128);
        } catch (InterruptedException e) { /* nothing to do here */ } finally {
            keyboardHook.shutdownHook();
        }
    }
}
