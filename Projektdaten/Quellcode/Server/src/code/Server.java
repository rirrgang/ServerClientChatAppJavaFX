package code;

import controllers.MainViewController;
import javafx.scene.image.Image;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {

    //Deklarationen / Variablen
    private Server server = this;
    private MainViewController mainViewController;
    private ServerSocket serverSocket;
    public Thread searchForClientsThread;

    public boolean isStarted=false;
    public String ip;
    public int port;
    public int clientCounter = 0;
    public int maxClients = 2;

    public ArrayList<ClientHandler> clients = new ArrayList<>();
    public ArrayList<String> clientNames = new ArrayList<>();

    //Methode zum Weiterleiten der Nachrichten an die GUI
    public void forwardOutput(String output){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        mainViewController.forwardInput("["+formatter.format(date)+"]"+output);
    }

    //Verbindung des MainControllers mit dem Server
    public void injectMainController(MainViewController mainController) {
        this.mainViewController = mainController;
    }

    //Methode zum Starten des Servers
    public void startServer(){
        try {
            //Startet den Server mit festgelegten Port
            mainViewController.serverStatusImage.setImage(new Image("resources/icons8-pause-96.png"));
            isStarted=true;
            InetAddress adress = InetAddress.getByName(ip);
            serverSocket = new ServerSocket(port, 50, adress);
            System.out.println("Server wird auf IP:Host:\t" + adress.getHostAddress()+":"+port + " gestartet...");
            forwardOutput("Server wird auf IP:Host:\t" + adress.getHostAddress()+":"+port + " gestartet...");
            forwardOutput("Server wurde gestartet.");
            System.out.println("Server wurde gestartet.");

            //Thread der nach neuen Clients sucht...
            searchForClientsThread = new Thread(){
                public void run(){
                    while(true){
                        try {
                                Socket client = serverSocket.accept();
                                System.out.println("Ein neuer Client hat sich mit dem Server verbunden.");
                                forwardOutput("Ein neuer Client hat sich mit dem Server verbunden.");

                                //Anbinden der Input/Output Streams

                                DataInputStream dis = new DataInputStream(client.getInputStream());
                                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                                //System.out.println("Input/Output Streams werden verbunden.");
                                //forwardOutput("Input/Output Streams werden verbunden.");

                                //Weiterleiten an ClientHandler
                                ClientHandler ch = new ClientHandler(client, dis, dos, server);
                                clients.add(ch);
                                ch.start();
                                //System.out.println("Client an ClientHandler weitergeleitet.");
                                //forwardOutput("Client an ClientHandler weitergeleitet.");
                            if(clientCounter >= maxClients){
                                System.out.println("Maximale Anzahl an Clients erreicht: " + clientCounter + "/" + maxClients);
                                forwardOutput("Maximale Anzahl an Clients erreicht: " + clientCounter + "/" + maxClients);
                                ch.dos.writeUTF("Maximale Anzahl an Clients erreicht: " + clientCounter + "/" + maxClients);
                                System.out.println("Verbindung mit Client wird getrennt!");
                                forwardOutput("Verbindung mit Client wird getrennt!");

                                ch.interrupt();
                                ch.client.close();
                                //interrupt();
                                //break;
                            }
                            clientCounter++;
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            };
            searchForClientsThread.start();


        } catch (IOException e) {
            //System.out.println("Server kann nicht unter folgender IP:Host gestartet werden:\n\t\t"+ this.ip + ":" + this.port);
            forwardOutput("Server kann nicht unter folgender IP:Host gestartet werden:\n\t\t"+ this.ip + ":" + this.port);
            //e.printStackTrace();
        }

    }

    public void sendMsgToAllClients(String msg){
        clients.forEach(client-> {
            try {
                if (client != null){
                    client.dos.flush();
                    client.dos.writeUTF(msg);
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        });
    }


    //Methode zum Stoppen des Servers
    public void stopServer(){
        if(isStarted == true){
            try {
                System.out.println("Server wird beendet...");
                forwardOutput("Server wird beendet...");
                searchForClientsThread.stop();
                mainViewController.serverStatusImage.setImage(new Image("/resources/icons8-circled-play-96.png"));
                isStarted=false;

                clients.forEach(elem -> {
                    try {
                        elem.client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    elem.interrupt();});
                clientCounter=0;

                serverSocket.close();

                if(serverSocket.isClosed()){
                    System.out.println("Server wurde erfolgreich beendet.");
                    forwardOutput("Server wurde erfolgreich beendet.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
