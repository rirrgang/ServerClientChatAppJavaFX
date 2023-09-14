package code;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread{

    private code.Server Server;
    public DataInputStream dis;
    public DataOutputStream dos;
    public Socket client;
    private String msgReceived;
    public String clientName ="";
    public Thread receiveMessages;

    ClientHandler(Socket client, DataInputStream dis, DataOutputStream dos, code.Server Server){
        this.dis = dis;
        this.dos = dos;
        this.client = client;
        this.Server = Server;
    }


    @Override
    public void run() {
        try {
            dos.writeUTF("Sie wurden erfolgreich mit dem Server verbunden!");
        } catch (IOException e) {
            //System.out.println("Sie konnten nicht mit dem Server verbunden werden!");
            //e.printStackTrace();
            //System.out.println("dos fehler? xD");
        }

        receiveMessages = new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        msgReceived = dis.readUTF();
                        if (msgReceived.substring(0,4).equals("NAME")){
                            clientName = msgReceived.substring(5);
                            Server.clientNames.add(clientName);
                            Server.forwardOutput("Ein neuer Client ist dem Server beigetreten: " + clientName);
                            Server.sendMsgToAllClients("Ein neuer Client ist dem Server beigetreten: " + clientName);
                        }else{
                            Server.forwardOutput(msgReceived);
                            //System.out.println(client.getInetAddress() + ": " + msgReceived);
                            //dos.writeUTF(msgReceived);
                            Server.sendMsgToAllClients(msgReceived);
                        }

                    } catch (IOException e) {
                        //e.printStackTrace();
                        //System.out.println("MSG-Received Fehler ---> Client hat die Verbindung abgebrochen.");
                        if(Server.clientCounter <= Server.maxClients){
                            Server.forwardOutput("Der Client " + clientName + " hat den Server verlassen.");
                            Server.sendMsgToAllClients("Der Client " + clientName + " hat den Server verlassen.");
                        }
                        Server.clientCounter--;
                        Server.clients.remove(this);
                        //Server.searchForClients.run();
                        break;
                    }
                }
            }
        };
        receiveMessages.start();


    }
}
