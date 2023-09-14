package controllers;

import code.Animations;
import code.Client;
import code.StartClientGui;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController {

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Variablen--------------------------------------------
    //--------------------------------------------------------------------------------------------------

    public Client client = new Client();
    public boolean isConnected = false;
    private int menu=0;

    @FXML private ChatViewController chatViewController;
    @FXML private SettingsViewController settingsViewController;
    @FXML public StartClientGui startClientGui;

    @FXML Button minimizeBtn;
    @FXML Button closeBtn;
    @FXML Button startClientBtn;
    @FXML Button chatBtn;
    @FXML Button settingsBtn;

    @FXML Label clientNameLabel;

    @FXML public ImageView clientStatusImage;

    @FXML AnchorPane clientAnchorPane;
    @FXML AnchorPane settingsView;
    @FXML AnchorPane chatView;
    @FXML AnchorPane menuAnchorPane;
    @FXML AnchorPane viewPort;
    @FXML AnchorPane footer;



    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Funktionen--------------------------------------------
    //--------------------------------------------------------------------------------------------------

    @FXML private void initialize(){
        initializeMehods();
        injectControllers();
        hideAll();
        viewPort.setVisible(true);
        setConnectionParams();
        //viewPort.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, black, gray)");
    }



    private void injectControllers(){
        chatViewController.injectMainController(this);
        settingsViewController.injectMainController(this);
        client.injectMainController(this);
    }



    private void initializeMehods(){
        closeBtn.setOnMouseClicked(mouseEvent -> closeClient());
        minimizeBtn.setOnMouseClicked(mouseEvent -> minimizeClient());
        startClientBtn.setOnMouseClicked(mouseEvent -> toggleConnect());
        settingsBtn.setOnMouseClicked(mouseEvent -> changeView(1));
        chatBtn.setOnMouseClicked(mouseEvent -> changeView(2));

        chatViewController.sendMessageBtn.setOnMouseClicked(mouseEvent -> client.sendMessage(chatViewController.sendMessage()));
        chatViewController.clearTextAreaBtn.setOnMouseClicked(mouseEvent -> chatViewController.clearChat());

        settingsViewController.settingsSaveBtn.setOnMouseClicked(mouseEvent -> settingsViewController.saveSettings());
    }



    public void setConnectionParams(){
        client.ip = settingsViewController.ip;
        client.port = settingsViewController.port;
        client.name = settingsViewController.name;
        clientNameLabel.setText(client.name);
    }



    public void toggleConnect(){
        if(!client.isConnected){
            client.connect();
            if(chatView.isVisible()==false){
                changeView(2);
            }
        }else{
            client.disconnect();
        }
    }



    public void forwardInput(String input){
        chatViewController.displayInput(input);
    }




    public void hideAll(){
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {settingsView.setVisible(false); chatView.setVisible(false);});
        delay.play();
    }



    public void setVisible(AnchorPane pane){
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> pane.setVisible(true));
        delay.play();
    }



    public void toggleDarkMode(boolean darkMode){
        if(darkMode){
            viewPort.getStyleClass().removeAll();
            viewPort.getStyleClass().set(0,"viewportDarkMode");
            menuAnchorPane.getStyleClass().removeAll();
            menuAnchorPane.getStyleClass().set(0,"menuDarkMode");

            chatViewController.toggleDarkMode(true);
            settingsViewController.toggleDarkMode(true);
        }else{
            viewPort.getStyleClass().removeAll();
            viewPort.getStyleClass().set(0,"viewport");
            menuAnchorPane.getStyleClass().removeAll();
            menuAnchorPane.getStyleClass().set(0,"menuLightMode");

            chatViewController.toggleDarkMode(false);
            settingsViewController.toggleDarkMode(false);
        }
    }



    public void changeView(int view){
        switch (view){
            case 0:
                Animations.slideHorizontalRightEffect(viewPort);
                hideAll();
                menu = 0;
                break;
            case 1:
                if(menu!=1){
                    Animations.slideHorizontalLeftEffect(viewPort);
                    hideAll();
                    setVisible(settingsView);
                    menu = 1;
                }else{
                    Animations.slideHorizontalRightEffect(viewPort);
                    hideAll();
                    menu = 0;
                }
                break;
            case 2:
                if(menu!=2) {
                    Animations.slideHorizontalRightEffect(viewPort);

                    hideAll();
                    setVisible(chatView);
                    menu = 2;
                }else{
                    Animations.slideHorizontalLeftEffect(viewPort);
                    hideAll();
                    menu = 0;
                }
                break;

        }
    }


    //--------------------------------------------------------------------------------------------------
    //---------------------------------------BUTTON AKTIONEN--------------------------------------------
    //--------------------------------------------------------------------------------------------------




    @FXML private void closeClient(){

        Animations.slideDown(clientAnchorPane);
        Animations.slideLeft(clientAnchorPane);
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> System.exit(0));
        delay.play();

    }

    @FXML private void minimizeClient(){

        Stage stage = (Stage)clientAnchorPane.getScene().getWindow();
        Animations.fadeOff(clientAnchorPane);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> stage.setIconified(true));
        delay.play();

        //Listener auf dem stage-iconifiedproperty
        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean isHidden) {

                if(!isHidden){
                    Animations.fadeIn(clientAnchorPane);
                }
            }
        });

    }




}
