package duke.ui;

import duke.error.DukeException;

import java.util.Optional;

/**
 * A Response represents a Response from Dukes as a result of some user input, as well as some
 * information about the current State of Duke after trying to carry out the instruction the input.
 */
public class Response {
    private Optional<DukeException> dukeException;
    private String message;
    private boolean isActive;

    private Response(String message, DukeException dukeException, boolean isActive) {
        assert message != null : "Response message cannot be null";
        this.message = message;
        this.dukeException = Optional.ofNullable(dukeException);
        this.isActive = isActive;
    }

    /**
     * Returns a Response from Duke if a Command is successfully executed.
     *
     * <p>The Response contains the message Duke responds with, and the activity status of Duke.</p>
     *
     * @param message The message from Duke due to successfully executing a Command
     * @param isActive True if Duke is able to accept further Commands, false otherwise.
     * @return The Response from successfully executing a Command
     */
    static Response fromString(String message, boolean isActive) {
        assert message != null;
        return new Response(message, null, isActive);
    }


    /**
     * Returns a Response from Duke if a Command was not successfully executed.
     *
     * <p>The Response contains the error message from Duke after an error occurs after user input, and
     * the activity status of Duke at the time of the Response.</p>
     *
     * @param dukeException The DukeException thrown when the error occurs
     * @param isActive True if Duke is able to respond to further Commands, false otherwise.
     * @return The Response due to the error that occurred in Duke.
     */
    static Response fromError(duke.error.DukeException dukeException, boolean isActive) {
        assert dukeException != null : "DukeException cannot be null for error Response";
        return new Response(dukeException.getMessage(), dukeException, isActive);
    }

    /**
     * Returns true if the Response was a result of a DukeException, false otherwise.
     *
     * @return True if the Response was a result of a DukeException, False otherwise.
     */
    public boolean wasCausedByError() {
        return dukeException.isPresent();
    }

    /**
     * Returns true if Response indicated Duke can take further user input, false otherwise.
     *
     * @return True if Response indicates Duke can take further user input, False otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Returns the String representation of the Response.
     *
     * @return the String representation of the Response.
     */
    @Override
    public String toString() {
        return message;
    }
}