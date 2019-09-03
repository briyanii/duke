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

    /**
     * Constructs an instance of the file handler for a specified file path.
     *
     * @param path The path to the file from the src/ directory
     */
    public Storage(String path) {
        filePath = "src/".concat(path);
    }


    private static void writeFromDeadline(Deadline task, FileWriter file) throws IOException {
        file.append("D");
        file.append(System.lineSeparator());
        file.append(task.isComplete() ? "1" : "0");
        file.append(System.lineSeparator());
        file.append(task.getDescription());
        file.append(System.lineSeparator());
        file.append(task.time);
        file.append(System.lineSeparator());
    }

    private static void writeFromEvent(Event task, FileWriter file) throws IOException {
        file.append("E");
        file.append(System.lineSeparator());
        file.append(task.isComplete() ? "1" : "0");
        file.append(System.lineSeparator());
        file.append(task.getDescription());
        file.append(System.lineSeparator());
        file.append(task.time);
        file.append(System.lineSeparator());
    }

    private static void writeFromToDo(ToDo task, FileWriter file) throws IOException {
        file.append("T");
        file.append(System.lineSeparator());
        file.append(task.isComplete() ? "1" : "0");
        file.append(System.lineSeparator());
        file.append(task.getDescription());
        file.append(System.lineSeparator());
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

            int tasksExpected = Integer.parseInt(file.readLine());
            while (tasksExpected > 0) {
                tasksExpected--;
                switch (file.readLine()) {
                case "D":
                    taskList.add(readAsDeadline(file));
                    break;
                case "E":
                    taskList.add(readAsEvent(file));
                    break;
                case "T":
                    taskList.add(readAsToDo(file));
                    break;
                default:
                    throw new DukeCorruptFileException(filePath);
                }
            }
            return taskList;
        } catch (FileNotFoundException ex) {
            throw new DukeInvalidFilePathException(filePath);
        } catch (IOException | NumberFormatException ex) {
            throw new DukeCorruptFileException(filePath);
        }
    }

    private Deadline readAsDeadline(BufferedReader file) throws IOException {
        boolean isComplete = !file.readLine().equals("0");

        return new Deadline(isComplete, file.readLine(), file.readLine());
    }

    private ToDo readAsToDo(BufferedReader file) throws IOException {
        boolean isComplete = !file.readLine().equals("0");

        return new ToDo(isComplete, file.readLine());
    }

    private Event readAsEvent(BufferedReader file) throws IOException {
        boolean isComplete = !file.readLine().equals("0");

        return new Event(isComplete, file.readLine(), file.readLine());
    }

    /**
     * Writes/Saves the TaskList into the file path specified during initialization of the Storage instance.
     *
     * @param taskList The TaskList to be saved at the file path
     * @throws DukeException when error occurs while trying to save the TaskList
     */
    public void save(TaskList taskList) throws DukeException {
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
//saved file format
/*
    total_#_of_tasks
    task_type
    task_completion_status
    task_description
    task_time(depends on type if needed);
    task_type...
    ...
    ...
    ...
 */