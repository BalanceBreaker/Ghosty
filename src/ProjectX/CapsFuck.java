package ProjectX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Random;

public class CapsFuck implements Runnable {
    boolean running = true;

    boolean allfuck = false;

    private boolean capsfuck = false;
    private boolean windowsfucker = false;
    boolean screenfucker = false;
    boolean mousefucker = false;
    int color = 0;
    int green = 100;
    int red = 200;
    int blue = 100;
    boolean gch = false;
    boolean rch = false;
    boolean bch = false;
    int x = 50;
    JFrame jf;
    static JFrame f1 = new JFrame();

    public void run() {
        while (running) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (capsfuck || allfuck) {
                Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, !Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK));
            }
            if (windowsfucker || allfuck) {
                try {
                    Robot r = new Robot();
                    r.keyPress(KeyEvent.VK_WINDOWS);
                    r.keyRelease(KeyEvent.VK_WINDOWS);
                } catch (Exception e) {
                }
            }

            if (mousefucker || allfuck) {
                try {
                    Robot r = new Robot();
                    Random rand = new Random();
                    r.mouseMove(rand.nextInt((GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width - 50 - 50) + 1) + 50, rand.nextInt((GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height - 50 - 50) + 1) + 50);
                } catch (Exception e) {
                }
            }
            if (screenfucker || allfuck) {
               /* if (color < 7)
                    color ++;
                else
                    color = 0;
                switch (color){
                    case 0:
                        jf.getContentPane().setBackground(new Color(0,255,0));
                        break;
                    case 1:
                        jf.getContentPane().setBackground(new Color(255,255,0));
                        break;
                    case 2:
                        jf.getContentPane().setBackground(new Color(255,0,0));
                        break;
                    case 3:
                        jf.getContentPane().setBackground(new Color(255,0,255));
                        break;
                    case 4:
                        jf.getContentPane().setBackground(new Color(0,0,255));
                        break;
                    case 5:
                        jf.getContentPane().setBackground(new Color(0,255,255));
                        break;
                    case 6:
                        jf.getContentPane().setBackground(new Color(0,255,0));
                        break;
                    case 7:
                        jf.getContentPane().setBackground(new Color(0,0,0));
                        break;
                }
                */

                jf.getContentPane().setBackground(new Color(red, green, blue));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
                if (red > 10 && !rch)
                    red -= 10;
                else
                    rch = true;
                if (blue < 245 && !bch)
                    blue += 10;
                else
                    bch = true;
                if (green < 245 && !gch)
                    green += 10;
                else
                    gch = true;
                if (red < 245 && rch)
                    red += 10;
                else
                    rch = false;
                if (blue > 10 && bch)
                    blue -= 10;
                else
                    bch = false;
                if (green > 10 && gch)
                    green -= 10;
                else
                    gch = false;

            }
        }
    }


    public void setCapsfuck(boolean capsfuck) {
        this.capsfuck = capsfuck;
    }

    public boolean isCapsfuck() {
        return capsfuck;
    }

    public boolean isWindowsfucker() {
        return windowsfucker;
    }

    public void setWindowsfucker(boolean windowsfucker) {
        this.windowsfucker = windowsfucker;
    }

    public boolean isAllfuck() {
        return allfuck;
    }

    public void setAllfuck(boolean allfuck) {
        this.allfuck = allfuck;
        if (this.allfuck) {
            jf = new JFrame();
            jf.setSize(1650, 1080);
            jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
            jf.getContentPane().setBackground(new Color(0, 255, 0));
            jf.setUndecorated(true);
            jf.setVisible(true);
            jf.setAlwaysOnTop(true);
        } else
            jf.dispatchEvent(new WindowEvent(jf, WindowEvent.WINDOW_CLOSING));
    }

    public boolean isScreenfucker() {
        return screenfucker;
    }

    public void setScreenfucker(boolean screenfucker) {
        this.screenfucker = screenfucker;
        if (this.screenfucker) {
            jf = new JFrame();
            jf.setSize(1650, 1080);
            jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
            jf.getContentPane().setBackground(new Color(0, 255, 0));
            jf.setUndecorated(true);
            jf.setVisible(true);
            jf.setAlwaysOnTop(true);
        } else
            jf.dispatchEvent(new WindowEvent(jf, WindowEvent.WINDOW_CLOSING));
    }


    public boolean isMousefucker() {
        return mousefucker;
    }

    public void setMousefucker(boolean mousefucker) {
        this.mousefucker = mousefucker;
    }
}

