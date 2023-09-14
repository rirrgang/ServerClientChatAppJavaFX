package controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleViewController {

    private MainViewController mainViewController;

    @FXML TextArea consoleTextArea;

    @FXML TextField consoleTextField;

    @FXML Button sendMessageBtn;
    @FXML Button clearConsoleBtn;

    public void initialize(){
        consoleTextField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    mainViewController.server.sendMsgToAllClients(sendMessage());
                }
            }
        });
    }

    public void displayInput(String input){
        //Auslagern des Updaten der TextArea in einen extra Thread (Bei zu vielen msg crasht sonst die Anwendung, weil die GUI nicht hinterherkommt)
        Platform.runLater(()->consoleTextArea.appendText(input+"\n"));

    }

    public String sendMessage(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String message = consoleTextField.getText();
        //chatTextArea.appendText(message+"\n");
        consoleTextField.setText("");
        displayInput("["+formatter.format(date)+"]"+"[Server]: " + message);
        return "[Server]: " + message;
    }

    public void clearConsole(){
        consoleTextArea.setText("");
    }

    public void injectMainController(MainViewController mainController) {
        this.mainViewController = mainController;
    }
}
