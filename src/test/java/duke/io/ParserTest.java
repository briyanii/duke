import org.junit.jupiter.api.Test;

import duke.command.Parser;

import duke.error.DukeException;

import duke.command.AddTaskCommand;
import duke.command.ShowListCommand;
import duke.command.ExitCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class to test certain functionality of the Parser class.
 */
public class ParserTest {

    /**
     * Test that the parseDateTime method can output the properly formatted date and time
     * when a valid input is given.
     */
    @Test
    public void parseDateTime_validInput_success() {
        try {
            assertEquals("12th of DECEMBER 1212, 12:12pm",
                    Parser.parseDateTime("12/12/1212 1212"));
            assertEquals("12th of DECEMBER 1212, 12:12am",
                    Parser.parseDateTime("12/12/1212 0012"));
            assertEquals("23rd of JANUARY 2019, 11pm",
                    Parser.parseDateTime("23/01/2019 2300"));
        } catch (DukeException dukeException) {
            fail("Should not have thrown exception");
        }
    }

    /**
     * Test that the parseDateTime method throws an exception when it tries to parse and
     * format an invalid input.
     */
    @Test
    public void parseDateTime_invalidInput_exceptionThrown() {
        assertThrows(DukeException.class, () -> Parser.parseDateTime("1212"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("12/12/1212"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("12pm"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("never"));
    }

    /**
     * Test that the parseDateTime method throws an exception when the input looks valid but
     * cannot be formatted to the expected format.
     */
    @Test
    public void parseDateTime_validLookingInvaliButdInput_exceptionThrown() {
        assertThrows(DukeException.class, () -> Parser.parseDateTime("12/00/1212 1212"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("12/13/1212 1212"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("99/12/1212 1212"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("00/12/1212 1212"));
        assertThrows(DukeException.class, () -> Parser.parseDateTime("12/12/0000 1212"));
    }

    /**
     * Test that the parseAsCommand method throws a DukeMissingParameterException when the
     * it tries to parse a input as a command, but the required arguments are not provided.
     */
    @Test
    public void parseAsCommand_missingParameter_exceptionThrown() {
        //task commands
        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("todo "));

        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("deadline "));
        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("deadline task"));
        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("deadline /by time"));

        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("event "));
        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("event task "));
        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("event /at time"));

        //other commands
        assertThrows(
                duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("done "));
        assertThrows(duke.command.DukeMissingArgumentException.class, () -> Parser.parseAsCommand("delete "));
    }

    /**
     * Test that the parseAsCommand method properly ignores redundant input when attempting to parse
     * commands which require 0 arguments.
     */
    @Test
    public void parseAsCommand_redundantArgumentForNoArgumentCommands_success() {
        try {
            assertTrue(Parser.parseAsCommand("list 1") instanceof ShowListCommand);
            assertTrue(Parser.parseAsCommand("bye 1") instanceof ExitCommand);
            assertTrue(Parser.parseAsCommand("list a") instanceof ShowListCommand);
            assertTrue(Parser.parseAsCommand("bye a") instanceof ExitCommand);
        } catch (DukeException dukeException) {
            fail("Should return a command ignoring the redundant arguments");
        }
    }

    /**
     * Test that the parseAsCommand method properly returns an AddTaskCommand when the input is valid.
     */
    @Test
    public void parseAsCommand_nonParseableTimeForDeadlineOrEvent_success() {
        try {
            assertTrue(Parser.parseAsCommand("deadline task /by time") instanceof AddTaskCommand);
            assertTrue(Parser.parseAsCommand("event task /at time") instanceof AddTaskCommand);
        } catch (DukeException dukeException) {
            fail("Should return without formatting the time argument into dd/MM/yyyy HHmm");
        }
    }
}