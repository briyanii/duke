package duke.command;

/**
 * A Command which is used to instruct Duke to add a Task to the TaskList.
 */
public class AddTaskCommand extends Command {

    /**
     * Constructs a Command to instruct Duke to add a Task to the TaskList.
     *
     * @param type The Type of Command to add a Task (begins with prefix COMMAND_ADD_)
     * @param arguments Description of the task, and Time of the task (if applicable)
     */
    AddTaskCommand(Type type, String... arguments) {
        super(type, arguments);
        assert arguments != null;
    }
}