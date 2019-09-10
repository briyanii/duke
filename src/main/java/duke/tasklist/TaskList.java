package duke.tasklist;

import duke.command.DukeIncorrectParameterTypeException;
import duke.error.DukeException;

import java.util.ArrayList;

/**
 * A list to contain Tasks.
 */
public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        taskList = new ArrayList<Task>();
    }

    /**
     * Returns a copy of the ArrayList used to store the Tasks in the TaskList.
     *
     * @return An ArrayList which contains the Tasks in the TaskList
     */
    public ArrayList<Task> list() {
        return new ArrayList<Task>(taskList);
    }

    /**
     * Returns the number of Tasks in the TaskList.
     *
     * @return The number of Tasks in the TaskList
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Deletes the Task which number in the TaskList corresponds to the number provided, if any.
     *
     * @param id The number corresponding to the Task in the TaskList to be deleted
     * @return The Task which was deleted from the TaskList, if the number corresponds
     * @throws DukeException when an error occurs when trying to delete a Task from the TaskList
     */
    private Task delete(int id) throws DukeException {
        assert id > 0;
        try {
            return taskList.remove(id - 1);
        } catch (IndexOutOfBoundsException exception) {
            // task id does not correspond to task in list
            throw new DukeNoCorrespondingTaskException(id);
        }
    }

    /**
     * Deletes the Task which number in the TaskList corresponds to the number provided, if any.
     *
     * @param id The number corresponding to the Task in the TaskList, as a String
     * @return The Task which was deleted from the list, if the number corresponds.
     * @throws DukeException when an error occurs when trying to delete the Task from the list
     */
    public Task delete(String id) throws DukeException {
        assert id != null;
        try {
            return delete(Integer.parseInt(id));
        } catch (NumberFormatException exception) {
            throw new DukeIncorrectParameterTypeException("Integer", id);
        }
    }

    /**
     * Adds the given Task to the TaskList.
     *
     * @param task The Task to be added to the TaskList
     * @return The Task which was added to the TaskList
     */
    public Task add(Task task) {
        assert task != null;
        taskList.add(task);
        return task;
    }


    /**
     * Marks as completed the Task which corresponds to the number provided, if it exists.
     *
     * @param id The number corresponding to the task in the task list
     * @return The Task which was marked as completed
     * @throws DukeException when an error occurs when trying to mark the task as completed
     */
    private Task complete(int id) throws DukeException {
        assert id > 0;
        try {
            return taskList.get(id - 1).complete();
        } catch (IndexOutOfBoundsException exception) {
            throw new DukeNoCorrespondingTaskException(id);
        }
    }

    /**
     * Marks as completed the Task which corresponds to the number provided, if it exists.
     *
     * @param id The number corresponding to the Task in the TaskList, as a String
     * @return The Task which was marked as completed
     * @throws DukeException when an error occurs when trying to mark the Task as completed
     */
    public Task complete(String id) throws DukeException {
        assert id != null;
        try {
            return complete(Integer.parseInt(id));
        } catch (NumberFormatException exception) {
            throw new DukeIncorrectParameterTypeException("Integer", id);
        }
    }

    /**
     * Returns an ArrayList containing the Tasks which contain the keyword provided.
     *
     * @param keyword The keyword to search for in the TaskList
     * @return An ArrayList containing the Tasks which contain the keyword
     */
    public ArrayList<Task> search(String keyword) {
        assert keyword != null;
        //searches the string representation of the class
        ArrayList<Task> results = new ArrayList<>();
        for (Task t : this.taskList) {
            if (t.toString().contains(keyword)) {
                results.add(t);
            }
        }
        return results;
    }
}
