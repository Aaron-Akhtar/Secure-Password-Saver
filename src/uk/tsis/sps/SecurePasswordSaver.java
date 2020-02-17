package uk.tsis.sps;

import uk.tsis.sps.crypto.Aes;
import uk.tsis.sps.crypto.Sha;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class SecurePasswordSaver {

    public static File savedPasswordsFile = new File(System.getProperty("user.home") + "/AppData/Roaming/SPS/passwords.txt");
    public static File encryptionKeyFile = new File(System.getProperty("user.home") + "/AppData/Roaming/SPS/key.txt");

    public static void main(String[] args){
        if (!savedPasswordsFile.getParentFile().isDirectory()){
            try {
                savedPasswordsFile.getParentFile().mkdir();
                savedPasswordsFile.createNewFile();
                encryptionKeyFile.createNewFile();
            }catch (Exception e){
                System.out.println("[SPS] Error Creating Storage Assets: " + e.getMessage());
                System.exit(0);
            }
        }

        if (!savedPasswordsFile.exists()){
            try {
                savedPasswordsFile.createNewFile();
            }catch (Exception e){
                System.out.println("[SPS] Error Creating Storage Assets: " + e.getMessage());
                System.exit(0);
            }
        }
        if (!encryptionKeyFile.exists()){
            try {
                encryptionKeyFile.createNewFile();
            }catch (Exception e){
                System.out.println("[SPS] Error Creating Storage Assets: " + e.getMessage());
                System.exit(0);
            }
        }

        System.out.println("Developed by The Secret Intelligence Squadron [https://tsis.uk] // [Aaron Akhtar]");
        System.out.println(" ");
        System.out.println("Type 'HELP' for a list of commands...");
        System.out.println(" ");

        get();



    }

    public static void get(){
        System.out.print("@SPS: ");
        check(new Scanner(System.in).nextLine().split(" "));
    }

    private static void check(String[] args){

        switch (args[0].toLowerCase()){

            case "help":{
                System.out.println("setKey - set your encryption and decryption key.");
                System.out.println("list - list all your saved passwords.");
                System.out.println("save - save a password.");
                System.out.println("get - decrypt a password.");
                break;
            }

            case "list":{
                for (String x : getPasswords()){
                    System.out.println(x);
                }
                break;
            }

            case "save":{ //save <label> <pass>

                if (args.length != 3){
                    System.out.println("Incorrect Arguments: 'save <label> <pass>'");
                    System.out.println("  - label: the passwords label is just a way to identify what password it is.");
                    System.out.println("  - pass: the password you want to save.");
                    System.out.println("[!] Notice, if you have not already please make sure you set your encryption key password by typing 'setKey'...");System.out.println(" ");
                    break;
                }
                try {
                    Save.savePassword(args[1], args[2]);
                }catch (Exception e){
                    System.out.println("[SPS] Error: " + e.getMessage());
                    System.out.println(" ");
                }
                break;
            }

            case "setkey":{                 //setk <k>
                if (args.length != 2){
                    System.out.println("Incorrect Arguments: 'setKey <key>'");
                    System.out.println("  - key: the key is used to encrypt all your passwords, choose something secure.");
                    break;
                }

                try{
                    Key.setKey(Sha.getSha(args[1]));
                }catch (Exception e){
                    System.out.println("[SPS] Error: "+ e.getMessage());
                }

                break;
            }

            case "get":{  // get <password_by_label> <decryption_key>
                if (args.length != 3){
                    System.out.println("Incorrect Arguments: 'get <password_by_label> <decryption_key>'");
                    System.out.println("  - password_by_label: specify what password you want by the label you saved it with (type 'list' to get a list of your passwords (encrypted version)).");
                    System.out.println("  - decryption_key: the encryption / decryption key you set.");
                    break;
                }
                String f = "";
                boolean g = false;
                for (String s : getPasswords()){
                    if (s.split(":")[0].equalsIgnoreCase(args[1])){
                        f = s.split(":")[1]; g = true;
                    }
                }

                if (!g){
                    System.out.println("[SPS] Invalid Label...");
                    break;
                }
                try {
                    if (!Key.getKey().equals(Sha.getSha(args[2]))){
                        System.out.println("[SPS] Invalid Key..");
                        break;
                    }
                }catch (Exception e){
                    System.out.println("[SPS] Error: " + e.getMessage());break;
                }
                System.out.println("[SPS] Decrypting Password....");

                try {
                    String pass = Aes.decrypt(f, Sha.getSha(args[2]));
                    System.out.println("[SPS] Decryption Successful: " + pass);
                }catch (Exception e){
                    System.out.println("[SPS] Error: " + e.getMessage());
                }
                break;
            }


        }

        get();
    }

    private static Collection<String> getPasswords(){
        try{
            List<String> s = Files.readAllLines(Paths.get(savedPasswordsFile.getAbsolutePath()));
            if (s.isEmpty()){
                System.out.println("[SPS] No saved passwords, type 'save' to get started...");System.out.println(" ");
                get();
            }
            return s;

        }catch (Exception e){
            System.out.println("[SPS] Error: " + e.getMessage());
            get();
        }
        return new ArrayList<>();
    }



}
