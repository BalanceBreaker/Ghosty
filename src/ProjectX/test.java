package ProjectX;


import Chat.ChatMessage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.transform.Affine;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.awt.Toolkit.getDefaultToolkit;

/**
 * Created by Alexander Ressl on 24.04.2017 14:25.
 */
public class test {

    static boolean run = true;
    private static TrayIcon trayIcon;

    public static LinkedHashMap<String, Long> sortHashMapByValues(
            HashMap<String, Long> passedMap) {
        java.util.List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        java.util.List<Long> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Long> sortedMap =
                new LinkedHashMap<>();

        Iterator<Long> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Long val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Long comp1 = passedMap.get(key);
                if (comp1.equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    private void Draw(BufferedImage image) {
        Window w = new Window(null) {
            @Override
            public void paint(Graphics g) {
                final Font font = getFont().deriveFont(20f);
                g.setFont(font);
                g.setColor(Color.WHITE);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Graphics2D g2 = (Graphics2D) g;
                //  g2.drawImage(op.filter(image, null), 0, 0, null);
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }
        };
        w.setAlwaysOnTop(true);
        w.setBounds(w.getGraphicsConfiguration().getBounds());
        w.setBackground(new Color(0, true));
        w.setVisible(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        w.dispose();
    }

    private static JFrame f1;

    public static void main(String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        Rectangle allScreenBounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
            allScreenBounds.width += screenBounds.width;
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
        }
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(allScreenBounds);


// Drawing the rotated image at the required drawing locations
        //  ImageIcon imageIcon2 = new ImageIcon(op.filter(image, null));

        Dimension dim = getDefaultToolkit().getScreenSize();
        Window w = new Window(null) {
            boolean positiv = true;

            void flip(Graphics2D g2, int degree, BufferedImage image) {
                double rotationRequired = Math.toRadians(degree);
                double locationX = image.getWidth() / 2;
                double locationY = image.getHeight() / 2;
                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransform move;
                if (positiv) {
                    // robot.mouseMove(degree * 3, (int) (degree * 0.5));
                    move = AffineTransform.getTranslateInstance(degree * 5, -degree);
                } else {
                    //   robot.mouseMove(degree * 2, (int) (degree * 1.5));
                    move = AffineTransform.getTranslateInstance(-degree * 5, degree);
                }
                positiv = !positiv;
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                AffineTransformOp moveOp = new AffineTransformOp(move, AffineTransformOp.TYPE_BILINEAR);
                // g2.clearRect(0,0,100000,100000);
                g2.drawImage(moveOp.filter(op.filter(image, null), null), 0, 0, null);

                //g2.drawImage(moveOp.filter(image,null), 0, 0, null);
            }

            void shake(Graphics2D g2, int degree, BufferedImage image) {
                AffineTransform move;
                if (positiv)
                    move = AffineTransform.getTranslateInstance(degree * 5, -degree);
                else
                    move = AffineTransform.getTranslateInstance(-degree * 5, degree);
                positiv = !positiv;
                AffineTransformOp moveOp = new AffineTransformOp(move, AffineTransformOp.TYPE_BILINEAR);
                g2.drawImage(moveOp.filter(image, null), 0, 0, null);
            }

            void deadPixel(Graphics2D g2) {
                //Random rand = new Random();
                g2.setColor(new Color(0, 100, 0, 100));
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
                //Bildschirm unbrauchbar machen
                // deadPixel(g2);
                //  if (true)
                //    return;
               g2.drawImage(image,0,0,null);

                /*for (int i = 0; i <= 360; i += 2) {
                    flip(g2, i, image);
                }

                BufferedImage image0 = robot.createScreenCapture(allScreenBounds);
                for (int i = 0; i <= 360; i += 5) {
                    flip(g2, i, image0);
                }
                BufferedImage image1 = robot.createScreenCapture(allScreenBounds);
                for (int i = 0; i <= 360; i += 10) {

                    flip(g2, i, image1);

                }
                BufferedImage image2 = robot.createScreenCapture(allScreenBounds);
                for (int i = 0; i <= 360; i += 30) {

                    flip(g2, i, image2);
                }
                for (int i = 360; i >= 0; i -= 30) {
                    flip(g2, i, image2);
                }
                for (int i = 360; i >= 0; i -= 10) {
                    flip(g2, i, image1);
                }
                for (int i = 360; i >= 0; i -= 5) {
                    flip(g2, i, image0);
                }
                for (int i = 360; i >= 0; i -= 2) {
                    flip(g2, i, image);
                }

                //ANDERES


                for (int i = 0; i <= 360; i += 2) {
                    shake(g2, i, image);
                }
                image0 = robot.createScreenCapture(allScreenBounds);
                for (int i = 0; i <= 360; i += 5) {
                    shake(g2, i, image0);
                }
                image1 = robot.createScreenCapture(allScreenBounds);
                for (int i = 0; i <= 360; i += 10) {
                    shake(g2, i, image1);
                }
                image2 = robot.createScreenCapture(allScreenBounds);
                for (int i = 0; i <= 360; i += 30) {
                    shake(g2, i, image2);
                }
                for (int i = 360; i >= 0; i -= 30) {
                    shake(g2, i, image2);
                }
                for (int i = 360; i >= 0; i -= 10) {
                    shake(g2, i, image1);
                }
                for (int i = 360; i >= 0; i -= 5) {
                    shake(g2, i, image0);
                }
                for (int i = 360; i >= 0; i -= 2) {
                    shake(g2, i, image);
                }

*/
                deadPixel(g2);
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }
        };
        w.setAlwaysOnTop(true);

        w.setBounds(w.getGraphicsConfiguration().getBounds());
        //w.setBackground(new Color(0, true));
        //JLabel label2 = new JLabel(imageIcon2);
        //w.add(label2);
        w.setAutoRequestFocus(true);
       /* w.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                w.setVisible(false);
                System.out.println(e.getButton());
                try {
                    Robot klick = new Robot();
                    if (e.getButton() == 1) {
                        klick.mousePress(MouseEvent.BUTTON1_MASK);
                        klick.mouseRelease(MouseEvent.BUTTON1_MASK);
                    } else if (e.getButton() == 3) {
                        klick.mousePress(MouseEvent.BUTTON3_MASK);
                        klick.mouseRelease(MouseEvent.BUTTON3_MASK);
                    }
                } catch (Exception e1) {
                    System.out.println(e1);
                }
                System.out.println("PRES");
                w.setVisible(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        */
        w.requestFocus();
        w.setLocation(0, 0);
        w.setVisible(true);
        System.out.println("Scihtbar");
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        w.dispose();
        if(true)
            return;
       /* f1 = new JFrame();
        f1.setDefaultCloseOperation(0);
        f1.setSize(dim);

        f1.setLocation(0, 0);
        f1.setSize(dim);

        f1.setUndecorated(true);

        f1.setBackground(new Color(0, 255, 0, 0));

        f1.setContentPane(new ContentPane());
        f1.getContentPane().setBackground(Color.BLACK);
        f1.setLayout(new BorderLayout());
        JLabel label2 = new JLabel(imageIcon2);
        f1.add(label2);
        f1.setAlwaysOnTop(true);
        f1.setVisible(true);
        */
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                System.out.println(event.getKeyChar());

                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
                    run = false;

                }
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_LWIN) {
                    try {
                        Robot carl = new Robot();
                        //carl.keyPress(KeyEvent.VK_WINDOWS);
                        carl.keyRelease(KeyEvent.VK_WINDOWS);
                    } catch (Exception e) {
                        System.out.println("oh noo");
                    }
                }

            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                //PressKey.client.sendMessage(new ChatMessage(4, event.getVirtualKeyCode(),false));
            }
        });


        try {
            while (run) Thread.sleep(128);
        } catch (InterruptedException e) { /* nothing to do here */ } finally {
            keyboardHook.shutdownHook();
        }


        if (true)
            return;
        if (true)
            return;

        if (true)
            return;
        if (!SystemTray.isSupported()) {
            return;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        //get the systemTray of the system
        //get default toolkit
        //Toolkit toolkit = Toolkit.getDefaultToolkit();
        //get image
        //Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
        String was = "DefenderIcon.png";
        //String was = "audio.png";
        Image imagee = Toolkit.getDefaultToolkit().getImage(("D:\\Downloads\\" + was));

        //popupmenu
        PopupMenu trayPopupMenu = new PopupMenu();


        //2nd menuitem of popupmenu
        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        //setting tray icon
        trayIcon = new TrayIcon(image, "Ghosty", trayPopupMenu);
        //adjust to default size as per system recommendation
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("klick");
                    try {
                        Robot click = new Robot();
                        click.mousePress(InputEvent.BUTTON3_MASK);
                        Thread.sleep(200);
                        click.mouseRelease(InputEvent.BUTTON3_MASK);
                    } catch (Exception ek) {
                        System.out.println(ek);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("press");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("release");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exited");
            }
        });
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("lol");
            }
        });


        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
       /* HashMap<String, Long> blub = new HashMap<>();
        blub.put("Hey", 5L);
        blub.put("Huhu", 6L);
        blub.put("AA", 1L);3
        System.out.println(blub);
        blub = sortHashMapByValues(blub);
        System.out.println(blub);
        System.out.println((blub.keySet().toArray()[2]));

/*

        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown.");
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                System.out.println(event.getVirtualKeyCode());
                ChatMessage  k = new ChatMessage(4, event.getVirtualKeyCode());
                System.out.println(k.getKEY());
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE)
                    run = false;
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                System.out.println(event);
            }
        });

        try {
            while (run) Thread.sleep(128);
        } catch (InterruptedException e) { /* nothing to do here  } finally {
            keyboardHook.shutdownHook();
        }

      /*  File x = new File("C:\\Users\\areco\\Desktop\\PRO64.exe");
        System.out.println(x.length());
        byte[] b = createChecksum("C:\\Users\\areco\\Desktop\\duplicate.txt");
        Thread.sleep(5000);
        byte[] b1 = createChecksum("C:\\Users\\areco\\Desktop\\duplicate.txt");
        if (b == b1)
            System.out.println("gleich");
        else
            System.out.println("komplett anders");
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        result += "\n";
        for (int i = 0; i < b1.length; i++) {
            result += Integer.toString((b1[i] & 0xff) + 0x100, 16).substring(1);
        }
        System.out.println(result);
        //System.out.println(data[0] + " " +data[1] + " " + data [2] + " " + data[3]);

       /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt (1816);
        byte[] data = baos.toByteArray();
        System.out.println(data[0] + " " +data[1] + " " + data [2] + " " + data[3]);
        /* double vers = 0;
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect("http://balancebrek.ihostfull.com/").get();
            System.out.println(doc.getAllElements().size());
            Elements bl = doc.select("*");
            System.out.println(bl.size());
            vers = Double.parseDouble(bl.get(0).text());
        } catch (Exception e1) {
            System.out.println(e1);
        }
        if (vers == 0)
            JOptionPane.showMessageDialog(null, "Version: " + 99 + "\nNewest Version!");
        else
            JOptionPane.showMessageDialog(null, "Version: " + 99 + "\nNew Version available!\nNew Version: " + vers);
            */
    }

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }
}
