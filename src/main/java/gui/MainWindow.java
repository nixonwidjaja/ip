package gui;

import duke.Duke;
import duke.DukeException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    private Duke duke;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setDuke(Duke d) {
        this.duke = d;
        dialogContainer.getChildren().addAll(
            DialogBox.getDukeDialog(duke.init(), dukeImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = getResponse(input);
        dialogContainer.getChildren().addAll(
            DialogBox.getUserDialog(input, userImage),
            DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
        if (duke.isTerminated()) {
            userInput.setDisable(true);
            userInput.setPromptText("AdaBot is terminated. You may close this window.");
            sendButton.setDisable(true);
        }
    }


    /**
     * Gets response from Duke.
     * @param input user input
     * @return Duke response
     */
    private String getResponse(String input) {
        try {
            return duke.process(input);
        } catch (DukeException e) {
            return "OOPS! " + e.toString();
        }
    }
}
