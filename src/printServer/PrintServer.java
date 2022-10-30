package printServer;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class PrintServer  extends UnicastRemoteObject implements IPrintServer {

    boolean isServerStarted = false;
    public int queueNo = 1;
    private LinkedList<PrinterQueue> printQueue = new LinkedList<PrinterQueue>();

    public PrintServer() throws RemoteException {
        super();
    }

    @Override
    public void print(String filename, String printer) throws RemoteException {
        try {
            if (isStarted()) {
                PrinterQueue job = new PrinterQueue(filename, printer, queueNo);
                printQueue.add(job);
                queueNo++;
                System.out.println("print() invoked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String queue(String printer) {
        String queue = "";
        try {
            if (isStarted()) {

                for(PrinterQueue pq: printQueue) {
                    queue += pq + "\n";
                }
            }
            System.out.println("queue() invoked");
            return queue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queue;
    }

    @Override
    public void topQueue(String printer, int job) {
        try {
            if (isStarted()) {
                PrinterQueue printingJobMoved = null;
                for(PrinterQueue pq : printQueue) {
                    if(pq.queueNo == job) {
                        printingJobMoved = pq;
                        printQueue.remove(pq);
                        break;
                    }
                }
                printQueue.addFirst(printingJobMoved);
                System.out.println("topQueue() invoked");
            }

        } catch (IndexOutOfBoundsException ie) {
            System.out.println("Invalid JobNo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        isServerStarted = true;
        System.out.println("start() invoked");

    }

    @Override
    public void stop() {
        isServerStarted = false;
        System.out.println("stop() invoked");

    }

    @Override
    public void restart() {
        isServerStarted = false;
        printQueue.clear();
        isServerStarted = true;
//        System.out.println("Printer is restarted");
        System.out.println("restart() invoked");
    }

    @Override
    public boolean status(String printer) {
        try {
            System.out.println("status() invoked");
            return isStarted();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String readConfig(String parameter) {
        try {
            FileReader fReader = new FileReader("project.config");
            BufferedReader bReader = new BufferedReader(fReader);
            String line;
            String configVal = null;
            while ((line = bReader.readLine()) != null) {
                if (line.split("=")[0].equalsIgnoreCase(parameter)) {
                    configVal = line.split("=")[1];
                    break;
                }
            }
            fReader.close();
            bReader.close();
            System.out.println("readConfig() invoked");
            return configVal;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setConfig(String parameter, String value) {
//        try {
//            PrintWriter pWriter = new PrintWriter(new BufferedWriter(new FileWriter("project.config", true)));
//            pWriter.println(parameter+"="+value);
//            pWriter.close();
            System.out.println("setConfig() invoked");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean isAuthorized(String username, String password) {
        FileReader fReader = null;
        try {
            fReader = new FileReader("enc_passwords.txt");
            BufferedReader bReader = new BufferedReader(fReader);
            String line;
            while ((line = bReader.readLine()) != null) {
                if(line.contains(username)) {
                    String correctEncPassword = line.split(":")[1];
                    if(correctEncPassword.equals(PasswordEncrypter.getEncryptedPassword(password)))
                        return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean isStarted() throws Exception {
//        if(!isServerStarted) {
//            throw new Exception("Printer not started!");
//        }
//        return isServerStarted;
//    }

    @Override
    public boolean isStarted() {
        if(!isServerStarted) {
            System.out.println("\t" + "Printer not started!");
            System.out.println("Please START the printer to see method invocation status"+"\n");
        }
        return isServerStarted;
    }

    public class PrinterQueue {
        public String fileName;
        public String printer;
        public int queueNo;

        public PrinterQueue(String fileName, String printer, int queueNo) {
            this.fileName = fileName;
            this.printer = printer;
            this.queueNo = queueNo;
        }


        public String toString() {
            return (this.queueNo + "\t\t\t" + this.fileName + "\t\t\t" + this.printer + "\n");
        }
    }
}
