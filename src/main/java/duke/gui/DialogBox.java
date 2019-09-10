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

    private static final Insets DIALOGBOX_BACKGROUND_INSET = new Insets(5,37.5,5,37.5);
    private static final Insets LABEL_PADDING_INSET_LEFT = new Insets(0,42.5,0,5);
    private static final Insets LABEL_PADDING_INSET_RIGHT = new Insets(5,5,5,42.5);

    private static final CornerRadii DIALOGBOX_BACKGROUND_RADII_TEN = new CornerRadii(10);

    private static final Color DIALOGBOX_BACKGROUND_RED = Color.rgb(100,0,0);
    private static final Color DIALOGBOX_BACKGROUND_BLUE = Color.rgb(0,0,100);
    private static final Color DIALOGBOX_BACKGROUND_GREEN = Color.rgb(0,100,0);

    /**
     * Constructs the dialog box.
     *
     * @param speakerText The speaker's text to be displayed in the dialog box
     * @param image The image to be used as the display picture
     */
    private DialogBox(String speakerText, Image image) {
        assert speakerText != null;
        assert image != null;

        try {
            assert new File("src/main/resources/view/DialogBox.fxml").exists() : "DialogBox.fxml file does not exist";
            FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        text.setText(speakerText);
        text.setPadding(LABEL_PADDING_INSET_RIGHT);

        // formats the display picture
        icon.setFill(new ImagePattern(image));
    }

    // mirrors the dialog box elements orientation
    private void flip() {
        this.setAlignment(Pos.CENTER_LEFT);
        text.setPadding(LABEL_PADDING_INSET_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /**
     * Returns a dialog box formatted to display a message from the user.
     *
     * @param userText The user's message
     * @param image The user's display picture
     * @return The dialog box formatted to display the user's message
     */
    static DialogBox getUserDialog(String userText, Image image) {
        assert userText != null;
        assert image != null;

        DialogBox user = new DialogBox(userText, image);

        // sets the background to a green rounded corner box
        user.setBackground(
                new Background(
                        new BackgroundFill(
                                DIALOGBOX_BACKGROUND_GREEN,
                                DIALOGBOX_BACKGROUND_RADII_TEN,
                                DIALOGBOX_BACKGROUND_INSET
                        )
                )
        );

        return user;
    }

    /**
     * Returns a dialog box formatted to display messages from Duke.
     *
     * @param dukeText Duke's message
     * @param image Duke's display picture
     * @return The dialog box formatted to display duke's message
     */
    static DialogBox getDukeNormalDialog(String dukeText, Image image) {
        assert dukeText != null;
        assert image != null;

        DialogBox duke = new DialogBox(dukeText, image);

        // sets the background to a blue rounded corner box
        duke.setBackground(
                new Background(
                        new BackgroundFill(
                                DIALOGBOX_BACKGROUND_BLUE,
                                DIALOGBOX_BACKGROUND_RADII_TEN,
                                DIALOGBOX_BACKGROUND_INSET
                        )
                )
        );

        duke.flip();

        return duke;
    }

    /**
     * Returns a dialog box formatted to display error messages from Duke.
     *
     * @param dukeErrorText Duke's error message
     * @param image Duke's display picture
     * @return The dialog box formatted to display duke's error message
     */
    static DialogBox getDukeErrorDialog(String dukeErrorText, Image image) {
        assert dukeErrorText != null;
        assert image != null;

        DialogBox duke = new DialogBox(dukeErrorText, image);

        // sets the background to a red rounded corner box
        duke.setBackground(
                new Background(
                        new BackgroundFill(
                                DIALOGBOX_BACKGROUND_RED,
                                DIALOGBOX_BACKGROUND_RADII_TEN,
                                DIALOGBOX_BACKGROUND_INSET
                        )
                )
        );

        duke.flip();

        return duke;
    }

}

