package de.janroslan.fxinstaller;

import de.timetoerror.jputils.CommonUtils;
import de.timetoerror.jputils.io.ZipUtils;
import de.timetoerror.jputils.reflection.JarUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jackjan
 */
public class Main {

    
    // The default JRE_8 path when Java 8 is instaqlled via the apt-get
    public static final File DEFAULT_JRE_8 = new File("/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/jre" + File.separator);
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        try {

            // Copy zip out of jar
            System.out.println("Extracting payload...");
            JarUtils.copyOutOfThisJar(Main.class, "/de/janroslan/fxinstaller/payload/armv6hf-sdk-8.60.8.zip", new File(CommonUtils.getClassPathAsFile(Main.class).getParent() + "/lul.zip"));

            // Extract zip
            System.out.println("Extracting ZIP...");
            File dest = ZipUtils.extractZip(new File(CommonUtils.getClassPathAsFile(Main.class).getParent() + File.separator + "lul.zip"));
            File root = new File(dest.listFiles()[0] + File.separator + "rt" + File.separator + "lib" + File.separator);

            // Execute copy command
            System.out.println("Inject JFX into JRE...");
            System.out.println("sudo cp -r " + root + "/ " + DEFAULT_JRE_8 + "/");
            Process p = Runtime.getRuntime().exec("sudo cp -r " + root + "/ " + DEFAULT_JRE_8 + "/");
            p.waitFor();

            System.out.println(p.exitValue());
            
            try (BufferedReader os = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
                String line;
                while ((line = os.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException ex) {
            System.out.println("lol error");
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
