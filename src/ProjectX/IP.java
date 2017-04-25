package ProjectX;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class IP {
    public static String getIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            return in.readLine(); //you get the IP as a String
        }catch(Exception e){
            return "ERR";
        }
    }
}
