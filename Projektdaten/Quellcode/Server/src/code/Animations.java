package code;

import javafx.animation.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Animations {

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------ANIMATIONEN--------------------------------------------
    //--------------------------------------------------------------------------------------------------


    public static void slideToNormal(AnchorPane pane){
        Timeline slideToNormal = new Timeline();
        KeyValue kv = new KeyValue(pane.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        KeyValue kv1 = new KeyValue(pane.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0.5), kv1);
        slideToNormal.getKeyFrames().add(kf);
        slideToNormal.getKeyFrames().add(kf1);
        slideToNormal.play();
    }

    public static void slideRight(AnchorPane pane){
        Timeline slideLeft = new Timeline();
        KeyValue kv = new KeyValue(pane.translateXProperty(), pane.getWidth()+10, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        slideLeft.getKeyFrames().add(kf);
        slideLeft.play();
    }

    public static void slideLeft(AnchorPane pane){
        Timeline slideRight = new Timeline();
        KeyValue kv = new KeyValue(pane.translateXProperty(), -pane.getWidth()-10, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        slideRight.getKeyFrames().add(kf);
        slideRight.play();
    }

    public static void slideDown(AnchorPane pane){
        Timeline slideDown = new Timeline();
        KeyValue kv = new KeyValue(pane.translateYProperty(), pane.getHeight()+20, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        slideDown.getKeyFrames().add(kf);
        slideDown.play();
    }

    public static void slideUp(AnchorPane pane){
        Timeline slideUp = new Timeline();
        KeyValue kv = new KeyValue(pane.translateYProperty(), -pane.getHeight(), Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        slideUp.getKeyFrames().add(kf);
        slideUp.play();
    }

    public static void slideHorizontalRightEffect(AnchorPane pane){
        slideRight(pane);
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> slideToNormal(pane));
        delay.play();
    }

    public static void slideHorizontalLeftEffect(AnchorPane pane){
        slideLeft(pane);
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> slideToNormal(pane));
        delay.play();
    }

    public static void slideVerticalDownEffect(AnchorPane pane){
        slideDown(pane);
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> slideToNormal(pane));
        delay.play();
    }

    public static void fadeOff(AnchorPane pane){
        Timeline fadeOff = new Timeline();
        KeyValue kv = new KeyValue(pane.opacityProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        fadeOff.getKeyFrames().add(kf);
        fadeOff.play();
    }

    public static void fadeIn(AnchorPane pane){
        Timeline fadeIn = new Timeline();
        KeyValue kv = new KeyValue(pane.opacityProperty(), 1, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        fadeIn.getKeyFrames().add(kf);
        fadeIn.play();
    }

    public static void fadeInAndOutEffect(AnchorPane pane){
        fadeOff(pane);
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> fadeIn(pane));
        delay.play();
    }

}
