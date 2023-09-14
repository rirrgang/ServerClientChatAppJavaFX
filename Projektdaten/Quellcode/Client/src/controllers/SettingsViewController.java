package controllers;

import code.Animations;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class SettingsViewController {

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Variablen---------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private MainViewController mainViewController;
    private static Random rand = new Random();
    public  String name = "Client" + rand.nextInt(1000);
    public  String ip = "localhost";
    public  int port = 1234;

    @FXML TextField nameTextField;
    @FXML TextField ipTextField;
    @FXML TextField portTextField;
    @FXML public ArrayList<TextField> settingsTextFields = new ArrayList<TextField>();

    @FXML Label settingsLabel01;
    @FXML Label settingsLabel02;
    @FXML Label settingsLabel03;
    @FXML Label settingsLabel04;

    @FXML Button settingsSaveBtn;

    @FXML ToggleGroup darkModeToggleGroup;
    @FXML RadioButton darkModeEnableRBtn;
    @FXML RadioButton darkModeDisableRBtn;


    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------Funktionen--------------------------------------------
    //--------------------------------------------------------------------------------------------------


    public void injectMainController(MainViewController mainController) {
        this.mainViewController = mainController;
    }



    public void initialize(){

        //Label-List füllen
        settingsTextFields.add(nameTextField);
        settingsTextFields.add(ipTextField);
        settingsTextFields.add(portTextField);

        //Textfeld auf Nummern und Länge beschränken
        portTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    portTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 5){
                    portTextField.setText(newValue.substring(0,5));
                }
            }
        });


        //DarkMode RadioButtons
        darkModeEnableRBtn.setUserData(true);
        darkModeDisableRBtn.setUserData(false);

        darkModeToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> mainViewController.toggleDarkMode((boolean)darkModeToggleGroup.getSelectedToggle().getUserData()));
                Animations.fadeInAndOutEffect(mainViewController.menuAnchorPane);
                Animations.fadeInAndOutEffect(mainViewController.viewPort);
                Animations.fadeInAndOutEffect(mainViewController.footer);
                delay.play();
            }
        });
    }



    public void saveSettings(){
        String name = nameTextField.getText();
        String ip = ipTextField.getText();
        String port = portTextField.getText();

        if(name!="" && name!=null && !name.isEmpty()){
            this.name = name;
            System.out.println(name);
            nameTextField.setPromptText(name);
            nameTextField.setText("");
        }

        if(ip!="" && ip!=null && !ip.isEmpty()){
            this.ip = ip;
            System.out.println(ip);
            ipTextField.setPromptText(ip);
            ipTextField.setText("");
        }

        if(port!="" && port!=null && !port.isEmpty()){
            this.port = Integer.parseInt(port);
            System.out.println(port);
            portTextField.setPromptText(port);
            portTextField.setText("");
        }

        mainViewController.setConnectionParams();
        mainViewController.changeView(0);
    }



    public void toggleDarkMode(boolean darkMode){
        if(darkMode){
            settingsTextFields.forEach(textField -> textField.getStyleClass().removeAll());
            settingsTextFields.forEach(textField -> textField.getStyleClass().set(0,"textFieldDarkMode"));
        }else{
            settingsTextFields.forEach(textField -> textField.getStyleClass().removeAll());
            settingsTextFields.forEach(textField -> textField.getStyleClass().set(0,"textField"));
        }
    }



}
