package code;

import controllers.MainViewController;
import javafx.scene.image.Image;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Variablen---------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private MainViewController mainViewController;
    public static Socket server;
    public int port;
    public String ip;
    public String name;
    public static DataInputStream dis;
    public static DataOutputStream dos;
    public static Thread receiveMessages;
    public Thread connectToServer;
    public static boolean isConnected = false;

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Funktionen--------------------------------------------
    //--------------------------------------------------------------------------------------------------

    public void injectMainController(MainViewController mainController) {
        this.mainViewController = mainController;
    }



    //Methode zum Weiterleiten der Nachrichten an die GUI
    public void forwardOutput(String output){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        mainViewController.forwardInput("["+formatter.format(date)+"]"+output);
    }



    public void connect(){
        mainViewController.clientStatusImage.setImage(new Image("/resources/icons8-pause-96.png"));
        isConnected = true;
        connectToServer = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println("Versuche zum Server ("+ip+":"+port+") zu verbinden...");
                    forwardOutput("Versuche zum Server ("+ip+":"+port+") zu verbinden...");
                    server = new Socket(ip,port);
                    dis = new DataInputStream(server.getInputStream());
                    dos = new DataOutputStream(server.getOutputStream());
                    dos.writeUTF("NAME:" + name);
                    dos.flush();
                    receiveMessages = new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                String message = null;
                                try {
                                    message = dis.readUTF();
                                    forwardOutput(message);
                                } catch (IOException e) {
                                    System.out.println("Nachrichten können nicht empfangen werden...(Sie sind mit keinem Server verbunden!\nVermutlich hat der Server die Verbindung geschlossen)");
                                    forwardOutput("Nachrichten können nicht empfangen werden...(Sie sind mit keinem Server verbunden!\nVermutlich hat der Server die Verbindung geschlossen)");
                                    disconnect();
                                    break;
                                    //e.printStackTrace();
                                }
                            }
                        }
                    };
                    receiveMessages.start();
                } catch (IOException e) {
                    System.out.println("Konnte nicht zu Server ("+ip+":"+port+") verbinden.");
                    forwardOutput("Konnte nicht zu Server ("+ip+":"+port+") verbinden.");
                    //disconnect();
                    mainViewController.toggleConnect();
                    //e.printStackTrace();
                }
            }
        };
        connectToServer.start();
    }



    public void disconnect(){
        try {
            isConnected = false;
            mainViewController.clientStatusImage.setImage(new Image("/resources/icons8-circled-play-96.png"));
            if(server != null){
                server.close();
                dis.close();
                dos.close();
                connectToServer.interrupt();
                receiveMessages.interrupt();
                System.out.println("Sie haben die Verbindung mit dem Server ("+ip+":"+port+") geschlossen.");
                forwardOutput("Sie haben die Verbindung mit dem Server ("+ip+":"+port+") geschlossen.");
            }
        } catch (IOException e) {
            System.out.println("Disconnect ist fehlgeschlagen!");
        }
    }



    //Methode zum Senden von Nachrichten an den Server
    public void sendMessage(String message){
        try {
            if(dos!=null){
                dos.writeUTF("["+name+"]: "+message);
                dos.flush();
            }else{
                System.out.println("Nachricht konnte nicht gesendet werden. (Sie sind mit keinem Server verbunden!)");
                forwardOutput("Nachricht konnte nicht gesendet werden. (Sie sind mit keinem Server verbunden!)");
            }
        } catch (IOException e) {
            System.out.println("Nachricht konnte nicht gesendet werden. (Sie sind mit keinem Server verbunden!)");
            forwardOutput("Nachricht konnte nicht gesendet werden. (Sie sind mit keinem Server verbunden!)");
            //e.printStackTrace();
        }
    }



}
