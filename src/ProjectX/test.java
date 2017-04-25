package ProjectX;

import javax.swing.*;

/**
 * Created by Alexander Ressl on 24.04.2017 14:25.
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
       /* Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
        Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);

        //System.out.println(batteryStatus); // Shows result of toString() method.
        System.out.println(batteryStatus);
        Thread.sleep(120000);
        Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
        System.out.println(batteryStatus);
        */
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Please enter your new password:");
        JPasswordField pass = new JPasswordField(10);
        JPasswordField pass1 = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        panel.add(pass1);
        panel.grabFocus();
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Passwort",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, pass);
        System.out.println(option);
    }
}
