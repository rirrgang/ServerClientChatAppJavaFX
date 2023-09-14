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

public class ChatViewController {

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Variablen---------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private MainViewController mainViewController;

    @FXML TextArea chatTextArea;
    @FXML TextField chatInputTextField;
    @FXML Button sendMessageBtn;
    @FXML Button clearTextAreaBtn;



    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Funktionen--------------------------------------------
    //--------------------------------------------------------------------------------------------------

    public void injectMainController(MainViewController mainController) {
        this.mainViewController = mainController;
    }

    public void initialize(){
        chatInputTextField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    mainViewController.client.sendMessage(sendMessage());
                }
            }
        });
    }

    public void displayInput(String input){
        //Auslagern des Updaten der TextArea in einen extra Thread (Bei zu vielen msg crasht sonst die Anwendung, weil die GUI nicht hinterherkommt)
        Platform.runLater(()->chatTextArea.appendText(input+"\n"));
    }

    public void toggleDarkMode(boolean darkMode){
        if(darkMode){
            chatTextArea.getStyleClass().removeAll();
            chatTextArea.getStyleClass().set(0,"chatAreaDarkMode");
            chatInputTextField.getStyleClass().removeAll();
            chatInputTextField.getStyleClass().set(0,"textFieldDarkMode");

        }else{
            chatTextArea.getStyleClass().removeAll();
            chatTextArea.getStyleClass().set(0,"chatArea");
            chatInputTextField.getStyleClass().removeAll();
            chatInputTextField.getStyleClass().set(0,"textField");
        }
    }

    public String sendMessage(){
        String message = chatInputTextField.getText();
        //chatTextArea.appendText(message+"\n");
        chatInputTextField.setText("");
        return message;
    }

    public void clearChat(){
        chatTextArea.setText("");
    }
}
