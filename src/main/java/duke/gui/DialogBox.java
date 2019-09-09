package duke.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.File;

/**
 * A custom control using FXML.
 *
 * <p>This control represents dialog from either Duke or the user. It consists of a Circle with an ImagePattern
 * fill to represent the speaker, and a Label to display the message from the speaker.</p>
 */
class DialogBox extends HBox {
    @FXML
    private Label text;
    @FXML
    private Circle icon;

    /**
     * Constructs the dialog box.
     *
     * @param speakerText The speaker's text to be displayed in the dialog box
     * @param img The image to be used as the display picture
     */
    private DialogBox(String speakerText, Image img) {
        assert speakerText != null;
        assert img != null;

        try {
            assert new File("src/main/resources/view/DialogBox.fxml").exists() : "DialogBox.fxml file does not exist";
            FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        text.setText(speakerText);
        text.setPadding(new Insets(0,5,0,42.5));
        setHeight(text.getHeight() +  10);

        // formats the display picture
        icon.setFill(new ImagePattern(img));
    }

    // mirrors the dialog box elements orientation
    private void flip() {
        this.setAlignment(Pos.CENTER_LEFT);
        text.setPadding(new Insets(0,42.5,0,5));
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /**
     * Returns a dialog box formatted to display a message from the user.
     *
     * @param userText The user's message
     * @param img The user's display picture
     * @return The dialog box formatted to display the user's message
     */
    static DialogBox getUserDialog(String userText, Image img) {
        assert userText != null;
        assert img != null;

        DialogBox user = new DialogBox(userText, img);

        // sets the background to a green rounded corner box
        user.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(0,100, 0),
                                new CornerRadii(10),
                                new Insets(5,37.5,5,37.5))));
        return user;
    }

    /**
     * Returns a dialog box formatted to display messages from Duke.
     *
     * @param dukeText Duke's message
     * @param img Duke's display picture
     * @return The dialog box formatted to display duke's message
     */
    static DialogBox getDukeNormalDialog(String dukeText, Image img) {
        assert dukeText != null;
        assert img != null;

        DialogBox duke = new DialogBox(dukeText, img);

        // sets the background to a blue rounded corner box
        duke.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(0,0, 100),
                                new CornerRadii(10),
                                new Insets(5,37.5,5,37.5))));
        duke.flip();

        return duke;
    }

    /**
     * Returns a dialog box formatted to display error messages from Duke.
     *
     * @param dukeErrorText Duke's error message
     * @param img Duke's display picture
     * @return The dialog box formatted to display duke's error message
     */
    static DialogBox getDukeErrorDialog(String dukeErrorText, Image img) {
        assert dukeErrorText != null;
        assert img != null;

        DialogBox duke = new DialogBox(dukeErrorText, img);

        // sets the background to a red rounded corner box
        duke.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(100,0,0),
                                new CornerRadii(10),
                                new Insets(5,37.5,5,37.5))));
        duke.flip();

        return duke;
    }

}

