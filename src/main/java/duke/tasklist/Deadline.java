package duke.tasklist;

import duke.error.DukeException;
import duke.command.Parser;

/**
 * A Task in a TaskList that represents a Deadline with a description and a time/date which the
 * Deadline Task is expected to be completed by.
 */
public class Deadline extends Task {
    public String time;

    /**
     * Constructs the Deadline Task.
     *
     * @param description The description of the Deadline Task
     * @param time        The time/date that the Deadline Task needs to be completed by
     */
    public Deadline(String description, String time) {
        super(description);
        assert description != null;
        assert time != null : "Deadline time cannot be null";
        try {
            this.time = Parser.parseDateTime(time);
        } catch (DukeException dukeException) {
            this.time = time;
        }
    }

    /**
     * Constructs the Deadline Task, with a specified completion status.
     *
     * @param isComplete  The completion status of the Task
     * @param description The description of the Task
     * @param time        The time/date the Task needs to be completed by
     */
    public Deadline(boolean isComplete, String description, String time) {
        super(description);
        assert description != null;
        assert time != null : "Deadline time cannot be null";
        try {
            this.time = Parser.parseDateTime(time);
        } catch (DukeException dukeException) {
            this.time = time;
        }
        taskCompletionStatus = isComplete;
    }

    /**
     * Returns the string representation of the Deadline Task.
     *
     * @return The string representation of the Deadline Task.
     */
    @Override
    public String toString() {
        return new StringBuilder("[D]").append(super.toString())
                .append(" (by: ")
                .append(time)
                .append(")")
                .toString();
    }
}
