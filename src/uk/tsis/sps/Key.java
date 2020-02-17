package uk.tsis.sps;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Key {

    private static boolean doesPasswordExist() throws Exception{
        List<String> s = Files.readAllLines(Paths.get(SecurePasswordSaver.encryptionKeyFile.getAbsolutePath()));
        if (s.isEmpty()){
            return false;
        }
        return true;
    }

    public static String getKey(){
        try{
            List<String> s = Files.readAllLines(Paths.get(SecurePasswordSaver.encryptionKeyFile.getAbsolutePath()));
            if (s.isEmpty()){
                System.out.println("[SPS] There is no password encryption key saved, type 'setKey' to get started....");System.out.println(" ");
                SecurePasswordSaver.get();
            }
            return s.get(0);
        }catch (Exception e){
            System.out.println("[SPS] Error: " + e.getMessage());System.out.println(" ");
            SecurePasswordSaver.get();
        }
        return null;
    }

    public static void setKey(String key) throws Exception{

        if (doesPasswordExist()){
            System.out.print("[SPS] A key already exists, are you sure you want to override it? You won't be able to decrypt any previously saved passwords without re-setting the key as the previous one if you continue. (yes/no):");
            String x = new Scanner(System.in).next();
            if (!x.equalsIgnoreCase("yes")){
                System.out.println("[SPS] Aborting...");
                SecurePasswordSaver.get();
            }
        }

        PrintWriter writer = new PrintWriter(SecurePasswordSaver.encryptionKeyFile);
        writer.write(key);
        writer.flush();
        writer.close();
        System.out.println("[SPS] Key has been set, you can now start saving passwords...");
    }

}
