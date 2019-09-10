package duke.command;

import duke.error.DukeException;

import java.util.Iterator;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

/**
 * Static methods for parsing user input into usable information by Duke.
 */
public class Parser {

    /**
     * Parses the argument provided by the user for the Date/Time parameter of the Deadline and
     * Event Tasks, if possible.
     *
     * <p>If the argument provided by the user is in dd/MM/yyyy HHmm format, it will be formatted into a more explicit
     * format. For example, "12/12/1212 1212" will be formatted to "12th of DECEMBER 1212, 12:12pm".</p>
     *
     * @param dateTimeString The input string to be parsed, and formatted, if possible
     * @return The formatted date and time, if the input is valid.
     * @throws DukeException when the input cannot be formatted.
     */
    public static String parseDateTime(String dateTimeString) throws DukeException {
        assert dateTimeString != null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
            LocalDateTime dateAndTime = LocalDateTime.parse(dateTimeString, formatter);

            StringBuilder dateTime = new StringBuilder();

            dateTime.append(getIntegerOrdinal(dateAndTime.getDayOfMonth()));
            dateTime.append(" of ");
            dateTime.append(dateAndTime.getMonth().toString());
            dateTime.append(" ");
            dateTime.append(dateAndTime.getYear());
            dateTime.append(", ");

            int hour = dateAndTime.getHour();
            int minute = dateAndTime.getMinute();

            dateTime.append((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour));
            if (minute != 0) {
                dateTime.append(":");
                dateTime.append(minute);
            }
            dateTime.append(hour < 12 ? "am" : "pm");

            return dateTime.toString();
        } catch (DateTimeParseException exception) {
            throw new DukeException(dateTimeString + " is not in dd/MM/yyyy HHmm format.");
        }
    }

    /**
     * Helper method to format a positive integer into it's ordinal form.
     */
    private static String getIntegerOrdinal(int integer) {
        assert (integer <= 31 && integer > 0);
        int remainderHundred = integer % 100;
        if (remainderHundred > 9 && remainderHundred < 21) {
            return integer + "th";
        } else {
            int remainderTen = integer % 10;
            switch (remainderTen) {
            case 1:
                return integer + "st";
            case 2:
                return integer + "nd";
            case 3:
                return integer + "rd";
            default:
                return integer + "th";
            }
        }
    }

    /**
     * Tries to parse user input as a Command, with arguments, if any.
     *
     * @param input The input string to be parsed as a Command
     * @return A Command, wrapping the user's instructions to Duke, which can instruct Duke to do some action
     * @throws DukeException when an error occurs attempting to parse the input as a valid Command
     */
    public static Command parseAsCommand(String input) throws DukeException {
        String[] split = input.trim().split("\\s+");
        // was a command provided
        if (split[0].length() == 0) {
            throw new DukeMissingCommandException();
        }
        // is the command valid

        Type commandType;

        switch (split[0]) {
        case "list":
            return new ShowListCommand();
        case "bye":
            return new ExitCommand();
        case "todo":
            commandType = Type.COMMAND_ADD_TODO;
            break;
        case "event":
            commandType = Type.COMMAND_ADD_EVENT;
            break;
        case "deadline":
            commandType = Type.COMMAND_ADD_DEADLINE;
            break;
        case "delete":
            commandType = Type.COMMAND_DELETE_TASK;
            break;
        case "done":
            commandType = Type.COMMAND_COMPLETE_TASK;
            break;
        case "find":
            commandType = Type.COMMAND_SEARCH;
            break;
        default:
            throw new DukeUnknownCommandException();
        }

        // if the command requires further parameters
        String[] parametersProvided = new String[Type.getNumberOfParametersExpectedFor(commandType)];

        Iterator<String> delimiterIterator = Type.getDelimitersFor(commandType).iterator();

        int parameterCount = 0;

        String nextDelimiter = (delimiterIterator.hasNext()) ? delimiterIterator.next(): " ";

        StringBuilder currentParameter = new StringBuilder();

        for (int i = 1; i <= split.length; i++) {
            if (i == split.length || split[i].equals(nextDelimiter)) {
                String parameter = currentParameter.toString().trim();

                parametersProvided[parameterCount] = (parameter.length() > 0) ? parameter : null;

                if (i < split.length && split[i].equals(nextDelimiter)) {
                    nextDelimiter = (delimiterIterator.hasNext()) ? delimiterIterator.next(): " ";
                }

                currentParameter = new StringBuilder();
                parameterCount++;
            } else {
                currentParameter.append(split[i]);
                currentParameter.append(" ");
            }
        }

        for (String parameter : parametersProvided) {
            if (parameter == null) {
                throw new DukeMissingArgumentException(commandType, parametersProvided);
            }
        }

        switch (commandType) {
        case COMMAND_DELETE_TASK:
            return new DeleteTaskCommand(parametersProvided[0]);
        case COMMAND_COMPLETE_TASK:
            return new CompleteTaskCommand(parametersProvided[0]);
        case COMMAND_SEARCH:
            return new SearchCommand(parametersProvided[0]);
        case COMMAND_ADD_TODO:
            //Fallthrough
        case COMMAND_ADD_DEADLINE:
            //Fallthrough
        case COMMAND_ADD_EVENT:
            return new AddTaskCommand(commandType, parametersProvided);
        default:
            return null; //unreachable
        }
    }
}
