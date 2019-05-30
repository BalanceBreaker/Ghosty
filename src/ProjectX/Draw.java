package ProjectX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.awt.Toolkit.getDefaultToolkit;


public class Draw extends JFrame {

    public static boolean active = false;
    static JFrame f1 = null;
    static Window f2 = null;
    private static int x = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;

    public static void screenfucker() {
        if (!active) {
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


    public static void walk() {
        if (!active) {
            active = true;
            f1 = new JFrame();
            f1.addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent arg0) {
                    frame__windowStateChanged(arg0);
                }
            });
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

    public static void unclickable() {
        if (!active) {
            active = true;
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] screens = ge.getScreenDevices();
            Rectangle allScreenBounds = new Rectangle();
            for (GraphicsDevice screen : screens) {
                Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
                allScreenBounds.width += screenBounds.width;
                allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
            }
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                System.out.println(e);
            }
            BufferedImage image = robot.createScreenCapture(allScreenBounds);
            Dimension dim = getDefaultToolkit().getScreenSize();


            f2 = new Window(null) {
                void deadPixel(Graphics2D g2) {
                    //Random rand = new Random();
                    g2.setColor(new Color(0, 0, 0, 0));
                    g2.fillRect(0, 0, 50000000, 500000000);
                    //g2.fillRect(rand.nextInt((GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width - 50 - 50) + 1) + 50, rand.nextInt((GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height - 50 - 50) + 1) + 50,5,5);
                }

                @Override
                public void paint(Graphics g) {
                    final Font font = getFont().deriveFont(20f);
                    g.setFont(font);
                    g.setColor(Color.WHITE);
                    //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawImage(image, 0, 0, null);
                    //Bildschirm unbrauchbar machen
                    deadPixel(g2);
                    //  if (true)
                    //    return;

                }

                @Override
                public void update(Graphics g) {
                    paint(g);
                }
            };

            //f1.setDefaultCloseOperation(0);

            f2.setSize(dim);

            f2.setLocation(0, 0);

            // f1.setUndecorated(true);


            //f1.setBackground(new Color(0, 255, 0, 0));

            //f1.setContentPane(new ContentPane());
            //f1.getContentPane().setBackground(Color.BLACK);
            f2.setLayout(new BorderLayout());
            //f1.add(label2);
            f2.setAlwaysOnTop(true);
            f2.setVisible(true);

        }
    }

    public static void scare() {
        if (!active) {
            active = true;

            Dimension dim = getDefaultToolkit().getScreenSize();

            f1 = new JFrame();
            f1.addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent arg0) {
                    frame__windowStateChanged(arg0);
                }
            });
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

    public static void blue() {
        System.out.println("OK!");
        if (!active) {
            active = true;
            System.out.println("Hee");
            Dimension dim = getDefaultToolkit().getScreenSize();
            System.out.println("Bluescreeeen");


            try {
                f2 = new Window(null) {

                    BufferedImage image = ImageIO.read(Draw.class.getResource("/Bilder/blue.png"));

                    @Override
                    public void paint(Graphics g) {
                        final Font font = getFont().deriveFont(20f);
                        g.setFont(font);
                        g.setColor(Color.WHITE);
                        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        Graphics2D g2 = (Graphics2D) g;
                        //Bildschirm unbrauchbar machen
                        // deadPixel(g2);
                        //  if (true)
                        //    return;
                        g2.drawImage(image, 0, 0, null);
                    }

                    @Override
                    public void update(Graphics g) {
                        paint(g);
                    }
                };
            } catch (IOException e) {
                System.out.println(e);
            }
            //f1.setDefaultCloseOperation(0);

            f2.setSize(dim);

            f2.setLocation(0, 0);

            // f1.setUndecorated(true);


            //f1.setBackground(new Color(0, 255, 0, 0));

            //f1.setContentPane(new ContentPane());
            //f1.getContentPane().setBackground(Color.BLACK);
            f2.setLayout(new BorderLayout());
            //f1.add(label2);
            f2.setAlwaysOnTop(true);
            f2.setVisible(true);

        }
    }

    public static void black() {
        if (!active) {
            active = true;
            Dimension dim = getDefaultToolkit().getScreenSize();
            try {
                f2 = new Window(null) {

                    BufferedImage image = ImageIO.read(Draw.class.getResource("/Bilder/blue.png"));

                    @Override
                    public void paint(Graphics g) {
                        final Font font = getFont().deriveFont(20f);
                        g.setFont(font);
                        g.setColor(Color.BLACK);
                        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        Graphics2D g2 = (Graphics2D) g;
                        //Bildschirm unbrauchbar machen
                        // deadPixel(g2);
                        //  if (true)
                        //    return;
                        g2.drawImage(image, 0, 0, null);
                    }

                    @Override
                    public void update(Graphics g) {
                        paint(g);
                    }
                };
            } catch (IOException e) {
                System.out.println(e);
            }
            //f1.setDefaultCloseOperation(0);

            f2.setSize(dim);

            f2.setLocation(0, 0);

            // f1.setUndecorated(true);


            //f1.setBackground(new Color(0, 255, 0, 0));

            //f1.setContentPane(new ContentPane());
            //f1.getContentPane().setBackground(Color.BLACK);
            f2.setLayout(new BorderLayout());
            //f1.add(label2);
            f2.setAlwaysOnTop(true);
            f2.setVisible(true);

        }
    }

    public static void dog() {
        if (!active) {
            active = true;

            Dimension dim = getDefaultToolkit().getScreenSize();

            f1 = new JFrame();
            f1.addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent arg0) {
                    frame__windowStateChanged(arg0);
                }
            });
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

    public static void stop() {
        //f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));
        if (f1 != null)
            f1.dispose();
        if (f2 != null)
            f2.dispose();
        active = false;
    }

    public static void frame__windowStateChanged(WindowEvent e) {
        // minimized
        if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
            System.out.println("minimized!");
            f1.setVisible(true);
            f1.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f1.requestFocus();
            f1.setAlwaysOnTop(true);
        }
        // maximized
        else if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
            //_print("maximized");
        }
    }

}

