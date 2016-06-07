package fr.bsimo.navisenserver;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by Ben on 25/05/16.
 */
public class Main {

    private static Properties prop;

    private static boolean checkProperties() {
        prop = new Properties();
        File base = null;

        try {
            base = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }

        if(base == null) return false;

        File configFile = new File(base, "config.properties");

        if(!configFile.exists()) {
            try {
                configFile.createNewFile();

                prop.setProperty("db_name", "");
                prop.setProperty("db_user", "");
                prop.setProperty("db_pass", "");
                prop.setProperty("server_port", "8888");
                prop.store(new FileOutputStream(configFile), "Database Config");

                System.out.println("Config Properties File created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        InputStream input = null;
        try {
            input = new FileInputStream(configFile);
            prop.load(input);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
    public static void main(String[] args) {
        if(!Main.checkProperties()) return ;

        String db_name = prop.getProperty("db_name");
        String db_user = prop.getProperty("db_user");
        String db_pass = prop.getProperty("db_pass");
        int server_port = Integer.parseInt(prop.getProperty("server_port"));

        if(!BDD.init(db_name, db_user, db_pass)) return ;

        new Navisen(server_port);
    }
}
