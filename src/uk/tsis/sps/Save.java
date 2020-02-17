package uk.tsis.sps;

import uk.tsis.sps.crypto.Aes;
import java.io.*;

public class Save {

    public static void savePassword(String label, String password) throws IOException {
        File file = SecurePasswordSaver.savedPasswordsFile;
        String k = Key.getKey();
        String encryptedPass = Aes.encrypt(password, k);
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        writer.write(label + ":" + encryptedPass + "\n");                         // label:pass
        writer.flush();
        writer.close();
        System.out.println("[SPS] Password Saved.");System.out.println(" ");
    }

}
