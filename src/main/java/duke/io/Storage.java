package duke.io;

import duke.error.DukeException;

import duke.tasklist.ToDo;
import duke.tasklist.Deadline;
import duke.tasklist.Event;
import duke.tasklist.Task;
import duke.tasklist.TaskList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;


/**
 * File handler responsible for reading/writing from/to files in order to load/save the TaskList
 * in between sessions.
 */
public class Storage {
    private String filePath;
    
    private static final String DEADLINE_FLAG = "D";
    private static final String EVENT_FLAG = "E";
    private static final String TODO_FLAG = "T";
    private static final String COMPLETE = "1";
    private static final String INCOMPLETE = "0";
    
    /**
     * Constructs an instance of the file handler for a specified file path.
     *
     * @param path The path to the file from the src/ directory
     */
    public Storage(String path) {
        assert path != null;
        filePath = "src/".concat(path);
    }

    /**
     * Helper method to write a Deadline Task to a file.
     */
    private static void writeFromDeadline(Deadline task, FileWriter file) throws IOException {
        assert task != null;
        assert file != null;
        writeLinesToFile(
                file,
                DEADLINE_FLAG,
                task.isComplete() ? COMPLETE : INCOMPLETE,
                task.getDescription(),
                task.time);
    }

    /**
     * Helper method to write a Event Task to a file.
     */
    private static void writeFromEvent(Event task, FileWriter file) throws IOException {
        assert task != null;
        assert file != null;
        writeLinesToFile(
                file,
                EVENT_FLAG,
                task.isComplete() ? COMPLETE : INCOMPLETE,
                task.getDescription(),
                task.time);
    }

    /**
     * Helper method to write a ToDo Task to a file.
     */
    private static void writeFromToDo(ToDo task, FileWriter file) throws IOException {
        assert task != null;
        assert file != null;
        writeLinesToFile(
                file,
                TODO_FLAG,
                task.isComplete() ? COMPLETE : INCOMPLETE,
                task.getDescription());
    }

    /**
     * Helper method to write multiple lines to a file.
     */
    private static void writeLinesToFile(FileWriter file, String... lines) throws IOException {
        for (String line : lines) {
            file.append(line);
            file.append(System.lineSeparator());
        }
    }

    /**
     * Returns the TaskList read from the file at the file path specified during initialization
     * of the Storage instance.
     *
     * @return The TaskList read from data stored in the file
     * @throws DukeException Exception thrown when error occurs when trying to recreate the task list
     */
    public TaskList loadTaskList() throws DukeException {
        // read file path
        BufferedReader file;
        try {
            file = new BufferedReader(new FileReader(filePath));

            // populate tasklist
            TaskList taskList = new TaskList();

            for (int tasksExpected = Integer.parseInt(file.readLine()); tasksExpected > 0; tasksExpected--) {
                switch (file.readLine()) {
                case DEADLINE_FLAG:
                    taskList.add(readAsDeadline(file));
                    break;
                case EVENT_FLAG:
                    taskList.add(readAsEvent(file));
                    break;
                case TODO_FLAG:
                    taskList.add(readAsToDo(file));
                    break;
                default:
                    throw new DukeCorruptFileException(filePath);
                }
            }
            return taskList;
        } catch (FileNotFoundException exception) {
            throw new DukeInvalidFilePathException(filePath);
        } catch (IOException | NumberFormatException exception) {
            throw new DukeCorruptFileException(filePath);
        }
    }

    private Deadline readAsDeadline(BufferedReader file) throws IOException {
        assert file != null;

        return new Deadline(!file.readLine().equals(INCOMPLETE), file.readLine(), file.readLine());
    }

    private ToDo readAsToDo(BufferedReader file) throws IOException {
        assert file != null;

        return new ToDo(!file.readLine().equals(INCOMPLETE), file.readLine());
    }

    private Event readAsEvent(BufferedReader file) throws IOException {
        assert file != null;

        return new Event(!file.readLine().equals(INCOMPLETE), file.readLine(), file.readLine());
    }

    /**
     * Writes/Saves the TaskList into the file path specified during initialization of the Storage instance.
     *
     * @param taskList The TaskList to be saved at the file path
     * @throws DukeException when error occurs while trying to save the TaskList
     */
    public void save(TaskList taskList) throws DukeException {
        assert taskList != null;
        FileWriter file;
        try {
            file = new FileWriter(filePath);

            // list size
            file.append(Integer.toString(taskList.size()));
            file.append(System.lineSeparator());

            // per task in list
            for (Task task : taskList.list()) {
                switch (task.getClass().getSimpleName()) {
                case "ToDo":
                    writeFromToDo((ToDo) task, file);
                    break;
                case "Event":
                    writeFromEvent((Event) task, file);
                    break;
                case "Deadline":
                    writeFromDeadline((Deadline) task, file);
                    break;
                default:
                    break;
                }
            }

            file.close();
        } catch (FileNotFoundException exception) {
            throw new DukeInvalidFilePathException(filePath);
        } catch (IOException exception) {
            // FileNotFoundException should the only exception, if it is not then:
            System.err.println(exception.getMessage());
        }
    }

}