package ProjectX;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by Alex on 01.09.2016.
 */
public class CDUtils {
    private CDUtils() {  }

    public static void open(){
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        for(File path:paths)
        {
            if(fsv.getSystemTypeDescription(path).contains("Laufwerk") || fsv.getSystemTypeDescription(path).toLowerCase().contains("drive"))
                CDUtils.open(path.toString());
        }
    }

    public static void close(){
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        for(File path:paths)
        {
            if(fsv.getSystemTypeDescription(path).contains("Laufwerk") || fsv.getSystemTypeDescription(path).toLowerCase().contains("drive"))
                CDUtils.close(path.toString());
        }
    }
    public static void open(String drive) {
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set wmp = CreateObject(\"WMPlayer.OCX\") \n"
                    + "Set cd = wmp.cdromCollection.getByDriveSpecifier(\""
                    + drive + "\") \n"
                    + "cd.Eject";
            fw.write(vbs);
            fw.close();
            Runtime.getRuntime().exec("wscript " + file.getPath()).waitFor();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(String drive) {
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            // to close a CD, we need eject two times!
            String vbs = "Set wmp = CreateObject(\"WMPlayer.OCX\") \n"
                    + "Set cd = wmp.cdromCollection.getByDriveSpecifier(\""
                    + drive + "\") \n"
                    + "cd.Eject \n "
                    + "cd.Eject ";
            fw.write(vbs);
            fw.close();
            Runtime.getRuntime().exec("wscript " + file.getPath()).waitFor();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

 /*   public static void main(String[] args){
        javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
                        null, "Press OK to open CD", "Fun.CDUtils",
                javax.swing.JOptionPane.DEFAULT_OPTION);
        Fun.CDUtils.open("E:\\");
        javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
                        null, "Press OK to close CD", "Fun.CDUtils",
                javax.swing.JOptionPane.DEFAULT_OPTION);
        Fun.CDUtils.close("E:\\");
    }
    */
}
