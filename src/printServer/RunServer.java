package printServer;

import java.io.*;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class RunServer {

    public static final String PLAIN_PASSWORD_FILE_NAME = "plain_passwords.txt";
    public static final String ENCRYPTED_PASSWORD_FILE_NAME = "enc_passwords.txt";
    private static IPrintServer server;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // encrypt all plain passwords
        //encryptPasswords();

        // initialize the printserver object
        server = new PrintServer();

        registerServerObject(server);

        System.out.println("Server is started!");
    }

    private static void registerServerObject(IPrintServer server) throws IOException {

        FileReader fReader = new FileReader("project.config");
        BufferedReader bReader = new BufferedReader(fReader);
        String port = null, name = null;
        port = server.readConfig("port");
        name = server.readConfig("name");

        Registry registry = java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(port));
        registry.rebind(name, server);

    }

    private static void encryptPasswords() throws IOException, NoSuchAlgorithmException {
        FileReader fin = new FileReader(PLAIN_PASSWORD_FILE_NAME);
        BufferedReader bin = new BufferedReader(fin);
        PrintWriter pwriter = new PrintWriter(ENCRYPTED_PASSWORD_FILE_NAME);
        String line;
        while((line = bin.readLine()) != null) {
            String user = line.split(":")[0];
            String pass = line.split(":")[1];
            String encPass = PasswordEncrypter.getEncryptedPassword(pass,null);
            pwriter.println(user+":"+encPass);
        }
        pwriter.close();
    }

}
