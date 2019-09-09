package duke.tasklist;

/**
 * Abstract class representing a Task which can be added to a TaskList.
 */
public abstract class Task {
    boolean taskCompletionStatus;
    private String taskDescription;

    /**
     * Constructs a Task.
     *
     * @param description The description of the task
     */
    public Task(String description) {
        taskCompletionStatus = false;

        assert description != null : "Task description cannot be null";
        taskDescription = description;
    }

    /**
     * Sets the completion status of the Task to true (is complete).
     *
     * @return This Task which has been marked as completed
     */
    Task complete() {
        taskCompletionStatus = true;
        return this;
    }

    /**
     * Returns true if the Task has been completed, or false otherwise.
     *
     * @return True if the Task is complete, false otherwise
     */
    public boolean isComplete() {
        return taskCompletionStatus;
    }

    /**
     * Returns the description of the Task.
     *
     * @return The description of the task
     */
    public String getDescription() {
        return taskDescription;
    }

    /**
     * Returns the string representation of the Task.
     *
     * @return The string representation of the Task
     */
    @Override
    public String toString() {
        return new StringBuilder("[").append((taskCompletionStatus ? "✓" : "✗"))
                .append("] ")
                .append(taskDescription)
                .toString();
    }
}
