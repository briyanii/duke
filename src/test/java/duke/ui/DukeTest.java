package duke.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class to test functionality of certain methods in the Duke class.
 */
public class DukeTest {

    private static Duke duke;

    @BeforeEach
    void setUpDuke() {
        duke = new Duke();
        duke.greet();
        duke.setUp("test/resources/save/DukeTestSave01.txt");
    }

    @BeforeEach
    void clearTestTaskList() {
        try {
            FileWriter temp = new FileWriter("src/test/resources/save/DukeTestSave01.txt");
            temp.write("0");
            temp.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Test that typos and unsupported commands result in an error Response from Duke.
     */
    @Test
    public void getResponse_invalidInputs_responseCausedByError() {
        assertTrue(duke.getResponse("deAdline").wasCausedByError());
        assertTrue(duke.getResponse("ddasd").wasCausedByError());
        assertTrue(duke.getResponse("what").wasCausedByError());
        assertTrue(duke.getResponse("").wasCausedByError());
        assertTrue(duke.getResponse("   ").wasCausedByError());
        assertTrue(duke.getResponse("fi nd").wasCausedByError());
    }


    /**
     * Test that bad syntax for commands to add tasks result in an error Response from Duke for the
     * following situations.
     * <ul>
     * <li>Missing arguments</li>
     * <li>Wrong delimiter</li>
     * </ul>
     */
    @Test
    public void getResponse_invalidAddTaskInputs_responseCausedByError() {
        assertTrue(duke.getResponse("todo").wasCausedByError());

        assertTrue(duke.getResponse("deadline").wasCausedByError());
        assertTrue(duke.getResponse("deadline a").wasCausedByError());
        assertTrue(duke.getResponse("deadline a /by").wasCausedByError());
        assertTrue(duke.getResponse("deadline /by").wasCausedByError());
        assertTrue(duke.getResponse("deadline /by 12").wasCausedByError());
        assertTrue(duke.getResponse("deadline a /at 12").wasCausedByError());

        assertTrue(duke.getResponse("event").wasCausedByError());
        assertTrue(duke.getResponse("event a").wasCausedByError());
        assertTrue(duke.getResponse("event a /at").wasCausedByError());
        assertTrue(duke.getResponse("event /at").wasCausedByError());
        assertTrue(duke.getResponse("event /at 12").wasCausedByError());
        assertTrue(duke.getResponse("event a /by 12").wasCausedByError());
    }

    /**
     * Test that bad syntax for commands to delete tasks result in an error Response from Duke for the
     * following situations.
     * <ul>
     * <li>no argument</li>
     * <li>negative integer</li>
     * <li>zero</li>
     * <li>argument not parsable as integer</li>
     * </ul>
     */
    @Test
    public void getResponse_invalidDeleteInputs_responseCausedByError() {
        assertTrue(duke.getResponse("delete").wasCausedByError());
        assertTrue(duke.getResponse("delete a").wasCausedByError());
    }

    /**
     * Test that bad syntax for commands to complete tasks result in an error Response from Duke for the
     * following situations.
     * <ul>
     * <li>no argument</li>
     * <li>negative integer</li>
     * <li>zero</li>
     * <li>argument not parsable as integer</li>
     * </ul>
     */
    @Test
    public void getResponse_invalidCompleteInputs_responseCausedByError() {
        assertTrue(duke.getResponse("done").wasCausedByError());
        assertTrue(duke.getResponse("done a").wasCausedByError());
    }


    /**
     * Test that bad syntax for commands to search for tasks result in an error Response from Duke
     * when no argument is given.
     */
    @Test
    public void getResponse_invalidSearchInputs_responseCausedByError() {
        assertTrue(duke.getResponse("find").wasCausedByError());
    }

    /**
     * Test that proper syntax for commands to search for tasks result in a normal Response from Duke.
     */
    @Test
    public void getResponse_validSearchInput_responseNotCausedByError() {
        assertFalse(duke.getResponse("find afsdf").wasCausedByError());
        assertFalse(duke.getResponse("find afsdf").wasCausedByError());
        assertFalse(duke.getResponse("find 1").wasCausedByError());
    }

    /**
     * Test that parameter-less commands (list, bye) result in a normal Response from Duke in the
     * following situations.
     * <ul>
     * <li>normal usage (no argument given)</li>
     * <li>with redundant argument given</li>
     * </ul>
     */
    @Test
    public void getResponse_validParameterlessCommand_responseNotCausedByError() {
        assertFalse(duke.getResponse("list").wasCausedByError());
        assertFalse(duke.getResponse("list asdas").wasCausedByError());
        assertFalse(duke.getResponse("bye").wasCausedByError());
        setUpDuke();
        assertFalse(duke.getResponse("bye asdas").wasCausedByError());
    }


    /**
     * Test that after the command to exit is given to Duke, that it is inactive and
     * response to further input is caused by error.
     */
    @Test
    public void getResponse_exitCommand_dukeInactiveAndResponseCausedByError() {
        // while accepting input
        assertTrue(duke.isActive());
        // stops accepting input after exit
        assertFalse(duke.getResponse("bye").wasCausedByError());
        assertTrue(duke.getResponse("bye").wasCausedByError());
        // after stop accepting input
        assertFalse(duke.isActive());
    }


    /**
     * Test that proper syntax for commands to add tasks result in an normal Response from Duke.
     */
    @Test
    public void getResponse_validAddTaskInputs_responseNotCausedByError() {
        assertFalse(duke.getResponse("todo test1").wasCausedByError());
        assertFalse(duke.getResponse("deadline test2 /by 12").wasCausedByError());
        assertFalse(duke.getResponse("deadline test3 /by 12/12/1212 1212").wasCausedByError());
        assertFalse(duke.getResponse("event test4 /at 12").wasCausedByError());
        assertFalse(duke.getResponse("event test5 /at 12/12/1212 1212").wasCausedByError());
    }

    /**
     * Test that proper syntax for commands to delete tasks result in an normal Response from Duke.
     */
    @Test
    public void getResponse_validDeleteTaskInputs_responseNotCausedByError() {
        duke.getResponse("todo test1").wasCausedByError();
        duke.getResponse("deadline test2 /by 12").wasCausedByError();
        duke.getResponse("deadline test3 /by 12/12/1212 1212").wasCausedByError();
        duke.getResponse("event test4 /at 12").wasCausedByError();
        duke.getResponse("event test5 /at 12/12/1212 1212").wasCausedByError();

        assertFalse(duke.getResponse("delete 5").wasCausedByError());
        assertFalse(duke.getResponse("delete 4").wasCausedByError());
        assertFalse(duke.getResponse("delete 3").wasCausedByError());
        assertFalse(duke.getResponse("delete 1").wasCausedByError());
        assertFalse(duke.getResponse("delete 1").wasCausedByError());
    }

    /**
     * Test that proper syntax for commands to mark tasks as complete results in an normal Response from Duke.
     */
    @Test
    public void getResponse_validCompleteTaskInputs_responseNotCausedByError() {
        duke.getResponse("todo test1");
        duke.getResponse("deadline test2 /by 12");
        duke.getResponse("deadline test3 /by 12/12/1212 1212");
        duke.getResponse("event test4 /at 12");
        duke.getResponse("event test5 /at 12/12/1212 1212");

        assertFalse(duke.getResponse("done 5").wasCausedByError());
        assertFalse(duke.getResponse("done 4").wasCausedByError());
        assertFalse(duke.getResponse("done 3").wasCausedByError());
        assertFalse(duke.getResponse("done 2").wasCausedByError());
        assertFalse(duke.getResponse("done 1").wasCausedByError());
    }

    /**
     * Test that tasks are saved between sessions.
     */
    @Test
    public void exitAndSetUp_manipulateLastSessionsTasks_responseNotCausedByError() {
        duke.getResponse("todo a");
        duke.getResponse("todo b");
        duke.getResponse("bye");

        setUpDuke();

        assertFalse(duke.getResponse("done 1").wasCausedByError());
        assertFalse(duke.getResponse("done 2").wasCausedByError());

        assertFalse(duke.getResponse("delete 1").wasCausedByError());
        assertFalse(duke.getResponse("delete 1").wasCausedByError());
    }
}