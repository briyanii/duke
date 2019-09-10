package duke.tasklist;

import duke.error.DukeException;
import duke.command.Parser;

/**
 * A Task in a TaskList that represents an Event with a description and a time/date which the Event
 * Task occurs at.
 */
public class Event extends Task {
    public String time;

    /**
     * Constructs an Event task, with description and time.
     *
     * @param description The description of the event
     * @param time The time the event occurs at
     */
    public Event(String description, String time) {
        super(description);
        assert time != null : "Event time cannot be null";
        assert description != null;
        try {
            this.time = Parser.parseDateTime(time);
        } catch (DukeException dukeException) {
            this.time = time;
        }
    }

    /**
     * Constructs an Event task with a specified completion status.
     *
     * @param isComplete The completion status of the Event
     * @param description The description of the Event
     * @param time The time the Event occurs
     */
    public Event(boolean isComplete, String description, String time) {
        super(description);

        assert time != null : "Event time cannot be null";
        assert description != null;

        try {
            this.time = Parser.parseDateTime(time);
        } catch (DukeException dukeException) {
            this.time = time;
        }
        taskCompletionStatus = isComplete;
    }

    /**
     * Returns the string representation of the Event Task.
     *
     * @return The string representation of the Event Task
     */
    @Override
    public String toString() {
        return new StringBuilder("[E]").append(super.toString())
                .append(" (at: ")
                .append(time)
                .append(")")
                .toString();
    }

}
