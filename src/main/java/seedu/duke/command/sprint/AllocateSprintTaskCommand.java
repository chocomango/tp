package seedu.duke.command.sprint;

import seedu.duke.model.project.Project;
import seedu.duke.model.member.Member;
import seedu.duke.model.project.ProjectManager;
import seedu.duke.model.sprint.Sprint;
import seedu.duke.model.sprint.SprintManager;
import seedu.duke.parser.DateTimeParser;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Hashtable;

public class AllocateSprintTaskCommand extends SprintCommand {
    private SprintManager allSprint;
    private ProjectManager projectManager;
    private Project proj;

    public AllocateSprintTaskCommand(Hashtable<String, String> parameters, ProjectManager projectManager) {
        super(parameters);
        this.projectManager = projectManager;
    }

    public void execute() {
        assert !projectManager.isEmpty() : "No project\n";
        if (projectManager.isEmpty()) {
            Ui.showError("Please create a project first.");
            return;
        }
        proj = projectManager.getProject();
        allSprint = proj.getAllSprints();
        if (allSprint.updateCurrentSprint()) {
            if (validateParams()) {
                int taskId = Integer.parseInt(this.parameters.get("task"));
                String[] userIds = this.parameters.get("user").split(" ");
                for (String id : userIds) {
                    Member mem = proj.getProjectMember().getMember(id.trim());
                    if (mem == null) {
                        Ui.showError("User not found.");
                        return;
                    } else {
                        mem.allocateTask(taskId);
                        proj.getProjectBacklog().getTask(taskId).allocateToMember(mem.getUserId());
                    }
                }
                Ui.showToUserLn(proj.getProjectBacklog().getTask(taskId).getTitle()
                        + " assigned to "
                        + Arrays.toString(userIds));
            }
        } else {
            checkReason();
        }
    }

    private boolean validateParams() {
        return this.parameters.containsKey("task") && this.parameters.containsKey("user");
    }

    private void checkReason() {
        if (allSprint.size() == 0) {
            Ui.showToUserLn("You have yet to create your sprint.");
            return;
        }

        Sprint latestSprint = allSprint.getSprint(allSprint.size() - 1);
        if (DateTimeParser.diff(LocalDate.now(), proj.getEndDate()) == 0) {
            Ui.showToUserLn("Project already ended on " + proj.getEndDate());
            return;
        } else if (DateTimeParser.diff(LocalDate.now(), proj.getStartDate()) > 0) {
            Ui.showToUserLn("Project will start on " + proj.getStartDate());
            return;
        }

        if (DateTimeParser.diff(latestSprint.getEndDate(), LocalDate.now()) >= 0) {
            Ui.showToUserLn("Latest sprint ended on " + latestSprint.getEndDate());
            Ui.showToUserLn("Please create new sprint.");
            return;
        }

        Sprint current = allSprint.getSprint(0);
        if (DateTimeParser.diff(LocalDate.now(), current.getStartDate()) < 0) {
            Ui.showToUserLn("First sprint will start on " + current.getStartDate());
        }
    }
}
