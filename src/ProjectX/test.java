package ProjectX;


import Chat.ChatMessage;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Alexander Ressl on 24.04.2017 14:25.
 */
public class test {

    static boolean run = true;

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

    public static void main(String[] args) throws Exception {
       /* HashMap<String, Long> blub = new HashMap<>();
        blub.put("Hey", 5L);
        blub.put("Huhu", 6L);
        blub.put("AA", 1L);
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
