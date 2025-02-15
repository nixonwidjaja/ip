package duke.command;

import duke.DukeException;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * This command functions to mark a task at specified index.
 */
public class MarkCommand extends Command {
    private int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Ui ui) throws DukeException {
        boolean marked = tasks.markDone(index);
        return ui.mark(tasks.get(index), marked);
    }
}
