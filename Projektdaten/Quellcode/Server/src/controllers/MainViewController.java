package controllers;

import code.Animations;
import code.Server;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController {

    public Server server = new Server();
    private int menu = 0;

    @FXML private ConsoleViewController consoleViewController;
    @FXML private SettingsViewController settingsViewController;

    @FXML Button settingsBtn;
    @FXML Button consoleBtn;
    @FXML Button startServerBtn;
    @FXML Button minimizeBtn;
    @FXML Button closeBtn;

    @FXML public ImageView serverStatusImage;

    @FXML AnchorPane viewPort;
    @FXML AnchorPane settingsView;
    @FXML AnchorPane consoleView;
    @FXML AnchorPane serverAnchorPane;

    @FXML private void initialize(){
        injectControllers();
        initializeMethods();
        hideAll();
    }

    public void forwardInput(String input){
        consoleViewController.displayInput(input);
    }

    public void setConnectionParams(){
        server.port = settingsViewController.port;
        server.ip = settingsViewController.ip;
        server.maxClients = settingsViewController.maxClients;
    }

    private void injectControllers(){
        consoleViewController.injectMainController(this);
        settingsViewController.injectMainController(this);
        server.injectMainController(this);
    }

    private void initializeMethods(){
        settingsBtn.setOnMouseClicked(event -> changeView(1));
        consoleBtn.setOnMouseClicked(event -> changeView(2));
        startServerBtn.setOnMouseClicked(mouseEvent -> toggleServerState());
        minimizeBtn.setOnMouseClicked(mouseEvent -> minimizeServer());
        closeBtn.setOnMouseClicked(mouseEvent -> closeServer());

        settingsViewController.saveSettingsBtn.setOnMouseClicked(mouseEvent -> settingsViewController.saveSettings());
        consoleViewController.sendMessageBtn.setOnMouseClicked(mouseEvent -> server.sendMsgToAllClients(consoleViewController.sendMessage()));
        consoleViewController.clearConsoleBtn.setOnMouseClicked(mouseEvent -> consoleViewController.clearConsole());
    }

    private void hideAll(){
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {settingsView.setVisible(false); consoleView.setVisible(false);});
        delay.play();
    }

    public void setVisible(AnchorPane pane){
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> pane.setVisible(true));
        delay.play();
    }

    public void toggleServerState(){
        if(!server.isStarted){
            server.startServer();
            if(consoleView.isVisible()==false){
                changeView(2);
            }
        }else{
            server.stopServer();
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
                    setVisible(consoleView);
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




    @FXML private void closeServer(){

        server.stopServer();
        Animations.slideDown(serverAnchorPane);
        Animations.slideLeft(serverAnchorPane);
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> System.exit(0));
        delay.play();

    }

    @FXML private void minimizeServer(){

        Stage stage = (Stage)serverAnchorPane.getScene().getWindow();
        Animations.fadeOff(serverAnchorPane);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> stage.setIconified(true));
        delay.play();

        //Listener auf dem stage-iconifiedproperty
        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean isHidden) {

                if(!isHidden){
                    Animations.fadeIn(serverAnchorPane);
                }
            }
        });

    }

}
