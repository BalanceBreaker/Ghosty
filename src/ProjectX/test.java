package ProjectX;

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

/**
 * Created by Alexander Ressl on 24.04.2017 14:25.
 */
public class test {
    public static void main(String[] args) throws Exception {

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
