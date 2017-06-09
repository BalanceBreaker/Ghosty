package ProjectX;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;


public class Draw extends JFrame {

    public static boolean active = false;
    private static JFrame f1;
    private static int x = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;

    public static void screenfucker() {
        if(!active) {
            active = true;
            Dimension dim = getDefaultToolkit().getScreenSize();

            f1 = new JFrame();
            f1.setDefaultCloseOperation(0);

            f1.setSize(dim);

            f1.setLocation(0, 0);
            f1.setSize(dim);
// Set undecorated
            f1.setUndecorated(true);
// Apply a transparent color to the background
// This is ALL important, without this, it won't work!
            f1.setBackground(new Color(0, 255, 0, 0));

// This is where we get sneaky, basically where going to
// supply our own content pane that does some special painting
// for us
            f1.setContentPane(new ContentPane());
            f1.getContentPane().setBackground(Color.BLACK);
            f1.setLayout(new FlowLayout());
       /* try {
            BufferedImage myPicture = ImageIO.read(new File("Bilder\\1.gif"));
            JLabel tt = new JLabel(new ImageIcon(myPicture));
            f1.add(tt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
            ImageIcon imageIcon = new ImageIcon(Draw.class.getResource("/Bilder/2.gif"));
            ImageIcon imageIcon1 = new ImageIcon(Draw.class.getResource("/Bilder/4.gif"));
            ImageIcon imageIcon3 = new ImageIcon(Draw.class.getResource("/Bilder/6.gif"));
            ImageIcon imageIcon4 = new ImageIcon(Draw.class.getResource("/Bilder/7.gif"));
            ImageIcon imageIcon5 = new ImageIcon(Draw.class.getResource("/Bilder/8.gif"));
            ImageIcon imageIcon6 = new ImageIcon(Draw.class.getResource("/Bilder/9.gif"));
            JLabel label = new JLabel(imageIcon);
            JLabel label1 = new JLabel(imageIcon1);
            JLabel label3 = new JLabel(imageIcon3);
            JLabel label4 = new JLabel(imageIcon4);
            JLabel label5 = new JLabel(imageIcon5);
            JLabel label6 = new JLabel(imageIcon6);
            f1.add(label);
            f1.add(label1);
            f1.add(label3);
            f1.add(label4);
            f1.add(label5);
            f1.add(label6);
            f1.setAlwaysOnTop(true);
            f1.setVisible(true);
        }
    }


    public static void walk(){
        if(!active) {
            active = true;
            f1 = new JFrame();
            f1.setDefaultCloseOperation(0);
            f1.setSize(500, 500);
            f1.setUndecorated(true);
            f1.setLocation(x, 100);
            f1.setBackground(new Color(0, 255, 0, 0));

            f1.setContentPane(new ContentPane());
            f1.getContentPane().setBackground(Color.BLACK);
            f1.setLayout(new BorderLayout());
            ImageIcon imageIcon2 = new ImageIcon(Draw.class.getResource("/Bilder/7.gif"));
            JLabel label2 = new JLabel(imageIcon2);
            f1.add(label2);
            f1.setAlwaysOnTop(true);
            f1.setVisible(true);
            while (x > -400) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x -= 1;
                f1.setLocation(x, 100);
            }
            stop();
        }
    }

    public static void scare(){
        if(!active) {
            active = true;

            Dimension dim = getDefaultToolkit().getScreenSize();

            f1 = new JFrame();
            f1.setDefaultCloseOperation(0);
            f1.setSize(dim);

            f1.setLocation(0, 0);
            f1.setSize(dim);

            f1.setUndecorated(true);
            f1.setAutoRequestFocus(true);
            f1.setBackground(new Color(0, 255, 0, 0));

            f1.setContentPane(new ContentPane());
            f1.getContentPane().setBackground(Color.BLACK);
            f1.setLayout(new BorderLayout());
            ImageIcon imageIcon2 = new ImageIcon(Draw.class.getResource("/Bilder/scary.gif"));
            JLabel label2 = new JLabel(imageIcon2);
            f1.add(label2);
            f1.setAlwaysOnTop(true);
            f1.setVisible(true);
        }
    }
    public static void blue(){
        if(!active) {
            active = true;

            Dimension dim = getDefaultToolkit().getScreenSize();

            f1 = new JFrame();
            f1.setDefaultCloseOperation(0);
            f1.setSize(dim);

            f1.setLocation(0, 0);
            f1.setSize(dim);

            f1.setUndecorated(true);

            f1.setBackground(new Color(0, 255, 0, 0));

            f1.setContentPane(new ContentPane());
            f1.getContentPane().setBackground(Color.BLACK);
            f1.setLayout(new BorderLayout());
            ImageIcon imageIcon2 = new ImageIcon(Draw.class.getResource("/Bilder/blue.png"));
            JLabel label2 = new JLabel(imageIcon2);
            f1.add(label2);
            f1.setAlwaysOnTop(true);
            f1.setVisible(true);
        }
    }
    public static void black(){
        if(!active){
            active = true;
            Dimension dim = getDefaultToolkit().getScreenSize();
            f1 = new JFrame();
            f1.setDefaultCloseOperation(0);
            f1.setSize(dim);
            f1.setLocation(0, 0);
            f1.setSize(dim);
            f1.setUndecorated(true);
            f1.setBackground(new Color(0, 0, 0, 255));
            f1.setContentPane(new ContentPane());
            f1.getContentPane().setBackground(Color.BLACK);
            f1.setLayout(new BorderLayout());
            f1.setAlwaysOnTop(true);
            f1.setVisible(true);

        }
    }
    public static void dog(){
        if(!active) {
            active = true;

            Dimension dim = getDefaultToolkit().getScreenSize();

            f1 = new JFrame();
            f1.setDefaultCloseOperation(0);
            f1.setSize(dim);

            f1.setLocation(0, 0);
            f1.setSize(dim);

            f1.setUndecorated(true);

            f1.setBackground(new Color(0, 255, 0, 0));

            f1.setContentPane(new ContentPane());
            f1.getContentPane().setBackground(Color.BLACK);
            f1.setLayout(new BorderLayout());
            ImageIcon imageIcon2 = new ImageIcon(Draw.class.getResource("/Bilder/6.gif"));
            JLabel label2 = new JLabel(imageIcon2);
            f1.add(label2);
            f1.setAlwaysOnTop(true);
            f1.setVisible(true);
        }
    }

    public static void stop(){
        //f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));7
        f1.dispose();
        active = false;
    }
}

