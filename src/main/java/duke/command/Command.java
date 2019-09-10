package duke.command;

/**
 * An abstract class representing an instruction for Duke.
 *
 * <p>A Command is a data structure that wraps a valid instruction type for Duke, and the arguments
 * that should be used when executing an instruction of that type.</p>
 */
public abstract class Command {
    // the type of instruction to be executed
    private Type type;
    // the arguments to be used when executing
    private String[] arguments;

    /**
     * Constructs the Command.
     *
     * @param type The enumerated Type of Command
     * @param arguments The arguments for the Command, if any
     */
    Command(Type type, String... arguments) {
        assert type != null;
        assert arguments != null;
        this.type = type;
        this.arguments = arguments;
    }

    /**
     * Returns the arguments provided along with the Command.
     *
     * @return The arguments provided along with the Command, in a String[].
     */
    public String[] getArgumentsUsed() {
        return arguments;
    }

    /**
     * Returns the enumerated Type of the Command.
     *
     * @return The enumerated Type of the command.
     */
    public Type getType() {
        return type;
    }
}





