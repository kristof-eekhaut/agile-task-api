package be.eekhaut.kristof.agile.task.validation.rules;

import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.List;

@Component
public class DeleteOfTaskWithSubTasksNotAllowedRule {

    private final TaskRepository taskRepository;

    @Autowired
    public DeleteOfTaskWithSubTasksNotAllowedRule(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void check(Task task, Errors errors) {

        List<Task> subTasks = taskRepository.findTasksByParentTaskId(task.getId());

        if(!subTasks.isEmpty()) {
            errors.reject("task.delete-with-sub-tasks-not-allowed");
        }
    }
}
