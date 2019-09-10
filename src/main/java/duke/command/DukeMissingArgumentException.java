package duke.command;

import duke.error.DukeException;

import java.util.ArrayList;

/**
 * A DukeException to be thrown when the parameters required for the Command are not provided.
 */
public class DukeMissingArgumentException extends DukeException {

    /**
     * Constructor for the DukeException to be thrown when needed parameters for a Command are not provided.
     *
     * @param type The type of the Command which parameters were not all provided arguments
     * @param arguments The arguments provided along with the Command
     */
    DukeMissingArgumentException(Type type, String[] arguments) {
        super(missingArguments(type, arguments));
    }

    // helper method to get the parameters which are missing arguments
    private static String missingArguments(Type type, String[] argumentsProvided) {
        assert type != null;
        assert argumentsProvided != null;

        StringBuilder message = new StringBuilder("The following field(s) cannot be empty:");
        ArrayList<String> parameters = type.getParameters();
        for (int argumentIndex = 0; argumentIndex < type.getNumberOfArgumentsExpected(); argumentIndex++) {
            if (argumentsProvided[argumentIndex] == null) {
                message.append("\n>>>");
                message.append(parameters.get(argumentIndex));
            }
        }
        return message.toString();
    }
}