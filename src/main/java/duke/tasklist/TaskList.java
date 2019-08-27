package duke.tasklist;

import duke.command.DukeIncorrectParameterTypeException;
import duke.DukeException;
import java.util.ArrayList;

public class TaskList {
	private ArrayList<Task> taskList;

	public TaskList() {
		taskList = new ArrayList<Task>();
	}

	public Task addTask(Task t) {
		taskList.add(t);
		return t;
	}

	public ArrayList<Task> list() throws DukeException {
		return new ArrayList<Task>(taskList);
	}

	public int size() {
		return taskList.size();
	}

	public Task delete(int id) throws DukeException {
		try {
			return taskList.remove(id - 1);
		} catch (IndexOutOfBoundsException ex) {
			// task id does not correspond to task in list
			throw new DukeNoCorrespondingTaskException(id);
		}
	}

	public Task add(Task task) {
		taskList.add(task);
		return task;
	}

	public Task delete(String id) throws DukeException {
		try {
			return delete(Integer.parseInt(id));
		} catch (NumberFormatException ex) {
			throw new DukeIncorrectParameterTypeException("Integer", id);
		}
	}

	public Task complete(int id) throws DukeException {
		try {
			return taskList.get(id - 1).complete();
		} catch (IndexOutOfBoundsException ex) {
			throw new DukeNoCorrespondingTaskException(id);
		}
	}

	public Task complete(String id) throws DukeException {
		try {
			return complete(Integer.parseInt(id));
		} catch (NumberFormatException ex) {
			throw new DukeIncorrectParameterTypeException("Integer", id);
		}
	}

	public ArrayList<Task> search(String keyword) {
		ArrayList<Task> results = new ArrayList<>();
		for (Task t : this.taskList) {
			if (t.toString().contains(keyword)) {
				results.add(t);
			}
		}
		return results;
	}
}
