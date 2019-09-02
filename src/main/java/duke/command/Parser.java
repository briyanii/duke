package duke.command;

import duke.error.DukeException;

import java.util.Iterator;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

/**
 * Static methods for parsing user input into usable information for other methods
 */
public class Parser {

    /**
     * Tries to parse input string as Date and Time in dd/MM/yyyy HHmm, and format into a format that cannot be misread:
     * dd/MM/yyyy HHmm(e.g. 12/12/1212 1212 -> 12th of DECEMBER 1212, 12:12pm)
     *
     * @param dateTimeString The input string to be parsed, and formatted, if possible
     * @return The formatted date and time, if it can be formatted
     * @throws DukeException The exception thrown when the input cannot be formatted
     */
    public static String parseDateTime(String dateTimeString) throws DukeException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
            LocalDateTime dateAndTime = LocalDateTime.parse(dateTimeString, formatter);

            int day = dateAndTime.getDayOfMonth();
            String month = dateAndTime.getMonth().toString();
            int year = dateAndTime.getYear();
            int hour = dateAndTime.getHour();
            int minute = dateAndTime.getMinute();

            StringBuilder dateTime = new StringBuilder();

            dateTime.append(getIntegerOrdinal(day));
            dateTime.append(" of ");
            dateTime.append(month);
            dateTime.append(" ");
            dateTime.append(year);
            dateTime.append(", ");
            dateTime.append((hour > 12 ? hour - 12 : hour == 0 ? 12 : hour));
            if (minute != 0) {
                dateTime.append(":");
                dateTime.append(minute);
            }
            if (hour < 12) {
                dateTime.append("am");
            } else {
                dateTime.append("pm");
            }

            return dateTime.toString();
        } catch (DateTimeParseException exception) {
            throw new DukeException(dateTimeString + " is not in dd/MM/yyyy HHmm format.");
        }
    }

    // helper method to format a number into it's ordinal form
    private static String getIntegerOrdinal(int integer) {
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
     * Tries to parse user input as a command, with arguments if any, and formats it in a way that it can be used by
     * other methods using the commands
     *
     * @param input The input string to be parsed as a command
     * @return A data structure containing the type of command, and the arguments provided with it, if any
     * @throws DukeException The exception thrown when there is an error when attempting to format the input as command
     */
    public static Command parseAsCommand(String input) throws DukeException {
        input = input.trim();
        String[] split = input.split("\\s+");
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

        String nextDelimiter;
        int parameterCount = 0;

        if (delimiterIterator.hasNext()) {
            nextDelimiter = delimiterIterator.next();
        } else {
            nextDelimiter = " ";
            // since split by whitespaces there will not be a word that is " "
        }

        StringBuilder currentParameter = new StringBuilder();

        for (int i = 1; i <= split.length; i++) {
            if (i == split.length || split[i].equals(nextDelimiter)) {
                String parameter = currentParameter.toString().trim();

                if (parameter.length() > 0) {
                    parametersProvided[parameterCount] = parameter;
                } else {
                    parametersProvided[parameterCount] = null;
                }

                if (i < split.length && split[i].equals(nextDelimiter)) {
                    if (delimiterIterator.hasNext()) {
                        nextDelimiter = delimiterIterator.next();
                    } else {
                        nextDelimiter = " ";
                    }
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
                throw new DukeMissingParameterException(commandType, parametersProvided);
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
