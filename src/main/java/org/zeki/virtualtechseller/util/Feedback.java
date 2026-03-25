package org.zeki.virtualtechseller.util;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Feedback {

    private Feedback(){}

    private static PauseTransition feedBackTransition = null;

    public static void showFeedback(Label label) {
        if (feedBackTransition != null) {
            feedBackTransition.stop();
        }
        label.setVisible(true);
        feedBackTransition = new PauseTransition(Duration.seconds(2));
        feedBackTransition.setOnFinished((event) -> {
            label.setVisible(false);
            feedBackTransition = null;
        });
        feedBackTransition.play();
    }

}
