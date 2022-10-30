package client;

import printServer.IPrintServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class PrintClient {
    private static Scanner scanner = new Scanner(System.in);
    private static IPrintServer printServer;

    public static void main(String[] args) {
        connectToRemotePrintServer();
        showMenu();
    }

    private static void connectToRemotePrintServer() {
        try {
            FileReader fReader = new FileReader("project.config");
            BufferedReader bReader = new BufferedReader(fReader);
            String host = null, port = null, name = null;
            String line;
            while ((line = bReader.readLine()) != null) {
                if (line.contains("host")) {
                    host = line.split("=")[1];
                }
                if (line.contains("port")) {
                    port = line.split("=")[1];
                }
                if (line.contains("name")) {
                    name = line.split("=")[1];
                }
            }
            printServer = (IPrintServer) Naming.lookup("rmi://"+host+":"+port+"/"+name);
            fReader.close();
            bReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticateUser() throws Exception {
        System.out.println("Enter UserName");
        String user = scanner.nextLine();
        System.out.println("Enter Password");
        String pass = scanner.nextLine();
        if (printServer.isAuthorized(user, pass)) {
            return true;
        }
        throw new Exception("Invalid credentials!");
    }

    public static void showMenu() {

        while (true) {
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            System.out.println("\n\n\n\n======= Print Client ==========");
            System.out.println("Select from following options:");
            System.out.println("1 Start the printer");
            System.out.println("2 Stop the printer");
            System.out.println("3 Restart the printer");
            System.out.println("4 Print a file");
            System.out.println("5 Queue");
            System.out.println("6 TopQueue");
            System.out.println("7 Read Configuration");
            System.out.println("8 Set Configuration");
            System.out.println("9 Check status");
            System.out.println("10 Exit");
            System.out.println("Select option: ");
            String input = scanner.nextLine();

            try {
                switch(input) {
                    case "1":
                        authenticateUser();
                        printServer.start();
                        System.out.println("Printer has been started");
                        break;

                    case "2":
                        authenticateUser();
                        printServer.stop();
                        System.out.println("Printer is stopped");
                        break;

                    case "3":
                        authenticateUser();
                        printServer.restart();
                        System.out.println("Printer is restarted");
                        break;

                    case "4":
                        authenticateUser();
                        System.out.println("Enter File Name");
                        System.out.println("(e.g.  'mounika.txt' or 'chandni.doc')");
                        String file = scanner.nextLine();
                        System.out.println("Enter Printer Number");
                        System.out.println("e.g. choose any between '1' or '10'");
                        String printer = scanner.nextLine();
                        printServer.print(file, printer);
                        System.out.println("Successfully Queued...");
                        System.out.println("view Queue status");
                        break;

                    case "5":
                        authenticateUser();
                        if(printServer.isStarted()) {
                            System.out.println("Enter Printer Number");
                            System.out.println("e.g. choose the same printer number selected previously");
                            String printer1 = scanner.nextLine();
                            System.out.println("JobNumber\t\tFileName\t\tPrinter");
                            System.out.println(printServer.queue(printer1));
                        }
                        break;

                    case "6":
                        authenticateUser();
                        if(printServer.isStarted()) {
                            System.out.println("Enter Printer Number");
                            System.out.println("e.g. choose any between '1' or '10'");
                            String printerName = scanner.nextLine();
                            System.out.println("Enter job number");
                            String jobString = scanner.nextLine();
                            printServer.topQueue(printerName, Integer.valueOf(jobString));
                            System.out.println("Job scheduled");
                        }
                        break;

                    case "7":
                        authenticateUser();
                        System.out.println("Enter parameter");
                        System.out.println("e.g. choose 'port', 'host' or 'name'");
                        String parameter = scanner.nextLine();
                        System.out.println(printServer.readConfig(parameter));
                        break;

                    case "8":
                        authenticateUser();
                        System.out.println("Enter parameter");
                        System.out.println("e.g. choose 'port', 'host' or 'name'");
                        String param = scanner.nextLine();
                        System.out.println("Enter parameter value");
                        String value = scanner.nextLine();
                        printServer.setConfig(param, value);
                        break;

                    case "9":
                        authenticateUser();
//                        if(printServer.isStarted()) {
                            System.out.println("Enter Printer Number");
                            System.out.println("e.g. choose any between '1' or '10'");
                            String printerName = scanner.nextLine();

                            if (printServer.status(printerName)) {
                                System.out.println("Printer Status: " + "ONLINE"+"\n");
                            } else {
                                System.out.println("Printer Status: " + "OFFLINE"+"\n");
                            }
//                        }
                        break;

                    case "10":
                        System.exit(0);

                    default:
                        System.out.println("Invalid option selection!");
                }
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
