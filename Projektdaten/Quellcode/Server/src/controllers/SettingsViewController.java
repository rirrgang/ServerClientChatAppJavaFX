package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class SettingsViewController {

    private MainViewController mainViewController;
    public int port;
    public String ip="localhost";
    public int maxClients = 2;

    @FXML Button saveSettingsBtn;

    @FXML TextField portTextField;
    @FXML TextField ipTextField;

    @FXML Slider clientSlider;

    public void injectMainController(MainViewController mainController) {
        this.mainViewController = mainController;
    }

    public void initialize(){
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

    }

    public void saveSettings(){
        String port = portTextField.getText();
        String ip = ipTextField.getText();

        this.maxClients =  (int)clientSlider.getValue();

        if(port!="" && port!=null && !port.isEmpty()){
            this.port = Integer.parseInt(port);
            System.out.println(port);
            portTextField.setPromptText(port);
            portTextField.setText("");
        }

        if(ip!="" && ip!=null && !ip.isEmpty()){
            this.ip = ip;
            System.out.println(ip);
            ipTextField.setPromptText(ip);
            ipTextField.setText("");
        }

        mainViewController.setConnectionParams();
        mainViewController.changeView(0);

    }


}
