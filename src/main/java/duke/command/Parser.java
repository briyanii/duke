package duke.command;

import duke.error.DukeException;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

import java.util.Iterator;

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

        if (split[0].length() == 0) {
            throw new DukeMissingCommandException();
        }

        Type commandType = extractCommandType(input);

        if (commandType == Type.COMMAND_EXIT) {
            return new ExitCommand();
        } else if (commandType == Type.COMMAND_SHOW_LIST) {
            return new ShowListCommand();
        }

        String[] arguments = extractArguments(input, commandType);

        switch (commandType) {
        case COMMAND_DELETE_TASK:
            return new DeleteTaskCommand(arguments[0]);
        case COMMAND_COMPLETE_TASK:
            return new CompleteTaskCommand(arguments[0]);
        case COMMAND_SEARCH:
            return new SearchCommand(arguments[0]);
        case COMMAND_ADD_TODO:
            //Fallthrough
        case COMMAND_ADD_DEADLINE:
            //Fallthrough
        case COMMAND_ADD_EVENT:
            return new AddTaskCommand(commandType, arguments);
        default:
            return null; //unreachable
        }
    }

    private static String[] extractArguments(String input, Type commandType) throws DukeMissingArgumentException {
        String[] split = input.trim().split("\\s+");
        String[] argumentsProvided = new String[commandType.getNumberOfArgumentsExpected()];

        Iterator<String> delimiterIterator = commandType.getDelimiters().iterator();

        int parameterCount = 0;

        String nextDelimiter = (delimiterIterator.hasNext()) ? delimiterIterator.next() : " ";

        StringBuilder currentParameter = new StringBuilder();

        for (int wordIndex = 1; wordIndex <= split.length; wordIndex++) {
            if (wordIndex == split.length || split[wordIndex].equals(nextDelimiter)) {
                String parameter = currentParameter.toString().trim();

                argumentsProvided[parameterCount] = (parameter.length() > 0) ? parameter : null;

                if (wordIndex < split.length && split[wordIndex].equals(nextDelimiter)) {
                    nextDelimiter = (delimiterIterator.hasNext()) ? delimiterIterator.next() : " ";
                }

                currentParameter = new StringBuilder();
                parameterCount++;
            } else {
                currentParameter.append(split[wordIndex]);
                currentParameter.append(" ");
            }
        }

        for (String parameter : argumentsProvided) {
            if (parameter == null) {
                throw new DukeMissingArgumentException(commandType, argumentsProvided);
            }
        }
        return argumentsProvided;
    }

    private static Type extractCommandType(String input) throws DukeUnknownCommandException {
        switch (input.trim().split("\\s+")[0]) {
        case "list":
            return Type.COMMAND_SHOW_LIST;
        case "bye":
            return Type.COMMAND_EXIT;
        case "todo":
            return Type.COMMAND_ADD_TODO;
        case "event":
            return Type.COMMAND_ADD_EVENT;
        case "deadline":
            return Type.COMMAND_ADD_DEADLINE;
        case "delete":
            return Type.COMMAND_DELETE_TASK;
        case "done":
            return Type.COMMAND_COMPLETE_TASK;
        case "find":
            return Type.COMMAND_SEARCH;
        default:
            throw new DukeUnknownCommandException();
        }
    }
}
