package ProjectX;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.swing.*;

/**
 * Created by Alexander Ressl on 24.04.2017 14:25.
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
        double vers = 0;
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
    }
}
