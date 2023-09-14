package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        TextArea textArea = new TextArea();
        TextFlow flow = new TextFlow();
        String log = ">> Sample passed \n";
        Text t1 = new Text();
        t1.setStyle("-fx-fill: #4F8A10;-fx-font-weight:bold;");
        t1.setText(log);
        Text t2 = new Text();
        t2.setStyle("-fx-fill: RED;-fx-font-weight:normal;");
        t2.setText(log);
        flow.getChildren().addAll(t1, t2);
        textArea.appendText(flow.getChildren().forEach(text ->));


        primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(new StackPane(textArea), 300, 250));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
